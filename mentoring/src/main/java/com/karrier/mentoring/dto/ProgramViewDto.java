package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Program;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramViewDto {

    private long programNo;

    private String name;

    private String title;

    private String profileImage;

    private String mainImage;

    private String major;

    private int likeCount;

    private Boolean onlineOffline;

    private String state;

    private String department;

    public static ProgramViewDto createProgramViewDto(Program program, String name, String profileImage, String major, String department){

        ProgramViewDto programViewDto = new ProgramViewDto();

        programViewDto.setProgramNo(program.getProgramNo());
        programViewDto.setName(name);
        programViewDto.setTitle(program.getTitle());
        programViewDto.setProfileImage(profileImage);
        programViewDto.setMainImage(program.getMainImage().getFileUrl());
        programViewDto.setMajor(major);
        programViewDto.setDepartment(department);
        programViewDto.setLikeCount(program.getLikeCount());
        programViewDto.setOnlineOffline(program.getOnlineOffline());
        programViewDto.setState(program.getState());

        return programViewDto;

    }
}
