package com.karrier.mentoring.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberPasswordDto {
    @NotBlank
    String newPassword;

    @NotBlank
    String passwordCheck;
}
