package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberManagePasswordDto extends MemberPasswordDto {
    @NotBlank
    String oldPassword;
}
