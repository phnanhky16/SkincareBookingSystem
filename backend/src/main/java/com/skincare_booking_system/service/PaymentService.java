package com.skincare_booking_system.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.skincare_booking_system.configuration.VnpayConfig;
import com.skincare_booking_system.controller.PaymentController;
import com.skincare_booking_system.entities.Booking;
import com.skincare_booking_system.entities.Payment;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.repository.BookingRepository;
import com.skincare_booking_system.repository.PaymentRepository;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(BookingRepository bookingRepository, PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    public String generatePaymentUrl(Long bookingId, HttpServletRequest req) throws UnsupportedEncodingException {
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        if (booking == null) {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND);
        }

        Payment payment = paymentRepository.findPaymentByBooking(booking);
        if (payment == null || payment.getPaymentAmount() <= 0) {
            throw new AppException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = (long) (payment.getPaymentAmount() * 100);
        String bankCode = req.getParameter("bankCode");
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);

        String vnp_IpAddr = VnpayConfig.getIpAddress(req);
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.HOUR, 48);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName)
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + query.toString();
        log.info("VNPay Request Params: " + vnp_Params);
        log.info("VNPay Payment URL: " + paymentUrl);
        payment.setTransactionId(vnp_TxnRef);
        paymentRepository.save(payment);
        return paymentUrl;
    }
}
