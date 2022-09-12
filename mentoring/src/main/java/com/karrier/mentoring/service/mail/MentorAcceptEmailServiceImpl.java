package com.karrier.mentoring.service.mail;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.InternalServerException;
import com.karrier.mentoring.http.error.exception.UnsupportedMediaTypeException;
import com.karrier.mentoring.service.MemberService;
import com.karrier.mentoring.service.MentorService;
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
public class MentorAcceptEmailServiceImpl<T> implements EmailService<T>{

    private final JavaMailSender javaMailSender;

    private final MentorService mentorService;

    private final MemberService memberService;

    @Override
    public MimeMessage createMessage(String to) {
        MimeMessage message = javaMailSender.createMimeMessage();
        Mentor mentor = mentorService.getMentor(to);
        Member mentorMemberObject = memberService.getMember(mentor.getEmail());
        String mentorImg = mentorMemberObject.getProfileImage().getFileUrl();
        if(mentorImg==null){
            mentorImg = ""; // 추후 basic 사진 기재
        }
        String msgg="<div\n" +
                "      style=\"\n" +
                "        border: solid 3px;\n" +
                "        border-color: lightgrey;\n" +
                "        width: fit-content;\n" +
                "        border-radius: 2%;\n" +
                "      \"\n" +
                "    >\n" +
                "      <table\n" +
                "        style=\"\n" +
                "          width: 100%;\n" +
                "          height: 100%;\n" +
                "          color: black;\n" +
                "          font-size: 1.2em;\n" +
                "          border-collapse: collapse;\n" +
                "        \"\n" +
                "        border=\"0\"\n" +
                "      >\n" +
                "        <tbody style=\"width: 100%; height: 100%\">\n" +
                "          <tr>\n" +
                "            <td>\n" +
                "              <center>\n" +
                "                <img\n" +
                "                  style=\"\n" +
                "                    width: 200px;\n" +
                "                    height: auto;\n" +
                "                    padding: 30px 20px 10px 0px;\n" +
                "                  \"\n" +
                "                  src=\"https://karrier.s3.ap-northeast-2.amazonaws.com/email-image/Karrier_logo_name.png\"\n" +
                "                  alt=\"\"\n" +
                "                />\n" +
                "              </center>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 30px 80px 20px 80px\">\n" +
                "              <center>\n" +
                "                <span\n" +
                "                  style=\"color: #2b8ecb; font-size: 1.2em; font-weight: bold\"\n" +
                "                  >캐리어 멘토</span\n" +
                "                >\n" +
                "                <span style=\"font-size: 1.2em; font-weight: bold\"\n" +
                "                  >가 된 것을 진심으로 환영합니다.</span\n" +
                "                >\n" +
                "              </center>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <center>\n" +
                "                <img\n" +
                "                  width=\"220em\"\n" +
                "                  height=\"220em\"\n" +
                "                  src=\""+mentorImg+"\"\n" +
                "                  alt=\"\"\n" +
                "                  style=\"\n" +
                "                    border-radius: 50%;\n" +
                "                    box-shadow: -2px -2px 8px 2px lightgrey;\n" +
                "                  \"\n" +
                "                />\n" +
                "              </center>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <center>\n" +
                "                <span\n" +
                "                  >진로에 고민이 많고, 학과 결정에 방황하는 학생들을 위해</span\n" +
                "                >\n" +
                "                <br />\n" +
                "                <br />\n" +
                "                <span\n" +
                "                  ><span style=\"font-weight: bold; font-size: 1.3em\"\n" +
                "                    >"+mentor.getName()+"</span\n" +
                "                  >\n" +
                "                  멘토님의 학과에 대한 지식과 경험을</span\n" +
                "                >\n" +
                "                <br />\n" +
                "                <br />\n" +
                "                <span>멘토링 프로그램을 통해 공유해주세요!</span>\n" +
                "                <br />\n" +
                "                <br />\n" +
                "              </center>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td>\n" +
                "              <center>\n" +
                "                <a\n" +
                "                  href=\"https://www.karrier.com\"\n" +
                "                  style=\"\n" +
                "                    background-color: #2b8ecb;\n" +
                "                    color: white;\n" +
                "                    border: none;\n" +
                "                    border-radius: 6%;\n" +
                "                    display: block;\n" +
                "                    width: 50%;\n" +
                "                    padding-top: 2%;\n" +
                "                    padding-bottom: 2%;\n" +
                "                    font-size: 0.9em;\n" +
                "                    font-weight: bold;\n" +
                "                    text-decoration: none;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  홈페이지로 이동\n" +
                "                </a>\n" +
                "              </center>\n" +
                "              <br />\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td\n" +
                "              style=\"\n" +
                "                border-radius: 2.5%;\n" +
                "                background-color: #2b8ecb;\n" +
                "                color: white;\n" +
                "                font-weight: bolder;\n" +
                "                padding: 7%;\n" +
                "                text-align: center;\n" +
                "              \"\n" +
                "            >\n" +
                "              대학생과 멘토링을 통해 진로고민을 해결해보세요.\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </div>";

        try {
            message.addRecipients(Message.RecipientType.TO, to);//보내는 대상
            message.setSubject("Karrier의 멘토가 되었음을 축하드립니다!");//제목
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
    public MimeMessage createMessage(String to, Program program) {
        return null;
    }

    @Override
    public void sendSimpleMessage(T memberObject){
        if(!(memberObject instanceof Member)) {
            throw new InternalServerException(ErrorCode.TYPE_CAST_ERROR);
        }
        Member member = (Member)memberObject;
        MimeMessage message = createMessage(member.getEmail());
        javaMailSender.send(message);
    }
}
