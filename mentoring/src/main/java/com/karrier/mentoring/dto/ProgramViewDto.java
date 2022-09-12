package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Program;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramViewDto {

    private String name;

    private String title;

    private String profileImage;

    private String mainImage;

    private String major;

    private int likeCount;

    private Boolean onlineOffline;

    public static ProgramViewDto createProgramViewDto(Program program, String name, String profileImage, String major){

        ProgramViewDto programViewDto = new ProgramViewDto();

        programViewDto.setName(name);
        programViewDto.setTitle(program.getTitle());
        programViewDto.setProfileImage(profileImage);
        programViewDto.setMainImage(program.getMainImage().getStoreFileName());
        programViewDto.setMajor(major);
        programViewDto.setLikeCount(program.getLikeCount());
        programViewDto.setOnlineOffline(program.getOnlineOffline());

        return programViewDto;

    }
}
