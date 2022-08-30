package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.MemberFormDto;
import com.karrier.mentoring.dto.MemberManagePasswordDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    private String nickname;

    private boolean activate;

    private boolean alarm;

    @Column(updatable = false)
    private LocalDateTime createAccountDate;

    private LocalDateTime recentlyLoginDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="uploadFileName", column = @Column(name="PROFILE_IMAGE_UPLOAD_NAME")),
            @AttributeOverride(name="storeFileName", column = @Column(name="PROFILE_IMAGE_STORE_NAME")),
    })
    private UploadFile profileImage;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);  // USER or MENTOR or ADMIN
        member.setCreateAccountDate(LocalDateTime.now());

        return member;
    }

    public static Member updateRecentlyLoginDate(Member member) {

        member.setRecentlyLoginDate(LocalDateTime.now());

        return member;
    }

    public static Member signUpMentor(Member member, UploadFile uploadFile) {

        member.setRole(Role.MENTOR_WAIT);
        member.setProfileImage(uploadFile);

        return member;
    }

    public static Member modifyProfile(Member member, UploadFile uploadFile, String nickname) {

        member.setNickname(nickname);
        member.setProfileImage(uploadFile);

        return member;
    }

    public static Member updatePassword(Member member, MemberManagePasswordDto memberManagePasswordDto, PasswordEncoder passwordEncoder) {

        String password = passwordEncoder.encode(memberManagePasswordDto.getNewPassword());
        member.setPassword(password);

        return member;
    }

    public static Member changeMentorRole(Member member){
        member.setRole(Role.MENTOR_APPROVE);

        return member;
    }
}
