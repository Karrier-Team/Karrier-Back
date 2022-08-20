package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.ReviewCommentFormDto;
import com.karrier.mentoring.dto.ReviewFormDto;
import com.karrier.mentoring.key.ReviewKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
@Getter
@Setter
@IdClass(ReviewKey.class)
public class Review implements Serializable {

    @Id
    private long programNo;

    @Id
    private long reviewNo;

    private String email;

    private String title;

    private String content;

    private LocalDateTime registerDate;

    private LocalDateTime commentDate;

    private long reviewLikeNo;

    private String comment;

    private float star;
    
    //reviewFormDto를 Review형태로 변환
    public static Review createReview(ReviewFormDto reviewFormDto, String email) {

        Review review = new Review();

        review.setProgramNo(reviewFormDto.getProgramNo());
        review.setEmail(email);
        review.setTitle(reviewFormDto.getTitle());
        review.setContent(reviewFormDto.getContent());
        review.setRegisterDate(LocalDateTime.now());
        review.setStar(reviewFormDto.getStar());

        return review;
    }

    //기존 review에 댓글 정보 추가해서 반환
    public static Review createComment(ReviewCommentFormDto reviewCommentFormDto, Review review) {

        review.setComment(reviewCommentFormDto.getComment());
        review.setCommentDate(LocalDateTime.now());

        return review;
    }
}
