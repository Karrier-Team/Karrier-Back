package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.ReviewCommentFormDto;
import com.karrier.mentoring.dto.ReviewFormDto;
import com.karrier.mentoring.key.ReviewKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
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
