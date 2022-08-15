package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MentorManageDetailDto {

    @NotBlank(message = "멘토소개는 필수 입력 값입니다.")
    private String introduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String naverBlogAddress;

    private String facebookAddress;

    private String instarAddress;

    public static MentorManageDetailDto createMentorManageDetailDto(Mentor mentor) {

        MentorManageDetailDto mentorManageDetailDto = new MentorManageDetailDto();

        mentorManageDetailDto.setIntroduce(mentor.getIntroduce());
        mentorManageDetailDto.setClub(mentor.getClub());
        mentorManageDetailDto.setContest(mentor.getContest());
        mentorManageDetailDto.setExternalActivity(mentor.getExternalActivity());
        mentorManageDetailDto.setIntern(mentor.getIntern());
        mentorManageDetailDto.setNaverBlogAddress(mentor.getNaverBlogAddress());
        mentorManageDetailDto.setFacebookAddress(mentor.getFacebookAddress());
        mentorManageDetailDto.setInstarAddress(mentor.getInstarAddress());

        return mentorManageDetailDto;
    }
}
