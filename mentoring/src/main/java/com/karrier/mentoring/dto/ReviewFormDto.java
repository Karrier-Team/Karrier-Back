package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReviewFormDto {

    private long programNo;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private float star;
}
