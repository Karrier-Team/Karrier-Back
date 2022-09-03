package com.karrier.mentoring.service.mail;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PwdCertEmailServiceImpl implements EmailService{


    private final JavaMailSender javaMailSender;

    private final MemberService memberService;

    private final EmailTokenService emailTokenService;

    private void validateExistEmailAndSocialMember(String to){
        Member member = memberService.getMember(to);
        if(member==null){
            throw new IllegalArgumentException("회원 등록된 사용자 이메일이 없습니다.");
        }
        else {
            if (memberService.isSocialMember(member)) {
                throw new IllegalArgumentException("Social 계정으로 회원가입 된 유저는 비밀번호를 변경 할 수 없습니다.");
            }
        }
    }

    @Override
    public MimeMessage createMessage(String to) throws Exception{

        // 존재하는 이메일인지 그리고 소셜 멤버인지 필터
        //validateExistEmailAndSocialMember(to);

        // 현재는 보내는 사람의 id(PK)가 receiver의 email이다. // 추후 변경 가능
        String emailToken = emailTokenService.createEmailToken(to,to);

        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+emailToken);

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
        msgg+= emailToken+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("tsi0521o@gmail.com","Karrier"));//보내는 사람

        return message;
    }

    @Override
    public boolean verifyEmail(String token) {

        // 이메일 토큰을 찾아옴
        EmailToken findEmailToken = emailTokenService.findByTokenAndExpirationDateAfterAndExpired(token);
        // 사용 완료
        findEmailToken.setTokenToUsed();

        return true;
    }

    @Override
    public void sendSimpleMessage(String to) throws Exception {
        MimeMessage message = createMessage(to);
        javaMailSender.send(message);
    }
}
