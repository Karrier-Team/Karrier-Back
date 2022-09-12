package com.karrier.mentoring.service.mail;

import com.karrier.mentoring.entity.Program;

import javax.mail.internet.MimeMessage;
import java.util.Random;

// 추후 이메일 서비스의 확정성을 고려
public interface EmailService<T> {
    // 메세지 전송
    public void sendSimpleMessage(T object);

    //이메일 만들기
    public MimeMessage createMessage(String to);
    MimeMessage createMessage(String to, Program program);


}
