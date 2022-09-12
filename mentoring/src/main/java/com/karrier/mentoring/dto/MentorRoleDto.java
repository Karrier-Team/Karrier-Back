package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MentorRoleDto {

    private String email;

    private String name;

    private String studentInfo;

    private LocalDateTime submitDate;

    public static MentorRoleDto createMentorRoleDto(Mentor mentor){

        MentorRoleDto mentorRoleDto = new MentorRoleDto();

        mentorRoleDto.setEmail(mentor.getEmail());
        mentorRoleDto.setName(mentor.getName());
        mentorRoleDto.setStudentInfo(mentor.getStudentInfo().getStoreFileName());
        mentorRoleDto.setSubmitDate(mentor.getSubmitDate());

        return mentorRoleDto;
    }
}
