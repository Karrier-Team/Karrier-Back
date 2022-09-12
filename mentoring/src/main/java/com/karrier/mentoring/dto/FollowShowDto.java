package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowShowDto {

    private String email;

    private String name;

    private String major;

    private String profileImage;

    public static FollowShowDto createFollowShowDto(Mentor mentor, String profileImage){

        FollowShowDto followShowDto = new FollowShowDto();

        followShowDto.setEmail(mentor.getEmail());
        followShowDto.setName(mentor.getName());
        followShowDto.setMajor(mentor.getMajor());
        followShowDto.setProfileImage(profileImage);

        return  followShowDto;

    }

    public static FollowShowDto createFollowerShowDto(Member member, String profileImage){

        FollowShowDto followShowDto = new FollowShowDto();

        followShowDto.setEmail(member.getEmail());
        followShowDto.setName(member.getNickname());
        followShowDto.setProfileImage(profileImage);

        return followShowDto;
    }
}
