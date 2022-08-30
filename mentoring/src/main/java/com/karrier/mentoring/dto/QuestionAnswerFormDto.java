package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QuestionAnswerFormDto {

    private long programNo;

    private long questionNo;

    @NotBlank
    private String answer;
}
