package com.example.userbackend.controller;

import com.example.userbackend.dto.LoginRequestDto;
import com.example.userbackend.dto.OtpVerifyRequestDto;
import com.example.userbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        String otp = authService.loginAndSendOtp(dto);
        return ResponseEntity.ok("OTP generated: " + otp);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequestDto dto) {
        String result = authService.verifyOtp(dto);
        return ResponseEntity.ok(result);
    }
}
