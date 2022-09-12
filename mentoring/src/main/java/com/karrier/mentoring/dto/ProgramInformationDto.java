package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;
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

    private String introduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String mainImage;

    private String title;

    private String mentorIntroduce;

    private String state;

    private Boolean isMyWishList;

    private Boolean isMyFollowList;

    private Boolean isMyParticipate;

    private List<Curriculum> curriculumList;

    private List<RecommendedTarget> recommendedTargetList;

    private List<Tag> tagList;

    private List<ParticipationStudent> participationStudentList;

    //private List<수강후기>
    //private List<질의응답>

    public static ProgramInformationDto createProgramInformationDto(Program program, Mentor mentor, String profileImage, List<Curriculum> curriculumList, List<RecommendedTarget> recommendedTargetList, List<Tag> tagList, List<ParticipationStudent> participationStudentList, Boolean isMyWishList, Boolean isMyFollowList,  Boolean isMyParticipate){

        ProgramInformationDto programInformationDto = new ProgramInformationDto();

        programInformationDto.setName(mentor.getName());
        programInformationDto.setProfileImage(profileImage);
        programInformationDto.setNaverBlogAddress(mentor.getNaverBlogAddress());
        programInformationDto.setFacebookAddress(mentor.getFacebookAddress());
        programInformationDto.setInstarAddress(mentor.getInstarAddress());
        programInformationDto.setLikeCount(program.getLikeCount());
        programInformationDto.setMentorIntroduce(mentor.getIntroduce());
        programInformationDto.setClub(mentor.getClub());
        programInformationDto.setContest(mentor.getContest());
        programInformationDto.setExternalActivity(mentor.getExternalActivity());
        programInformationDto.setIntern(mentor.getIntern());
        programInformationDto.setMainImage(program.getMainImage().getStoreFileName());
        programInformationDto.setTitle(program.getTitle());
        programInformationDto.setIntroduce(program.getIntroduce());
        programInformationDto.setState(program.getState());
        programInformationDto.setCurriculumList(curriculumList);
        programInformationDto.setRecommendedTargetList(recommendedTargetList);
        programInformationDto.setTagList(tagList);
        programInformationDto.setParticipationStudentList(participationStudentList);
        programInformationDto.setIsMyWishList(isMyWishList);
        programInformationDto.setIsMyFollowList(isMyFollowList);
        programInformationDto.setIsMyParticipate(isMyParticipate);

        return programInformationDto;
    }
}
