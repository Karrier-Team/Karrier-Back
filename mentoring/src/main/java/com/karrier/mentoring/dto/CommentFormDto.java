package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentFormDto {

    private long programNo;

    private long reviewNo;

    @NotBlank
    private String comment;
}
