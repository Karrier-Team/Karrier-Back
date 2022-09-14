package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Blob;
import java.util.List;

@Getter
@Setter
public class ProgramInformationDto {

    private String name;

    private String university;

    private String studentId;

    private String department;

    private String major;

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

    private Boolean onlineOffline;

    private String offlinePlace;

    private String openDate;

    private String closeDate;

    private String runningTime;

    private int maxPeople;

    private int price;

    private List<ReviewDetailDto> reviewDetailDtoList;

    private List<QuestionDetailDto> questionDetailDtoList;

    public static ProgramInformationDto createProgramInformationDto(Program program, Mentor mentor, String profileImage, List<Curriculum> curriculumList, List<RecommendedTarget> recommendedTargetList, List<Tag> tagList, List<ParticipationStudent> participationStudentList, Boolean isMyWishList, Boolean isMyFollowList,  Boolean isMyParticipate, List<ReviewDetailDto> reviewDetailDtoList, List<QuestionDetailDto> questionDetailDtoList){

        ProgramInformationDto programInformationDto = new ProgramInformationDto();

        programInformationDto.setName(mentor.getName());
        programInformationDto.setUniversity(mentor.getUniversity());
        programInformationDto.setStudentId(mentor.getStudentId());
        programInformationDto.setDepartment(mentor.getDepartment());
        programInformationDto.setMajor(mentor.getMajor());
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
        programInformationDto.setMainImage(program.getMainImage().getFileUrl());
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
        programInformationDto.setOnlineOffline(program.getOnlineOffline());
        programInformationDto.setOfflinePlace(program.getOfflinePlace());
        programInformationDto.setOpenDate(program.getOpenDate());
        programInformationDto.setCloseDate(program.getCloseDate());
        programInformationDto.setRunningTime(program.getRunningTime());
        programInformationDto.setMaxPeople(program.getMaxPeople());
        programInformationDto.setPrice(program.getPrice());
        programInformationDto.setReviewDetailDtoList(reviewDetailDtoList);
        programInformationDto.setQuestionDetailDtoList(questionDetailDtoList);

        return programInformationDto;
    }
}
