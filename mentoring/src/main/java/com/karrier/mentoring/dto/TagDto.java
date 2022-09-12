package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TagDto {

    @NotBlank(message = "태그 이름은 필수 입력 값입니다.")
    private String tagName;
}
