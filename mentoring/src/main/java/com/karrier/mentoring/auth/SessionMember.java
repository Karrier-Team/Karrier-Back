package com.karrier.mentoring.auth;

import com.karrier.mentoring.entity.Member;
import lombok.Getter;

@Getter
public class SessionMember {
    private String nickname;
    private String email;

    public SessionMember(Member member){
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
