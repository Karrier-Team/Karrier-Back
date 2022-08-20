package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.QuestionKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "AnswerLike")
@Getter
@Setter
@IdClass(QuestionKey.class)
public class AnswerLike implements Serializable {

    @Id
    private long programNo;

    @Id
    private long questionNo;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime likeDate;

    public static AnswerLike createReviewLike(long programNo, long questionNo, String email) {

        AnswerLike answerLike = new AnswerLike();

        answerLike.setProgramNo(programNo);
        answerLike.setQuestionNo(questionNo);
        answerLike.setEmail(email);
        answerLike.setLikeDate(LocalDateTime.now());

        return answerLike;
    }
}
