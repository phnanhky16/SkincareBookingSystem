package com.skincare_booking_system.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;
import com.skincare_booking_system.dto.request.*;
import com.skincare_booking_system.dto.response.AuthenticationResponse;
import com.skincare_booking_system.dto.response.IntrospectResponse;
import com.skincare_booking_system.service.AuthenticationService;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.login(request);

        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/login-gg")
    private ApiResponse<AuthenticationResponse> checkLoginGoogle(@RequestBody LoginGG loginGG) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.loginGoogle(loginGG.getToken()))
                .success(true)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder().message("Successfully logged out").build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/refreshThe")
    ApiResponse<AuthenticationResponse> refreshThe(@RequestBody RefreshRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.refreshTokenThe(request);

        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/refreshSta")
    ApiResponse<AuthenticationResponse> refreshSta(@RequestBody RefreshRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.refreshTokenStaff(request);

        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }
}
