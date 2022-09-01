package com.karrier.mentoring.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class PwdCertEmailServiceImpl implements EmailService{

    @Autowired
    JavaMailSender javaMailSender;

    private MimeMessage createMessage(String to)throws Exception{

        String emailKey = EmailService.createKey();

        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+emailKey);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
        message.setSubject("Karrier 인증번호가 도착했습니다.");//제목

        String msgg="";
        msgg+= "<div style='margin:100px;'>";
        msgg+= "<h1> 안녕하세요 Karrier입니다!!! </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 비밀번호 변경 창으로 돌아가 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다!<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= emailKey+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("properties에 작성한 이메일","BZshop"));//보내는 사람

        return message;
    }

    @Override
    public void sendSimpleMessage(String to) {
        try {
            MimeMessage message = createMessage(to);
            javaMailSender.send(message);
        }
        catch (AddressException e){
            e.printStackTrace();
            throw new IllegalArgumentException("메세지 전송과정에서 AddressException이 발생했습니다.");
        }
        catch (MessagingException e){
            e.printStackTrace();
            throw new IllegalArgumentException("메세지 전송과정에서 MessagingException이 발생했습니다.");
        }
        catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException("메세지 전송과정에서 MailException이 발생했습니다.");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메세지 전송과정에서 UnsupportedEncodingException이 발생했습니다.");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메세지 전송과정에서 Exception이 발생했습니다.");
        }
    }
}
