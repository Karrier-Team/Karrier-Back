package com.karrier.mentoring.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;

    public String createEmailToken(String memberEmail, String receiverEmail) {

        // 이메일 토큰 저장
        EmailToken emailToken = EmailToken.createEmailToken(memberEmail);

        emailTokenRepository.save(emailToken);

        return emailToken.getToken();    // 인증메일 전송 시 토큰 반환

    }

    public void deleteEmailToken(String id){
        emailTokenRepository.deleteById(id);
    }

    // 유효한 토큰 가져오기
    public EmailToken findByTokenAndExpirationDateAfterAndExpired(String emailTokenId) {
        Optional<EmailToken> emailToken = emailTokenRepository
                .findByTokenAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false);
        // 토큰이 없다면 예외 발생
        return emailToken.orElseThrow(() -> new IllegalArgumentException("인증 Token 정보가 일치하지 않습니다."));
    }

}
