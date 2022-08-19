package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MentorManageBasicDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String gender;

    @NotBlank(message = "대학교는 필수 입력 값입니다.")
    private String university;

    @NotBlank(message = "단과대학은 필수 입력 값입니다.")
    private String college;

    @NotBlank(message = "전공은 필수 입력 값입니다.")
    private String major;

    @NotBlank(message = "학번은 필수 입력 값입니다.")
    private String studentId;

    @NotBlank(message = "학년은 필수 입력 값입니다.")
    private String year;

    public static MentorManageBasicDto createMentorManageBasicDto(Mentor mentor) {

        MentorManageBasicDto mentorManageBasicDto = new MentorManageBasicDto();

        mentorManageBasicDto.setName(mentor.getName());
        mentorManageBasicDto.setGender(mentor.getGender());
        mentorManageBasicDto.setUniversity(mentor.getUniversity());
        mentorManageBasicDto.setCollege(mentor.getCollege());
        mentorManageBasicDto.setMajor(mentor.getMajor());
        mentorManageBasicDto.setStudentId(mentor.getStudentId());
        mentorManageBasicDto.setYear(String.valueOf(mentor.getYear()));

        return mentorManageBasicDto;
    }
}
