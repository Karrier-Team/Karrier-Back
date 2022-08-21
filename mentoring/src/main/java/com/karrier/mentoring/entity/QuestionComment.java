package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.QuestionCommentFormDto;
import com.karrier.mentoring.key.QuestionCommentKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "QuestionComment")
@Getter
@Setter
@IdClass(QuestionCommentKey.class)
public class QuestionComment implements Serializable {

    @Id
    private long programNo;

    @Id
    private long questionNo;

    @Id
    private long commentNo;

    private String email;

    private String content;

    private LocalDateTime commentDate;

    //questionCommentFormDto QuestionComment형태로 변환
    public static QuestionComment createComment(QuestionCommentFormDto questionCommentFormDto, String email) {

        QuestionComment questionComment = new QuestionComment();

        questionComment.setProgramNo(questionCommentFormDto.getProgramNo());
        questionComment.setQuestionNo(questionCommentFormDto.getQuestionNo());
        questionComment.setEmail(email);
        questionComment.setContent(questionCommentFormDto.getComment());
        questionComment.setCommentDate(LocalDateTime.now());

        return questionComment;
    }

    //questionCommentFormDto QuestionComment형태로 변환
    public static QuestionComment updateComment(QuestionCommentFormDto questionCommentFormDto, QuestionComment questionComment) {

        questionComment.setContent(questionCommentFormDto.getComment());
        questionComment.setCommentDate(LocalDateTime.now());

        return questionComment;
    }
}