package com.karrier.mentoring.dto;

import javax.validation.constraints.NotBlank;

public class QuestionAnswerDto {

    @NotBlank
    private long programNo;

    @NotBlank
    private long questionNo;

    @NotBlank
    private String answer;
}
