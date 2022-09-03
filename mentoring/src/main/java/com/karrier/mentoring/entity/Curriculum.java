package com.karrier.mentoring.entity;


import com.karrier.mentoring.key.CurriculumKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Curriculum")
@Getter
@Setter
@IdClass(CurriculumKey.class)
public class Curriculum implements Serializable {

    @Id
    private long programNo;

    @Id
    private String curriculumTitle;

    @Column(nullable = false)
    private String content;

    public static Curriculum createCurriculum(long programNo, String curriculumTitle, String content){
        Curriculum curriculum = new Curriculum();

        curriculum.setProgramNo(programNo);
        curriculum.setCurriculumTitle(curriculumTitle);
        curriculum.setContent(content);

        return curriculum;
    }
}
