package com.example.userbackend.service;

import com.example.userbackend.dto.LoginRequestDto;
import com.example.userbackend.dto.OtpVerifyRequestDto;
import com.example.userbackend.model.OtpToken;
import com.example.userbackend.model.User;
import com.example.userbackend.repository.OtpTokenRepository;
import com.example.userbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final OtpTokenRepository otpTokenRepository;

    public AuthService(UserRepository userRepository, UserService userService, OtpTokenRepository otpTokenRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.otpTokenRepository = otpTokenRepository;
    }

    public String loginAndSendOtp(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (!userService.verifyPassword(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        String otp = generateOtp();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);
        OtpToken token = new OtpToken(user.getEmail(), otp, expiresAt);
        otpTokenRepository.save(token);
        return otp;
    }

    public String verifyOtp(OtpVerifyRequestDto dto) {
        OtpToken token = otpTokenRepository.findTopByEmailOrderByIdDesc(dto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("OTP not found for email"));
        if (token.isUsed()) {
            throw new IllegalStateException("OTP already used");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("OTP expired");
        }
        if (!token.getOtpCode().equals(dto.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        token.setUsed(true);
        otpTokenRepository.save(token);
        return "Login successful";
    }

    private String generateOtp() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}
