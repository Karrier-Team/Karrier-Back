package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.ParticipationStudentFormDto;
import com.karrier.mentoring.key.ParticipationStudentKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ParticipationStudent")
@Getter
@Setter
@IdClass(ParticipationStudentKey.class)
public class ParticipationStudent implements Serializable {

    @Id
    private long programNo;

    @Id
    private String email;

    private String name;

    private String gender;

    private String phoneNo;

    private String age;

    private String region;

    private String schoolName;

    private String applicationRoute;

    private String introduce;

    private String questionCategory;

    private String questionContent;

    private LocalDateTime applyDate;

    public static ParticipationStudent createParticipationStudent(ParticipationStudentFormDto participationStudentFormDto, String email, long programNo){
        ParticipationStudent participationStudent = new ParticipationStudent();

        participationStudent.setProgramNo(programNo);
        participationStudent.setEmail(email);
        participationStudent.setName(participationStudentFormDto.getName());
        participationStudent.setGender(participationStudentFormDto.getGender());
        participationStudent.setAge(participationStudentFormDto.getAge());
        participationStudent.setRegion(participationStudentFormDto.getRegion());
        participationStudent.setSchoolName(participationStudentFormDto.getSchoolName());
        participationStudent.setApplicationRoute(participationStudentFormDto.getApplicationRoute());
        participationStudent.setIntroduce(participationStudentFormDto.getIntroduce());
        participationStudent.setQuestionCategory(participationStudentFormDto.getQuestionCategory());
        participationStudent.setQuestionContent(participationStudentFormDto.getQuestionContent());
        participationStudent.setApplyDate(LocalDateTime.now());

        return participationStudent;

    }
}
