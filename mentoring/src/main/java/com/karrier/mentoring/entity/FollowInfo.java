package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class FollowInfo implements Serializable{
    private String memberEmail;
    private String mentorEmail;
}
