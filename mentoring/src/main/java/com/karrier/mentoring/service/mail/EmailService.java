package com.karrier.mentoring.service.mail;

import javax.mail.internet.MimeMessage;
import java.util.Random;

// 추후 이메일 서비스의 확정성을 고려
public interface EmailService {
    // 메세지 전송
    public void sendSimpleMessage(String to) throws Exception;

    //이메일 만들기
    public MimeMessage createMessage(String to) throws Exception;

    //이메일 인증
    public boolean verifyEmail(String token);
}
