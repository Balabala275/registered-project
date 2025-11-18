package com.example.userbackend.repository;

import com.example.userbackend.model.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailOrderByIdDesc(String email);
}
