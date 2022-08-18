package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.ReviewKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

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

    private String email;

    private String title;

    private String content;

    private String registerDate;

    private long likeNo;

    private String comment;

    private float star;
}
