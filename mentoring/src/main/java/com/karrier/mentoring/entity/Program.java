package com.karrier.mentoring.entity;

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
            @AttributeOverride(name = "uploadFileName", column = @Column(name = "MAIN_IMAGE_UPLOAD_NAME")),
            @AttributeOverride(name = "storeFileName", column = @Column(name = "MAIN_IMAGE_STORE_NAME")),
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
}