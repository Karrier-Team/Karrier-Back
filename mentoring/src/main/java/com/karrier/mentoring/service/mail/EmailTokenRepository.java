package com.karrier.mentoring.service.mail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken,String> {
    Optional<EmailToken> findByTokenAndExpirationDateAfterAndExpired(String token, LocalDateTime now, boolean expired);
}
