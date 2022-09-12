package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Program;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.util.List;

@Getter
@Setter
public class ProgramFormDto {

    private MultipartFile mainImageFile;

    private String type;

    private String title;

    private String introduce;

    private String onlineOffline;

    private String offlinePlace;

    private String openDate;

    private String closeDate;

    private String runningTime;

    private String maxPeople;

    private String price;

    private String mentorIntroduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String naverBlogAddress;

    private String facebookAddress;

    private String instarAddress;

    public static ProgramFormDto createProgramFormDto(Program program, Mentor mentor){
        ProgramFormDto programFormDto = new ProgramFormDto();

        programFormDto.setTitle(program.getTitle());
        programFormDto.setType(program.getType());
        programFormDto.setIntroduce(program.getIntroduce());
        programFormDto.setOnlineOffline(String.valueOf(program.getOnlineOffline()));
        programFormDto.setOfflinePlace(program.getOfflinePlace());
        programFormDto.setOpenDate(program.getOpenDate());
        programFormDto.setCloseDate(program.getCloseDate());
        programFormDto.setRunningTime(program.getRunningTime());
        programFormDto.setMaxPeople(String.valueOf(program.getMaxPeople()));
        programFormDto.setPrice(String.valueOf(program.getPrice()));
        programFormDto.setMentorIntroduce(mentor.getIntroduce());
        programFormDto.setClub(mentor.getClub());
        programFormDto.setContest(mentor.getContest());
        programFormDto.setExternalActivity(mentor.getExternalActivity());
        programFormDto.setIntern(mentor.getIntern());
        programFormDto.setNaverBlogAddress(mentor.getNaverBlogAddress());
        programFormDto.setFacebookAddress(mentor.getFacebookAddress());
        programFormDto.setInstarAddress(mentor.getInstarAddress());

        return programFormDto;
    }
}
