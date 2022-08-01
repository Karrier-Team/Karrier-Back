package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    private LocalDateTime createAccountDate;

    private LocalDateTime recentlyLoginDate;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);  // USER or MENTOR or ADMIN
        member.setCreateAccountDate((LocalDateTime.now()));

        return member;
    }

    public static Member updateRecentlyLoginDate(Member member) {

        member.setRecentlyLoginDate(LocalDateTime.now());

        return member;
    }
}
