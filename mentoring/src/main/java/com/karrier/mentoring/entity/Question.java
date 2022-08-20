package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.QuestionAnswerFormDto;
import com.karrier.mentoring.dto.QuestionFormDto;
import com.karrier.mentoring.dto.ReviewCommentFormDto;
import com.karrier.mentoring.dto.ReviewFormDto;
import com.karrier.mentoring.key.QuestionKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
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

    private String email;

    private String title;

    private String content;

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

    //기존 review에 댓글 정보 추가해서 반환
    public static Question createAnswer(QuestionAnswerFormDto questionAnswerFormDto, Question question) {

        question.setAnswer(questionAnswerFormDto.getAnswer());
        question.setAnswerDate(LocalDateTime.now());

        return question;
    }
}
