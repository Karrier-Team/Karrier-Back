package com.karrier.mentoring.entity;


import com.karrier.mentoring.dto.ProgramFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Program")
@Getter
@Setter
@ToString
@SequenceGenerator(
        name = "PROGRAM_SEQ_GENERATOR",
        sequenceName = "PROGRAM_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROGRAM_SEQ_GENERATOR")
    @Column(name = "program_no")
    private long programNo;

    private String email;

    @Embedded
    @AttributeOverrides({

            @AttributeOverride(name="uploadFileName", column = @Column(name="MAIN_IMAGE_UPLOAD_NAME")),
            @AttributeOverride(name="storeFileName", column = @Column(name="MAIN_IMAGE_STORE_NAME")),

    })
    private UploadFile mainImage;

    private String type;

    private String title;

    private String introduce;

    private Boolean onlineOffline; //온라인이 true

    private String offlinePlace; // onlineOffline이 false일시에만

    private String openDate;

    private String closeDate;

    private String runningTime;

    private int maxPeople;

    private int price;

    private LocalDateTime createDate;

    private LocalDateTime tempDate;

    private LocalDateTime modifiedDate;

    private int applyPeople;

    private String state;

    private int likeCount;

    private float averageStar;

    private Boolean programState;


    public static Program createProgram(ProgramFormDto programFormDto, UploadFile mainImage, String email){
        Program program = new Program();

        program.setEmail(email);
        program.setMainImage(mainImage);
        program.setType(programFormDto.getType());
        program.setTitle(programFormDto.getTitle());
        program.setIntroduce(programFormDto.getIntroduce());
        program.setOnlineOffline(Boolean.parseBoolean(programFormDto.getOnlineOffline()));
        program.setOfflinePlace(programFormDto.getOfflinePlace());
        program.setOpenDate(programFormDto.getOpenDate());
        program.setCloseDate(programFormDto.getCloseDate());
        program.setRunningTime(programFormDto.getRunningTime());
        program.setMaxPeople(Integer.parseInt(programFormDto.getMaxPeople()));
        program.setPrice(Integer.parseInt(programFormDto.getPrice()));
        program.setCreateDate(LocalDateTime.now());
        program.setApplyPeople(0);
        program.setLikeCount(0);
        program.setAverageStar(0);

        return program;
    }

    public static Program updateProgram(Program program, UploadFile mainImage, ProgramFormDto programFormDto){

        program.setMainImage(mainImage);
        program.setType(programFormDto.getType());
        program.setTitle(programFormDto.getTitle());
        program.setIntroduce(programFormDto.getIntroduce());
        program.setOnlineOffline(Boolean.parseBoolean(programFormDto.getOnlineOffline()));
        program.setOfflinePlace(programFormDto.getOfflinePlace());
        program.setOpenDate(programFormDto.getOpenDate());
        program.setCloseDate(programFormDto.getCloseDate());
        program.setRunningTime(programFormDto.getRunningTime());
        program.setMaxPeople(Integer.parseInt(programFormDto.getMaxPeople()));
        program.setPrice(Integer.parseInt(programFormDto.getPrice()));

        return program;
    }


    public static Program updateTempProgram(Program program, UploadFile mainImage, ProgramFormDto programFormDto){

        program.setMainImage(mainImage);
        program.setType(programFormDto.getType());
        program.setTitle(programFormDto.getTitle());
        program.setIntroduce(programFormDto.getIntroduce());
        program.setOnlineOffline(Boolean.parseBoolean(programFormDto.getOnlineOffline()));
        program.setOfflinePlace(programFormDto.getOfflinePlace());
        program.setOpenDate(programFormDto.getOpenDate());
        program.setCloseDate(programFormDto.getCloseDate());
        program.setRunningTime(programFormDto.getRunningTime());
        program.setMaxPeople(Integer.parseInt(programFormDto.getMaxPeople()));
        program.setPrice(Integer.parseInt(programFormDto.getPrice()));

        program.setProgramState(false);

        return program;
    }
}
