package com.karrier.mentoring.entity;

import com.karrier.mentoring.key.FollowKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Follow")
@Getter
@Setter
@IdClass(FollowKey.class)
public class Follow implements Serializable {

    @Id
    private String memberEmail;

    @Id
    private String mentorEmail;

    public static Follow createFollow(String memberEmail, String mentorEmail){
        Follow follow = new Follow();

        follow.setMemberEmail(memberEmail);
        follow.setMentorEmail(mentorEmail);

        return follow;
    }
}
