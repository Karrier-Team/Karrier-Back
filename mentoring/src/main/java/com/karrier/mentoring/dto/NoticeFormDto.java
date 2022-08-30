package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Review;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Getter
@Setter
public class NoticeFormDto {

    @Id
    private long noticeNo;

    @NotBlank
    private String title;

    @NotBlank
    private String content;


}
