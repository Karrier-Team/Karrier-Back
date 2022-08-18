package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.QuestionCommentKey;
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
@IdClass(QuestionCommentKey.class)
public class QuestionComment implements Serializable {

    @Id
    private long programNo;

    @Id
    private long questionNo;

    @Id
    private long commentNo;

    private String commentEmail;

    private String commentContent;

    private String commentDate;
}