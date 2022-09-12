package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowShowDto {

    private String name;

    private String major;

    private String profileImage;

    public static FollowShowDto createFollowShowDto(Mentor mentor, String profileImage){

        FollowShowDto followShowDto = new FollowShowDto();

        followShowDto.setName(mentor.getName());
        followShowDto.setMajor(mentor.getMajor());
        followShowDto.setProfileImage(profileImage);

        return  followShowDto;

    }
}
