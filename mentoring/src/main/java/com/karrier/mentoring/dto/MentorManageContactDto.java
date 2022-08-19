package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MentorManageContactDto {

    @NotBlank(message = "휴대전화번호는 필수 입력 값입니다.")
    private String phoneNo;

    @NotBlank(message = "주요활동 국가는 필수 입력 값입니다.")
    private String country;

    @NotBlank(message = "주요활동 도시는 필수 입력 값입니다.")
    private String city;

    //멘토 전체 정보에서 연락 정보만 보내기
    public static MentorManageContactDto createMentorManageContactDto(Mentor mentor) {

        MentorManageContactDto mentorManageContactDto = new MentorManageContactDto();

        mentorManageContactDto.setPhoneNo(mentor.getPhoneNo());
        mentorManageContactDto.setCountry(mentor.getCountry());
        mentorManageContactDto.setCity(mentor.getCity());

        return mentorManageContactDto;
    }
}
