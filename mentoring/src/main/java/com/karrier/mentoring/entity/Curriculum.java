package com.karrier.mentoring.entity;


import com.karrier.mentoring.dto.CurriculumDto;
import com.karrier.mentoring.key.CurriculumKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
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

    private String curriculumContent;

    public static Curriculum createCurriculum(long programNo, CurriculumDto curriculumDto){
        Curriculum curriculum = new Curriculum();

        curriculum.setProgramNo(programNo);
        curriculum.setCurriculumTitle(curriculumDto.getCurriculumTitle());
        curriculum.setCurriculumContent(curriculumDto.getCurriculumContent());

        return curriculum;
    }
}
