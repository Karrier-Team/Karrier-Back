package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ParticipationStudentFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String gender;

    @NotBlank(message = "연락처는 필수 입력 값입니다.")
    private String phoneNo;

    @NotBlank(message = "나이는 필수 입력 값입니다.")
    private String age;

    @NotBlank(message = "지역 필수 입력 값입니다.")
    private String region;

    @NotBlank(message = "학교는 필수 입력 값입니다.")
    private String schoolName;

    @NotBlank(message = "신청 경로는 필수 입력 값입니다.")
    private String applicationRoute;

    @NotBlank(message = "자기소개서는 필수 입력 값입니다.")
    private String introduce;

    private String questionCategory;

    private String questionContent;

}
