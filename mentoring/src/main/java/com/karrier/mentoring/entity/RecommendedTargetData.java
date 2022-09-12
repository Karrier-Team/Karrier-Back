package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.RecommendedTargetDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendedTargetData {
    private List<RecommendedTargetDto> recommendedTargetDtoList;

    public RecommendedTargetData(List<RecommendedTargetDto> recommendedTargetDtoList){
        this.recommendedTargetDtoList = recommendedTargetDtoList;
    }
}
