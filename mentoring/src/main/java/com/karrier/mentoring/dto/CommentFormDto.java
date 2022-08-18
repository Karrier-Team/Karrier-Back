package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentFormDto {

    @NotBlank
    private long programNo;

    @NotBlank
    private long reviewNo;

    @NotBlank
    private String comment;
}
