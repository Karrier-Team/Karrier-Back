package com.karrier.mentoring.dto;

import javax.validation.constraints.NotBlank;

public class QuestionCommentFormDto {

    private long programNo;

    private long reviewNo;

    @NotBlank
    private String comment;

}
