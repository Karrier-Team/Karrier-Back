package com.karrier.mentoring.entity;


import com.karrier.mentoring.dto.ProgramFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Program")
@Getter
@Setter
@ToString
public class Program {

    @Id
    private long programNo;

    @Column(nullable = false)
    private String email;

    @Embedded
    @AttributeOverrides({

            @AttributeOverride(name="uploadFileName", column = @Column(name="MAIN_IMAGE_UPLOAD_NAME")),
            @AttributeOverride(name="storeFileName", column = @Column(name="MAIN_IMAGE_STORE_NAME")),

    })
    private UploadFile mainImage;

    private String title;

    private String shortIntroduce;

    private Boolean onlineOffline; //온라인이 true

    private String offlinePlace; // onlineOffline이 false일시에만

    private String openDate;

    private String closeDate;

    private String runningTime;

    private int maxPeople;

    private int price;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private LocalDateTime tempDate;

    private LocalDateTime modifiedDate;

    private int applyPeople;

    private String state;

    private int likeCount;

    private float averageStar;

    private String tag;

    @Column(nullable = false)
    private Boolean programState;


    public static Program createProgram(Long ProgramNo, ProgramFormDto programFormDto, UploadFile mainImage, String email){
        Program program = new Program();

        program.setProgramNo(ProgramNo);
        program.setEmail(email);
        program.setMainImage(mainImage);
        program.setTitle(programFormDto.getTitle());
        program.setShortIntroduce(programFormDto.getShortIntroduce());
        program.setOnlineOffline(programFormDto.getOnlineOffline());
        program.setOfflinePlace(programFormDto.getOfflinePlace());
        program.setOpenDate(programFormDto.getOpenDate());
        program.setCloseDate(programFormDto.getCloseDate());
        program.setRunningTime(programFormDto.getRunningTime());
        program.setMaxPeople(programFormDto.getMaxPeople());
        program.setPrice(programFormDto.getPrice());
        program.setCreateDate(LocalDateTime.now());
        program.setApplyPeople(0);
        program.setLikeCount(0);
        program.setAverageStar(0);
        program.setTag(programFormDto.getTag());

        return program;
    }

    public static Program updateProgram(Program program, UploadFile mainImage, ProgramFormDto programFormDto){

        program.setMainImage(mainImage);
        program.setTitle(programFormDto.getTitle());
        program.setShortIntroduce(programFormDto.getShortIntroduce());
        program.setOnlineOffline(programFormDto.getOnlineOffline());
        program.setOfflinePlace(programFormDto.getOfflinePlace());
        program.setOpenDate(programFormDto.getOpenDate());
        program.setCloseDate(programFormDto.getCloseDate());
        program.setRunningTime(programFormDto.getRunningTime());
        program.setMaxPeople(programFormDto.getMaxPeople());
        program.setPrice(programFormDto.getPrice());

        //기존에 완전 저장된 프로그램이면 modifiedDate 수정해주기
        if (program.getProgramState().equals(true)){
            program.setModifiedDate(LocalDateTime.now());
        }
        else{
            program.setCreateDate(LocalDateTime.now());
        }

        program.setTag(programFormDto.getTag());

        program.setProgramState(true);

        return program;
    }


    public static Program updateTempProgram(Program program, UploadFile mainImage, ProgramFormDto programFormDto){

        program.setMainImage(mainImage);
        program.setTitle(programFormDto.getTitle());
        program.setShortIntroduce(programFormDto.getShortIntroduce());
        program.setOnlineOffline(programFormDto.getOnlineOffline());
        program.setOfflinePlace(programFormDto.getOfflinePlace());
        program.setOpenDate(programFormDto.getOpenDate());
        program.setCloseDate(programFormDto.getCloseDate());
        program.setRunningTime(programFormDto.getRunningTime());
        program.setMaxPeople(programFormDto.getMaxPeople());
        program.setPrice(programFormDto.getPrice());
        program.setModifiedDate(LocalDateTime.now());
        program.setTag(programFormDto.getTag());

        program.setProgramState(false);

        return program;
    }
}
