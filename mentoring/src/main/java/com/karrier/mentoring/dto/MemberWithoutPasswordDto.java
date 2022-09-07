package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Role;
import com.karrier.mentoring.entity.UploadFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
public class MemberWithoutPasswordDto {

    private String email;

    private String role;

    private String nickname;

    private boolean sleep;

    private boolean alarm;

    private LocalDateTime createAccountDate;

    private LocalDateTime recentlyLoginDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="uploadFileName", column = @Column(name="PROFILE_IMAGE_UPLOAD_NAME")),
            @AttributeOverride(name="storeFileName", column = @Column(name="PROFILE_IMAGE_STORE_NAME")),
    })
    private UploadFile profileImage;

    //비밀번호를 제외한 member 생성
    public static MemberWithoutPasswordDto createMemberWithoutPasswordDto(Member member) {

        MemberWithoutPasswordDto memberWithoutPasswordDto = new MemberWithoutPasswordDto();

        memberWithoutPasswordDto.setEmail(member.getEmail());
        memberWithoutPasswordDto.setRole(member.getRole().toString());
        memberWithoutPasswordDto.setNickname(member.getNickname());
        memberWithoutPasswordDto.setSleep(member.isSleep());
        memberWithoutPasswordDto.setAlarm(member.isAlarm());
        memberWithoutPasswordDto.setCreateAccountDate(member.getCreateAccountDate());
        memberWithoutPasswordDto.setRecentlyLoginDate(member.getRecentlyLoginDate());
        memberWithoutPasswordDto.setProfileImage(member.getProfileImage());

        return memberWithoutPasswordDto;
    }
}
