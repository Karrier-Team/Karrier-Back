package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.QuestionKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

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

    private String modifyDate;

    private long likeNo;

    private boolean solve;
}
