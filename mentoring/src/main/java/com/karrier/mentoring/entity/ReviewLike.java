package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.ReviewLikeKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ReviewLike")
@Getter
@Setter
@IdClass(ReviewLikeKey.class)
public class ReviewLike implements Serializable {

    @Id
    private long programNo;

    @Id
    private long reviewNo;

    @Id
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime likeDate;

    public static ReviewLike createReviewLike(long programNo, long reviewNo, String email) {

        ReviewLike reviewLike = new ReviewLike();

        reviewLike.setProgramNo(programNo);
        reviewLike.setReviewNo(reviewNo);
        reviewLike.setEmail(email);
        reviewLike.setLikeDate(LocalDateTime.now());

        return reviewLike;
    }
}
