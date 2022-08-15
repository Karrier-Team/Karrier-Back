package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.MentorFormDto;
import com.karrier.mentoring.dto.MentorManageBasicDto;
import com.karrier.mentoring.dto.MentorManageContactDto;
import com.karrier.mentoring.dto.MentorManageDetailDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Mentor")
@Getter
@Setter
@ToString
public class Mentor {

    @Id
    private String email;

    private String name;

    private String gender;

    private String university;

    private String college;

    private String major;

    private String studentId;

    private int year;

    private String introduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String naverBlogAddress;

    private String facebookAddress;

    private String instarAddress;

    private String phoneNo;

    private String country;

    private String city;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="uploadFileName", column = @Column(name="STUDENT_INFO_UPLOAD_NAME")),
            @AttributeOverride(name="storeFileName", column = @Column(name="STUDENT_INFO_STORE_NAME")),
    })
    private UploadFile studentInfo;

    private String answerPercent;

    private String answerNo;

    private boolean alarm;

    private LocalDateTime submitDate;

    public static Mentor createMentor(MentorFormDto mentorFormDto, UploadFile studentInfo, String email) {

        Mentor mentor = new Mentor();

        mentor.setEmail(email);
        mentor.setName(mentorFormDto.getName());
        mentor.setGender(mentorFormDto.getGender());
        mentor.setUniversity(mentorFormDto.getUniversity());
        mentor.setCollege(mentorFormDto.getCollege());
        mentor.setMajor(mentorFormDto.getMajor());
        mentor.setStudentId(mentorFormDto.getStudentId());
        mentor.setYear(Integer.parseInt(mentorFormDto.getYear()));
        mentor.setIntroduce(mentorFormDto.getIntroduce());
        mentor.setClub(mentorFormDto.getClub());
        mentor.setContest(mentorFormDto.getContest());
        mentor.setExternalActivity(mentorFormDto.getExternalActivity());
        mentor.setIntern(mentorFormDto.getIntern());
        mentor.setNaverBlogAddress(mentorFormDto.getNaverBlogAddress());
        mentor.setFacebookAddress(mentorFormDto.getFacebookAddress());
        mentor.setInstarAddress(mentorFormDto.getInstarAddress());
        mentor.setPhoneNo(mentorFormDto.getPhoneNo());
        mentor.setCountry(mentorFormDto.getCountry());
        mentor.setCity(mentorFormDto.getCity());
        mentor.setStudentInfo(studentInfo);
        mentor.setSubmitDate(LocalDateTime.now());

        return mentor;
    }

    public static Mentor updateMentorBasic(Mentor mentor, MentorManageBasicDto mentorManageBasicDto) {

        mentor.setName(mentorManageBasicDto.getName());
        mentor.setGender(mentorManageBasicDto.getGender());
        mentor.setUniversity(mentorManageBasicDto.getUniversity());
        mentor.setCollege(mentorManageBasicDto.getCollege());
        mentor.setMajor(mentorManageBasicDto.getMajor());
        mentor.setStudentId(mentorManageBasicDto.getStudentId());
        mentor.setYear(Integer.parseInt(mentorManageBasicDto.getYear()));

        return mentor;
    }

    public static Mentor updateMentorDetail(Mentor mentor, MentorManageDetailDto mentorManageDetailDto) {

        mentor.setIntroduce(mentorManageDetailDto.getIntroduce());
        mentor.setClub(mentorManageDetailDto.getClub());
        mentor.setContest(mentorManageDetailDto.getContest());
        mentor.setExternalActivity(mentorManageDetailDto.getExternalActivity());
        mentor.setIntern(mentorManageDetailDto.getIntern());
        mentor.setNaverBlogAddress(mentorManageDetailDto.getNaverBlogAddress());
        mentor.setFacebookAddress(mentorManageDetailDto.getFacebookAddress());
        mentor.setInstarAddress(mentorManageDetailDto.getInstarAddress());

        return mentor;
    }

    public static Mentor updateMentorContact(Mentor mentor, MentorManageContactDto mentorManageContactDto) {

        mentor.setPhoneNo(mentorManageContactDto.getPhoneNo());
        mentor.setCountry(mentorManageContactDto.getCountry());
        mentor.setCity(mentorManageContactDto.getCity());

        return mentor;
    }
}
