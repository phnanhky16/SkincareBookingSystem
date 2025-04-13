package com.skincare_booking_system.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.skincare_booking_system.dto.request.*;
import com.skincare_booking_system.dto.response.UserResponse;
import com.skincare_booking_system.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    ApiResponse<UserResponse> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.registerUser(request))
                .build();
    }

    @GetMapping()
    ApiResponse<List<UserResponse>> getUserList() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{phoneNumber}")
    ApiResponse<UserResponse> getUser(@PathVariable("phoneNumber") String phone) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByPhoneNumber(phone))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/update")
    ApiResponse<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(request))
                .message("User updated successfully")
                .build();
    }

    @PutMapping("/{id}")
    String deleteUser(@PathVariable long id) {
        System.out.println("Deleting user with ID: " + id);
        userService.deleteUser(id);
        return "User has been deleted";
    }

    @PutMapping("/active/{id}")
    String activeUser(@PathVariable long id) {
        userService.activeUser(id);
        return "Active user successfully";
    }

    @PutMapping("/change-password")
    public ApiResponse<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(request);
        return ApiResponse.<String>builder().result("Password has been changed").build();
    }

    @PutMapping("/reset-password/{id}")
    public ApiResponse<String> resetPassword(@PathVariable long id, @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request, id);
        return ApiResponse.<String>builder().result("Password has been reset").build();
    }
}
