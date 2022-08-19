package com.karrier.mentoring.dto;

import javax.validation.constraints.NotBlank;

public class QuestionFormDto {

    private long programNo;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
