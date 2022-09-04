package com.karrier.mentoring.service.mail;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.BadRequestException;
import com.karrier.mentoring.http.error.exception.InternalServerException;
import com.karrier.mentoring.http.error.exception.NotFoundException;
import com.karrier.mentoring.http.error.exception.UnsupportedMediaTypeException;
import com.karrier.mentoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

@Transactional
@RequiredArgsConstructor
@Service
public class PwdCertEmailServiceImpl implements EmailService{


    private final JavaMailSender javaMailSender;

    private final MemberService memberService;

    private final EmailTokenService emailTokenService;

    private final HttpSession httpSession;

    private void validateExistEmailAndSocialMember(String to){
        Member member = memberService.getMember(to);
        if(member==null){
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        else {
            if (memberService.isSocialMember(member)) {
                throw new BadRequestException(ErrorCode.VALIDATE_SOCIAL_USER);
            }
        }
    }

    @Override
    public MimeMessage createMessage(String to){

        // 존재하는 이메일인지 그리고 소셜 멤버인지 필터
        validateExistEmailAndSocialMember(to);

        // 현재는 보내는 사람의 id(PK)가 receiver의 email이다. // 추후 변경 가능
        String emailToken = emailTokenService.createEmailToken(to,to);

        MimeMessage message = javaMailSender.createMimeMessage();

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

        try {
            message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
            message.setSubject("Karrier 인증번호가 도착했습니다.");//제목
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

    @Override
    public void verifyEmail(String token) {

        // 이메일 토큰을 찾아옴
        EmailToken findEmailToken = emailTokenService.findByTokenAndExpirationDateAfterAndExpired(token);
        // 사용 완료
        findEmailToken.setTokenToUsed();
        // 비밀번호 수정을 진행하는 사용자의 아이디를 세션에 등록한다.
        httpSession.setAttribute("verifiedMemberEmail", findEmailToken.getMemberEmail());

    }

    @Override
    public void sendSimpleMessage(String to) {
        MimeMessage message = createMessage(to);
        javaMailSender.send(message);
    }
}
