package com.karrier.mentoring.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCommentKey implements Serializable {

    private long programNo;

    private long questionNo;

    private long commentNo;
}
