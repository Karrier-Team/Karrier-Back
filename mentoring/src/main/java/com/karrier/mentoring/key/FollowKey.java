package com.karrier.mentoring.key;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowKey implements Serializable{
    private String memberEmail;
    private String mentorEmail;
}
