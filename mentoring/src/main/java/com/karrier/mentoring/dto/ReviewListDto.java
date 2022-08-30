package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Review;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewListDto {

    private long programNo;

    private String programName;

    private long reviewNo;

    private String nickname;

    private String title;

    private String content;

    private boolean comment;

    private LocalDateTime registerDate;

    private long reviewLikeNo;

    public static ReviewListDto createReviewListDto(Review review, String programName, String nickname) {

        ReviewListDto reviewListDto = new ReviewListDto();

        reviewListDto.setProgramName(programName);
        reviewListDto.setProgramNo(review.getProgramNo());
        reviewListDto.setReviewNo(review.getReviewNo());
        reviewListDto.setNickname(nickname);
        reviewListDto.setTitle(review.getTitle());
        reviewListDto.setContent(review.getContent());
        reviewListDto.setReviewLikeNo(review.getReviewLikeNo());
        reviewListDto.setRegisterDate(review.getRegisterDate());
        if (review.getCommentDate() != null) { //수강후기 답변 여부 반환
            reviewListDto.setComment(true);
        }

        return reviewListDto;
    }
}
