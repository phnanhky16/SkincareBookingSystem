package com.skincare_booking_system.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skincare_booking_system.dto.request.VoucherRequest;
import com.skincare_booking_system.dto.response.VoucherResponse;
import com.skincare_booking_system.entities.Voucher;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.mapper.VoucherMapper;
import com.skincare_booking_system.repository.VoucherRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class VoucherService {

    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;

    public VoucherResponse createVoucher(VoucherRequest request) {
        if (voucherRepository.existsByVoucherCode(request.getVoucherCode())) {
            throw new AppException(ErrorCode.VOUCHER_CODE_EXISTS);
        }
        if (voucherRepository.existsByVoucherName(request.getVoucherName())) {
            throw new AppException(ErrorCode.VOUCHER_NAME_EXISTS);
        }
        if (request.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRY_DATE_INVALID);
        }
        if (request.getQuantity() <= 0) {
            throw new AppException(ErrorCode.VOUCHER_QUANTITY_INVALID);
        }

        Voucher voucher = voucherMapper.toVoucher(request);
        voucher.setIsActive(true);
        return voucherMapper.toVoucherResponse(voucherRepository.save(voucher));
    }

    public List<VoucherResponse> getAllVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        if (vouchers.isEmpty()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }
        return vouchers.stream().map(voucherMapper::toVoucherResponse).collect(Collectors.toList());
    }

    public VoucherResponse getVoucherByCode(String voucherCode) {
        Voucher voucher = voucherRepository
                .findByVoucherCode(voucherCode)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (!voucher.getIsActive()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_ACTIVE);
        }

        if (voucher.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        if (voucher.getQuantity() <= 0) {
            throw new AppException(ErrorCode.VOUCHER_OUT_OF_STOCK);
        }

        return voucherMapper.toVoucherResponse(voucher);
    }

    public VoucherResponse getVoucherById(Long id) {
        Voucher voucher =
                voucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (!voucher.getIsActive()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_ACTIVE);
        }

        if (voucher.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        if (voucher.getQuantity() <= 0) {
            throw new AppException(ErrorCode.VOUCHER_OUT_OF_STOCK);
        }

        return voucherMapper.toVoucherResponse(voucher);
    }

    public List<VoucherResponse> getActiveVouchers() {
        List<Voucher> vouchers =
                voucherRepository.findByIsActiveTrueAndQuantityGreaterThanAndExpiryDateAfter(0, LocalDate.now());

        if (vouchers.isEmpty()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }

        return vouchers.stream().map(voucherMapper::toVoucherResponse).toList();
    }

    public List<VoucherResponse> getInactiveVouchers() {
        List<Voucher> vouchers = voucherRepository.findByIsActiveFalse();
        if (vouchers.isEmpty()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }
        return vouchers.stream().map(voucherMapper::toVoucherResponse).toList();
    }

    public String deactivateVoucher(Long id) {
        Voucher voucher =
                voucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (!voucher.getIsActive()) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_INACTIVE);
        }

        voucher.setIsActive(false);
        voucherRepository.save(voucher);
        return "Voucher deactivated successfully";
    }

    public String activateVoucher(Long id) {
        Voucher voucher =
                voucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (voucher.getIsActive()) {
            throw new AppException(ErrorCode.VOUCHER_ALREADY_ACTIVE);
        }

        if (voucher.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        if (voucher.getQuantity() <= 0) {
            throw new AppException(ErrorCode.VOUCHER_QUANTITY_INVALID);
        }

        voucher.setIsActive(true);
        voucherRepository.save(voucher);
        return "Voucher activated successfully";
    }

    public VoucherResponse updateVoucher(Long id, VoucherRequest voucherRequest) {
        Voucher v = voucherRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherMapper.updateVoucher(v, voucherRequest);
        return voucherMapper.toVoucherResponse(voucherRepository.save(v));
    }

    public String useVoucher(String voucherCode) {
        Voucher voucher = voucherRepository
                .findByVoucherCode(voucherCode)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (!voucher.getIsActive()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_ACTIVE);
        }

        if (voucher.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        if (voucher.getQuantity() <= 0) {
            throw new AppException(ErrorCode.VOUCHER_OUT_OF_STOCK);
        }

        voucher.setQuantity(voucher.getQuantity() - 1);
        if (voucher.getQuantity() == 0) {
            voucher.setIsActive(false);
        }
        voucherRepository.save(voucher);
        return "Voucher used successfully";
    }
}
