package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.RecommendedTargetDto;
import com.karrier.mentoring.key.RecommendedTargetKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "RecommendedTarget")
@Getter
@Setter
@IdClass(RecommendedTargetKey.class)
public class RecommendedTarget implements Serializable {

    @Id
    private long programNo;

    @Id
    private String target;

    public static RecommendedTarget createRecommendedTarget(long programNo, RecommendedTargetDto recommendedTargetDto){
        RecommendedTarget recommendedTarget = new RecommendedTarget();

        recommendedTarget.setProgramNo(programNo);
        recommendedTarget.setTarget(recommendedTargetDto.getTarget());

        return recommendedTarget;
    }
}
