package com.karrier.mentoring.dto;

import javax.validation.constraints.NotBlank;

public class QuestionAnswerDto {

    private long programNo;

    private long questionNo;

    @NotBlank
    private String answer;
}
