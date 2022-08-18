package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.AnswerKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Answer")
@Getter
@Setter
@IdClass(AnswerKey.class)
public class Answer implements Serializable {

    @Id
    private long programNo;

    @Id
    private long questionNo;

    @Id
    private long answerNo;

    private String answerEmail;

    private String answerContent;

    private String answerDate;
}