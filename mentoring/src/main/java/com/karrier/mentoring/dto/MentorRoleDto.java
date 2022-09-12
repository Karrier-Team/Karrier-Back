package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class MentorRoleDto {

    private String email;

    private String name;

    private String studentInfo;

    private LocalDateTime submitDate;

    private String gender;

    private String college;

    private String department;

    private String major;

    private String studentId;

    private String year;

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

    private String profileImage;

    public static MentorRoleDto createMentorRoleDto(Mentor mentor, Member member){

        MentorRoleDto mentorRoleDto = new MentorRoleDto();

        mentorRoleDto.setEmail(mentor.getEmail());
        mentorRoleDto.setName(mentor.getName());
        mentorRoleDto.setStudentInfo(mentor.getStudentInfo().getStoreFileName());
        mentorRoleDto.setSubmitDate(mentor.getSubmitDate());
        mentorRoleDto.setName(mentor.getName());
        mentorRoleDto.setGender(mentor.getGender());
        mentorRoleDto.setCollege(mentor.getCollege());
        mentorRoleDto.setDepartment(mentor.getDepartment());
        mentorRoleDto.setMajor(mentor.getMajor());
        mentorRoleDto.setStudentId(mentor.getStudentId());
        mentorRoleDto.setYear(String.valueOf(mentor.getYear()));
        mentorRoleDto.setIntroduce(mentor.getIntroduce());
        mentorRoleDto.setClub(mentor.getClub());
        mentorRoleDto.setContest(mentor.getContest());
        mentorRoleDto.setExternalActivity(mentor.getExternalActivity());
        mentorRoleDto.setIntern(mentor.getIntern());
        mentorRoleDto.setNaverBlogAddress(mentor.getNaverBlogAddress());
        mentorRoleDto.setFacebookAddress(mentor.getFacebookAddress());
        mentorRoleDto.setInstarAddress(mentor.getInstarAddress());
        mentorRoleDto.setPhoneNo(mentor.getPhoneNo());
        mentorRoleDto.setCountry(mentor.getCountry());
        mentorRoleDto.setCity(mentor.getCity());
        mentorRoleDto.setProfileImage(member.getProfileImage().getStoreFileName());


        return mentorRoleDto;
    }
}