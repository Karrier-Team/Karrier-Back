package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MentorFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String gender;

    @NotBlank(message = "대학교는 필수 입력 값입니다.")
    private String university;

    @NotBlank(message = "단과대학은 필수 입력 값입니다.")
    private String college;
    
    @NotBlank(message = "학과는 필수 입력 값입니다.")
    private String department;
    
    private String major;

    @NotBlank(message = "학번은 필수 입력 값입니다.")
    private String studentId;

    @NotBlank(message = "학년은 필수 입력 값입니다.")
    private String year;

    @NotBlank(message = "멘토소개는 필수 입력 값입니다.")
    private String introduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String naverBlogAddress;

    private String facebookAddress;

    private String instarAddress;

    @NotBlank(message = "휴대전화번호는 필수 입력 값입니다.")
    private String phoneNo;

    @NotBlank(message = "주요활동 국가는 필수 입력 값입니다.")
    private String country;

    @NotBlank(message = "주요활동 도시는 필수 입력 값입니다.")
    private String city;

    private MultipartFile studentInfoFile;

    private MultipartFile profileImageFile;
}
