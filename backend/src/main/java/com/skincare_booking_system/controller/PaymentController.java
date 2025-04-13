package com.skincare_booking_system.controller;

import java.io.UnsupportedEncodingException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.entities.Payment;
import com.skincare_booking_system.entities.Transactions;
import com.skincare_booking_system.entities.User;
import com.skincare_booking_system.repository.PaymentRepository;
import com.skincare_booking_system.repository.TransactionsRepository;
import com.skincare_booking_system.repository.UserRepository;
import com.skincare_booking_system.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final TransactionsRepository transactionsRepository;
    private final UserRepository userRepository;

    public PaymentController(
            PaymentService paymentService,
            PaymentRepository paymentRepository,
            TransactionsRepository transactionsRepository,
            UserRepository userRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.transactionsRepository = transactionsRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{bookingId}")
    public String getPay(@PathVariable Long bookingId, HttpServletRequest req) throws UnsupportedEncodingException {

        return paymentService.generatePaymentUrl(bookingId, req);
    }

    @GetMapping("/response")
    public ResponseEntity<String> checkPaymentSuccess(
            @RequestParam String vnp_BankCode,
            @RequestParam String vnp_CardType,
            @RequestParam String vnp_ResponseCode,
            @RequestParam String vnp_TxnRef) {
        // String vnp_ResponseCode = vnp_Params.get("vnp_ResponseCode");
        // String vnp_txnRef = vnp_Params.get("vnp_TxnRef");
        Payment payment = paymentRepository.findByTransactionId(vnp_TxnRef);
        // Kiểm tra mã phản hồi từ VNPay
        if ("00".equals(vnp_ResponseCode)) {
            // Giao dịch thành công
            payment.setPaymentMethod("VNPAY-Banking");
            paymentRepository.save(payment);
            Transactions transactions = new Transactions();
            transactions.setTransactionIdVNPay(payment.getTransactionId());
            transactions.setAmount(payment.getPaymentAmount());
            transactions.setFromAccount(payment.getBooking().getUser());
            User adminAccount = userRepository.findByUsername("admin").orElse(null);
            transactions.setToAccount(adminAccount);
            transactions.setTransactionDate(payment.getPaymentDate());
            transactions.setBankCode(vnp_BankCode);
            transactions.setCardType(vnp_CardType);
            transactions.setPayment(payment);
            transactionsRepository.save(transactions);
            return ResponseEntity.ok("Payment success. Transaction ID: " + vnp_TxnRef);
        } else {
            // Giao dịch không thành công
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed with code: " + vnp_ResponseCode);
        }
    }
}
