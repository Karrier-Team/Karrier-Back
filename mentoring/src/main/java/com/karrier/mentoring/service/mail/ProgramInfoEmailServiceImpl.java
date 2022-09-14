package com.karrier.mentoring.service.mail;

import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.ParticipationStudent;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.InternalServerException;
import com.karrier.mentoring.http.error.exception.NotFoundException;
import com.karrier.mentoring.http.error.exception.UnsupportedMediaTypeException;
import com.karrier.mentoring.service.MentorService;
import com.karrier.mentoring.service.ProgramService;
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
public class ProgramInfoEmailServiceImpl<T> implements EmailService<T>{

    private final JavaMailSender javaMailSender;

    private final MentorService mentorService;

    private final ProgramService programService;

    @Override
    public MimeMessage createMessage(String to) {
        return null;
    }

    @Override
    public MimeMessage createMessage(String to, Program program) {
        MimeMessage message = javaMailSender.createMimeMessage();
        Mentor mentor = mentorService.getMentor(program.getEmail());
        if(mentor==null)
            throw new NotFoundException(ErrorCode.MENTOR_NOT_FOUND);
        String onOffStr = program.getOnlineOffline() ? "온라인" : "오프라인";
        String subStr = "멘토링 프로그램 신청이 완료되었습니다.";
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
                "            <td style=\"padding: 30px 20px 20px 20px\">\n" +
                "              <span style=\"color: #0098d3; font-size: 1.2em; font-weight: bold\"\n" +
                "                >" +program.getTitle() +"</span\n" +
                "              >\n" +
                "              <span>의 신청이 완료되었습니다.</span>\n" +
                "              <br />\n" +
                "              <span\n" +
                "                >해당 프로그램은 마이페이지 - 수강목록에서 확인하실 수\n" +
                "                있습니다.</span\n" +
                "              >\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <span style=\"font-weight: bold; font-size: 1.1em\">제목 : </span>\n" +
                "              <span>"+ program.getTitle() +"</span>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <span style=\"font-weight: bold; font-size: 1.1em\">멘토 : </span>\n" +
                "              <span>"+mentor.getName()+"</span>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <span style=\"font-weight: bold; font-size: 1.1em\">유형 : </span>\n" +
                "              <span>"+ program.getType()+"</span>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <span style=\"font-weight: bold; font-size: 1.1em\"\n" +
                "                >진행기간 :\n" +
                "              </span>\n" +
                "              <span>"+ program.getOpenDate() +" ~ "+ program.getCloseDate() + "</span>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <span style=\"font-weight: bold; font-size: 1.1em\"\n" +
                "                >진행시간 :\n" +
                "              </span>\n" +
                "              <span>" + program.getRunningTime()+ "</span>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 10px 20px\">\n" +
                "              <span style=\"font-weight: bold; font-size: 1.1em\"\n" +
                "                >온/오프라인 :\n" +
                "              </span>\n" +
                "              <span>"+onOffStr+"</span>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td style=\"padding: 10px 20px 30px 20px\">\n" +
                "              <span style=\"color: #2b8ecb; font-size: 1.4em; font-weight: 900\"\n" +
                "                >WEB :\n" +
                "              </span>\n" +
                "              <a\n" +
                "                style=\"\n" +
                "                  color: black;\n" +
                "                  font-size: 0.9em;\n" +
                "                  font-weight: bolder;\n" +
                "                  text-decoration: none;\n" +
                "                \"\n" +
                "                href=\"https://www.karrier.com\"\n" +
                "                >HTTPS://WWW.KARRIER.COM</a\n" +
                "              >\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td\n" +
                "              style=\"\n" +
                "                border-radius: 2%;\n" +
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

    @Override
    public void sendSimpleMessage(T student) {
        if(!(student instanceof ParticipationStudent)) {
            throw new InternalServerException(ErrorCode.TYPE_CAST_ERROR);
        }
        ParticipationStudent participationStudent = (ParticipationStudent)student;
        Program program = programService.getProgramByNo(participationStudent.getProgramNo());
        if(program==null)
            throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
        MimeMessage message = createMessage(participationStudent.getEmail(),program);
        javaMailSender.send(message);
    }
}
