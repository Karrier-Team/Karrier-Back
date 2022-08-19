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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programNo;

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

    private Integer maxPeople;

    private Integer price;

    @ElementCollection
    private List<String> recommendedTarget;

    private LocalDateTime createDate;

    private LocalDateTime modifiedDate;

    private Integer applyPeople;

    private String state;

    private Integer likeCount;

    private Integer averageStar;

    @ElementCollection
    private List<String> curriculumTitleList;

    @ElementCollection
    private List<String> curriculumTextList;

    @ElementCollection
    private List<String> tag;

    private Boolean programState = false;


    public static Program createProgram(ProgramFormDto programFormDto, UploadFile mainImage, String email){

        Program program = new Program();

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
        program.setRecommendedTarget(programFormDto.getRecommendedTarget());
        program.setCreateDate(LocalDateTime.now());
        program.setApplyPeople(0);
        program.setLikeCount(0);
        program.setAverageStar(0);
        program.setCurriculumTitleList(programFormDto.getCurriculumTitleList());
        program.setCurriculumTextList(programFormDto.getCurriculumTextList());
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
        program.setRecommendedTarget(programFormDto.getRecommendedTarget());

        //기존에 완전 저장된 프로그램이면 modifiedDate 수정해주기
        if (program.getProgramState().equals(true)){
            program.setModifiedDate(LocalDateTime.now());
        }
        else{
            program.setCreateDate(LocalDateTime.now());
        }

        program.setCurriculumTitleList(programFormDto.getCurriculumTitleList());
        program.setCurriculumTextList(programFormDto.getCurriculumTextList());
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
        program.setRecommendedTarget(programFormDto.getRecommendedTarget());
        program.setModifiedDate(LocalDateTime.now());
        program.setCurriculumTitleList(programFormDto.getCurriculumTitleList());
        program.setCurriculumTextList(programFormDto.getCurriculumTextList());
        program.setTag(programFormDto.getTag());

        program.setProgramState(false);

        return program;
    }
}
