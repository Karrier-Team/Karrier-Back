package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Program;
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

    private boolean writer;

    private boolean mentor;

    public static ReviewDetailDto createReviewDetailDto(Review review, Program program, String writerNickname, String mentorName, String myEmail) {

        ReviewDetailDto reviewDetailDto = new ReviewDetailDto();

        reviewDetailDto.setProgramName(program.getTitle());
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
        if (review.getEmail().equals(myEmail)) {//리뷰 삭제 가능 여부
            reviewDetailDto.setWriter(true);
        }
        if (program.getEmail().equals(myEmail)) {//댓글 삭제 가능 여부 && 댓글 작성 가능 여부 판단 가능
            reviewDetailDto.setMentor(true);
        }

        return reviewDetailDto;
    }
}
