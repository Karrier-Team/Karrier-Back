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

    @NotBlank(message = "학과는 필수 입력 값입니다.")
    private String department;
    
    private String major;

    @NotBlank(message = "학번은 필수 입력 값입니다.")
    private String studentId;

    @NotBlank(message = "학년은 필수 입력 값입니다.")
    private String year;

    //멘토 전체 정보에서 기본 정보 부분만 보내기
    public static MentorManageBasicDto createMentorManageBasicDto(Mentor mentor) {

        MentorManageBasicDto mentorManageBasicDto = new MentorManageBasicDto();

        mentorManageBasicDto.setName(mentor.getName());
        mentorManageBasicDto.setGender(mentor.getGender());
        mentorManageBasicDto.setUniversity(mentor.getUniversity());
        mentorManageBasicDto.setCollege(mentor.getCollege());
        mentorManageBasicDto.setDepartment(mentor.getDepartment());
        mentorManageBasicDto.setMajor(mentor.getMajor());
        mentorManageBasicDto.setStudentId(mentor.getStudentId());
        mentorManageBasicDto.setYear(String.valueOf(mentor.getYear()));

        return mentorManageBasicDto;
    }
}
