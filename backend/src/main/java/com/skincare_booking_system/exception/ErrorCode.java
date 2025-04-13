package com.skincare_booking_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    KEY_INVALID("Key is invalid", HttpStatus.BAD_REQUEST),
    USER_EXISTED("User already exists", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED("User already exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID("Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID("Your password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_WRONG("Old password is incorrect", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("New password and confirm password do not match", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID("Your email is not corret", HttpStatus.BAD_REQUEST),
    BLANK_FIELD("Field cannot be blank", HttpStatus.BAD_REQUEST),
    INVALID_OTP("Invalid OTP", HttpStatus.BAD_REQUEST),
    OTP_HAS_EXPIRED("OTP has expired", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED("User not found", HttpStatus.NOT_FOUND),
    EMAIL_NOT_EXISTED("Email not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATION("Unauthenticated", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILED("Wrong username or password", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("You do not have permission", HttpStatus.FORBIDDEN),
    PHONENUMBER_EXISTED("Your phone number already used", HttpStatus.BAD_REQUEST),
    PHONENUMBER_INVALID("Your phone number is not valid", HttpStatus.BAD_REQUEST),
    GENDER_INVALID("Invalid {gender} selection", HttpStatus.BAD_REQUEST),
    PRICE_INVALID("Price must be at least 0", HttpStatus.BAD_REQUEST),
    SERVICE_EXIST("Service exist", HttpStatus.BAD_REQUEST),
    SERVICE_NOT_FOUND("Service not found", HttpStatus.BAD_REQUEST),
    DESCRIPTION_INVALID("Description is not more than 150", HttpStatus.BAD_REQUEST),
    CATEGORY_INVALID("Category is not more 50", HttpStatus.BAD_REQUEST),
    SLOT_ID_EXISTED("Slot id existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED("Your email already used", HttpStatus.BAD_REQUEST),
    SLOT_NOT_FOUND("Slot not found", HttpStatus.NOT_FOUND),
    BOOKING_NOT_FOUND("Booking not found", HttpStatus.NOT_FOUND),
    SERVICES_INVALID("No services found with the provided names.", HttpStatus.BAD_REQUEST),
    SERVICES_FAILED("Some services are not active. Please check again.", HttpStatus.BAD_REQUEST),
    PACKAGE_NOT_FOUND("Package not found", HttpStatus.NOT_FOUND),
    PACKAGE_EXIST("Package exist", HttpStatus.BAD_REQUEST),
    VOUCHER_CODE_EXISTS("Voucher code already exists", HttpStatus.BAD_REQUEST),
    VOUCHER_NAME_EXISTS("Voucher name already exists", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND("Voucher not found", HttpStatus.NOT_FOUND),
    EXPIRY_DATE_REQUIRED("Expiry date is required", HttpStatus.BAD_REQUEST),
    QUANTITY_REQUIRED("Quantity is required", HttpStatus.BAD_REQUEST),
    QUANTITY_MIN("Quantity must be greater than or equal to 0", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRED("Voucher has expired", HttpStatus.BAD_REQUEST),
    VOUCHER_OUT_OF_STOCK("Voucher is out of stock", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_ACTIVE("Voucher is already active", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_INACTIVE("Voucher is already inactive", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRY_DATE_INVALID("Expiry date must be in the future", HttpStatus.BAD_REQUEST),
    VOUCHER_QUANTITY_INVALID("Quantity must be greater than 0", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_USED("Voucher has already been used", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_ACTIVE("Voucher is not active", HttpStatus.BAD_REQUEST),
    IMG_URL_REQUIRED("Image URL is required", HttpStatus.BAD_REQUEST),
    IMG_URL_TOO_LONG("Image URL must not exceed 500 characters", HttpStatus.BAD_REQUEST),
    IMG_URL_INVALID("Invalid image URL. Only accepted formats: jpg, jpeg, png, gif", HttpStatus.BAD_REQUEST),
    THERAPIST_SCHEDULE_EXIST("Stylist already have schedule in this day", HttpStatus.BAD_REQUEST),
    SLOT_TIME_ALREADY_EXISTS("Slot time already exists", HttpStatus.BAD_REQUEST),
    TITLE_NOT_EMPTY("Title cannot be empty", HttpStatus.BAD_REQUEST),
    TITLE_INVALID("Title must be between 10 and 200 characters", HttpStatus.BAD_REQUEST),
    CONTENT_NOT_EMPTY("Content cannot be empty", HttpStatus.BAD_REQUEST),
    CONTENT_INVALID("Content must have at least 100 characters", HttpStatus.BAD_REQUEST),
    BLOG_ALREADY_USED("Blog already used", HttpStatus.BAD_REQUEST),
    BLOG_NOT_FOUND("Blog not found", HttpStatus.NOT_FOUND),
    SHIFT_EXIST("Shift already exists", HttpStatus.BAD_REQUEST),
    SHIFT_NOT_EXIST("Shift is not exists", HttpStatus.BAD_REQUEST),
    LIMIT_BOOKING_INVALID("Booking limit is at least 1", HttpStatus.BAD_REQUEST),
    SLOT_NOT_VALID("Slot is not valid", HttpStatus.BAD_REQUEST),
    BOOKING_EXIST("Booking already exists", HttpStatus.BAD_REQUEST),
    THERAPIST_NOT_FOUND("Therapist not found", HttpStatus.NOT_FOUND),
    INVALID_PAYMENT_AMOUNT("Invalid payment amount", HttpStatus.BAD_REQUEST),
    EXCEPTION("Error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICES_ALREADY_BOOKED("Service is already booked", HttpStatus.BAD_REQUEST),
    THERAPIST_UNAVAILABLE("Stylist is not available", HttpStatus.BAD_REQUEST),
    CUSTOMER_DE_ACTIVE("Your account has been blocked", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(String message, HttpStatusCode httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
