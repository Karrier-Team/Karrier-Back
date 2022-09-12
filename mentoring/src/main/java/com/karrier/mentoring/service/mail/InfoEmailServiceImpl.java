package com.karrier.mentoring.service.mail;

import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.InternalServerException;
import com.karrier.mentoring.http.error.exception.UnsupportedMediaTypeException;
import com.karrier.mentoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

@Transactional
@RequiredArgsConstructor
@Service
public class InfoEmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    private final MemberService memberService;

    @Override
    public void sendSimpleMessage(String to){
        MimeMessage message = createMessage(to);
        javaMailSender.send(message);
    }

    @Override
    public MimeMessage createMessage(String to) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String subStr = "멘토링 프로그램 신청이 완료되었습니다.";
        String msgg="";
        try {
            message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
            message.setSubject(subStr);//제목
            message.setText(msgg, "utf-8", "html");//내용
            message.setFrom(new InternetAddress("good.karrier@gmail.com","Karrier"));//보내는 사람
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new InternalServerException(ErrorCode.MESSAGING_ERROR);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new UnsupportedMediaTypeException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
        }
        return message;
    }
}
