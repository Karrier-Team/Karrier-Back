package com.karrier.mentoring.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CurriculumDto {

    @NotBlank(message = "커리큘럼 제목은 필수 입력 값입니다.")
    private String curriculumTitle;

    @NotBlank(message = "커리큘럼 내용은 필수 입력 값입니다.")
    private String curriculumContent;
}
