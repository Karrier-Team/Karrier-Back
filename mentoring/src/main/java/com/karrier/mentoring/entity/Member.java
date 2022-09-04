package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.MemberFormDto;
import com.karrier.mentoring.dto.MemberManagePasswordDto;
import com.karrier.mentoring.dto.MemberPasswordDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
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
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String nickname;

    private boolean sleep;

    private boolean alarm;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createAccountDate;

    private LocalDateTime recentlyLoginDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="uploadFileName", column = @Column(name="PROFILE_IMAGE_UPLOAD_NAME")),
            @AttributeOverride(name="storeFileName", column = @Column(name="PROFILE_IMAGE_STORE_NAME")),
    })
    private UploadFile profileImage;

    @Builder
    public Member(String email, String nickname, Role role){
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.createAccountDate = LocalDateTime.now();
    }

    // by 이승민
    // 원래는 이런식으로 구현 하면 안됨.. 확장성 고려 안한 코드
    // 정석은 Enum 안에서 문자열 값도 함께 선언해야되는데 현재 수정 시, 코드 구조 변경 사항이 커질듯..
    public String getRoleKey(){
        if(this.role == Role.USER)
            return "USER";
        else if(this.role == Role.MENTOR_WAIT)
            return "MENTOR_WAIT";
        else if(this.role == Role.MENTOR_WAIT)
            return "MENTOR_WAIT";
        else if(this.role == Role.MENTOR_APPROVE)
            return "MENTOR_APPROVE";
        else
            return "ADMIN";
    }

    //회원가입시 member 생성
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        member.setNickname(memberFormDto.getNickname());
        String password = passwordEncoder.encode(memberFormDto.getPassword()); //비밀번호 암호화해서 저장하기 위해
        member.setPassword(password);
        member.setRole(Role.USER);  // USER or MENTOR_WAIT or MENTOR_APPROVE or ADMIN
        member.setCreateAccountDate(LocalDateTime.now());

        return member;
    }

    //로그인 날짜 업데이트
    public static Member updateRecentlyLoginDate(Member member) {

        member.setRecentlyLoginDate(LocalDateTime.now());

        return member;
    }

    //멘토 회원가입시 member 테이블 정보 변경되는 부분
    public static Member signUpMentor(Member member, UploadFile uploadFile) {

        member.setRole(Role.MENTOR_WAIT);
        member.setProfileImage(uploadFile);

        return member;
    }

    //프로필 변경시 닉네임과 프로필 사진 변경
    public static Member modifyProfile(Member member, UploadFile uploadFile, String nickname) {

        member.setNickname(nickname);
        member.setProfileImage(uploadFile);

        return member;
    }

    //비밀번호 변경시
    public static Member updatePassword(Member member, MemberPasswordDto MemberPasswordDto, PasswordEncoder passwordEncoder) {

        String password = passwordEncoder.encode(MemberPasswordDto.getNewPassword());
        member.setPassword(password);

        return member;
    }

    public static Member changeMentorRole(Member member){
        member.setRole(Role.MENTOR_APPROVE);

        return member;
    }

}
