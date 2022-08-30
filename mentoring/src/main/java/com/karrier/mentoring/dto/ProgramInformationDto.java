package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Curriculum;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.ParticipationStudent;
import com.karrier.mentoring.entity.Program;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProgramInformationDto {

    private String name;

    private String profileImage;

    private String naverBlogAddress;

    private String facebookAddress;

    private String instarAddress;

    private int likeCount;

    private String tag;

    private String introduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String mainImage;

    private String title;

    private String shortIntroduce;

    private List<Curriculum> curriculumList;

    private List<ParticipationStudent> participationStudentList;

    //private List<수강후기>
    //private List<질의응답>

    public static ProgramInformationDto createProgramInformationDto(Program program, Mentor mentor, String profileImage, List<Curriculum> curriculumList, List<ParticipationStudent> participationStudentList){

        ProgramInformationDto programInformationDto = new ProgramInformationDto();

        programInformationDto.setName(mentor.getName());
        programInformationDto.setProfileImage(profileImage);
        programInformationDto.setNaverBlogAddress(mentor.getNaverBlogAddress());
        programInformationDto.setFacebookAddress(mentor.getFacebookAddress());
        programInformationDto.setInstarAddress(mentor.getInstarAddress());
        programInformationDto.setLikeCount(program.getLikeCount());
        programInformationDto.setTag(program.getTag());
        programInformationDto.setIntroduce(mentor.getIntroduce());
        programInformationDto.setClub(mentor.getClub());
        programInformationDto.setContest(mentor.getContest());
        programInformationDto.setExternalActivity(mentor.getExternalActivity());
        programInformationDto.setIntern(mentor.getIntern());
        programInformationDto.setMainImage(program.getMainImage().getStoreFileName());
        programInformationDto.setTitle(program.getTitle());
        programInformationDto.setShortIntroduce(program.getShortIntroduce());
        programInformationDto.setCurriculumList(curriculumList);
        programInformationDto.setParticipationStudentList(participationStudentList);

        return programInformationDto;
    }
}
