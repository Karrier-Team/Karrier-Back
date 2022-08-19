package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class PartInfo implements Serializable {
    private Long programNo;
    private String email;
}
