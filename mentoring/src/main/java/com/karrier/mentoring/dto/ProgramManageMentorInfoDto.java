package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class ProgramManageMentorInfoDto {
    @NotBlank(message = "멘토소개는 필수 입력 값입니다.")
    private String mentorIntroduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String naverBlogAddress;

    private String facebookAddress;

    private String instarAddress;


    public static ProgramManageMentorInfoDto createProgramManageMentorInfoDto(Mentor mentor){
        ProgramManageMentorInfoDto programManageMentorInfoDto = new ProgramManageMentorInfoDto();

        programManageMentorInfoDto.setMentorIntroduce(mentor.getIntroduce());
        programManageMentorInfoDto.setClub(mentor.getClub());
        programManageMentorInfoDto.setContest(mentor.getContest());
        programManageMentorInfoDto.setExternalActivity(mentor.getExternalActivity());
        programManageMentorInfoDto.setIntern(mentor.getIntern());
        programManageMentorInfoDto.setNaverBlogAddress(mentor.getNaverBlogAddress());
        programManageMentorInfoDto.setFacebookAddress(mentor.getFacebookAddress());
        programManageMentorInfoDto.setInstarAddress(mentor.getInstarAddress());

        return programManageMentorInfoDto;
    }
}
