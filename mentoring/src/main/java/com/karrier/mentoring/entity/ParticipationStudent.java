package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ParticipationStudent")
@Getter
@Setter
public class ParticipationStudent {

    @Id
    private String program_no;

    private String email;

    private String name;

    private String gender;

    private String phone_no;

    private int age;

    private String region;

    private String schoolName;

    private String applicationRoute;

    private String introduce;

    private String question_category;

    private String question_content;

    private LocalDateTime applyDate;
}
