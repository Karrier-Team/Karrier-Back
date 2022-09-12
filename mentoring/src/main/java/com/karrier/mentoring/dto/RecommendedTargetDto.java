package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RecommendedTargetDto {

    @NotBlank(message = "추천대상은 필수 입력 값입니다.")
    private String target;

}
