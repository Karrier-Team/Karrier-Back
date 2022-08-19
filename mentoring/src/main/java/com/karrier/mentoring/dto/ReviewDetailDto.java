package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDetailDto {

    private long programNo;

    private String programName;

    private long reviewNo;

    private String writerNickname;

    private String mentorName;

    private String title;

    private String content;

    private String comment;

    private LocalDateTime registerDate;

    private LocalDateTime commentDate;

    private long reviewLikeNo;

    private float star;

    public static ReviewDetailDto createReviewDetailDto(Review review, String programName, String writerNickname, String mentorName) {

        ReviewDetailDto reviewDetailDto = new ReviewDetailDto();

        reviewDetailDto.setProgramName(programName);
        reviewDetailDto.setProgramNo(review.getProgramNo());
        reviewDetailDto.setReviewNo(review.getReviewNo());
        reviewDetailDto.setWriterNickname(writerNickname);
        reviewDetailDto.setMentorName(mentorName);
        reviewDetailDto.setTitle(review.getTitle());
        reviewDetailDto.setContent(review.getContent());
        reviewDetailDto.setComment(review.getComment());
        reviewDetailDto.setReviewLikeNo(review.getReviewLikeNo());
        reviewDetailDto.setRegisterDate(review.getRegisterDate());
        reviewDetailDto.setCommentDate(review.getCommentDate());
        reviewDetailDto.setStar(review.getStar());

        return reviewDetailDto;
    }
}
