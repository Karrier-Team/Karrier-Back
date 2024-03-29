package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.QuestionLikeKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "QuestionLike")
@Getter
@Setter
@IdClass(QuestionLikeKey.class)
public class QuestionLike implements Serializable {

    @Id
    private long programNo;

    @Id
    private long questionNo;

    @Id
    private String email;

    @Column(nullable = false)
    private LocalDateTime likeDate;

    public static QuestionLike createQuestionLike(long programNo, long questionNo, String email) {

        QuestionLike questionLike = new QuestionLike();

        questionLike.setProgramNo(programNo);
        questionLike.setQuestionNo(questionNo);
        questionLike.setEmail(email);
        questionLike.setLikeDate(LocalDateTime.now());

        return questionLike;
    }
}
