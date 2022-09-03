package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.QuestionAnswerFormDto;
import com.karrier.mentoring.dto.QuestionFormDto;
import com.karrier.mentoring.dto.ReviewCommentFormDto;
import com.karrier.mentoring.dto.ReviewFormDto;
import com.karrier.mentoring.key.QuestionKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Question")
@Getter
@Setter
@IdClass(QuestionKey.class)
public class Question implements Serializable {

    @Id
    private long programNo;

    @Id
    private long questionNo;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime modifyDate;

    private long questionLikeNo;

    private long answerLikeNo;

    private String answer;

    private LocalDateTime answerDate;

    private boolean solve;

    //questionFormDto를 Question형태로 변환
    public static Question createQuestion(QuestionFormDto questionFormDto, String email) {

        Question question = new Question();

        question.setProgramNo(questionFormDto.getProgramNo());
        question.setEmail(email);
        question.setTitle(questionFormDto.getTitle());
        question.setContent(questionFormDto.getContent());
        question.setModifyDate(LocalDateTime.now());

        return question;
    }

    //질문 수정시
    public static Question updateQuestion(QuestionFormDto questionFormDto, Question question) {

        question.setTitle(questionFormDto.getTitle());
        question.setContent(questionFormDto.getContent());
        question.setModifyDate(LocalDateTime.now());

        return question;
    }


    //답변 추가 && 답변 수정시
    public static Question createAnswer(QuestionAnswerFormDto questionAnswerFormDto, Question question) {

        question.setAnswer(questionAnswerFormDto.getAnswer());
        question.setAnswerDate(LocalDateTime.now());

        return question;
    }
}
