package com.karrier.mentoring.dto;

import com.karrier.mentoring.entity.Program;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class ProgramFormDto {

    private MultipartFile mainImageFile;

    @NotBlank(message = "프로그램 제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "프로그램에 대한 한 줄 소개는 필수 입력 값입니다.")
    private String shortIntroduce;

    @NotBlank(message = "온라인/오프라인 여부는 필수 입력 값입니다.")
    private Boolean onlineOffline;

    private String offlinePlace;

    @NotBlank(message = "진행기간 시작일은 필수 입력 값입니다.")
    private String openDate;

    @NotBlank(message = "진행기간 종료일은 필수 입력 값입니다.")
    private String closeDate;

    @NotBlank(message = "진행시간은 필수 입력 값입니다.")
    private String runningTime;

    @NotBlank(message = "최대수강인원은 필수 입력 값입니다.")
    private Integer maxPeople;

    @NotBlank(message = "가격 설정은 필수 입력 값입니다.")
    @Range(min = 0, max = 100000, message = "0원 이상 100000이하 값만 가능합니다.")
    private Integer price;

    @NotBlank(message = "추천대상은 필수 입력 값입니다.")
    private List<String> recommendedTarget;

    @NotBlank(message = "강의 구성은 필수 입력 값입니다.")
    private List<String> curriculumTitleList;

    @NotBlank(message = "강의 구성은 필수 입력 값입니다.")
    private List<String> curriculumTextList;

    @NotBlank(message = "멘토소개는 필수 입력 값입니다.")
    private String mentorIntroduce;

    private String club;

    private String contest;

    private String externalActivity;

    private String intern;

    private String naverBlogAddress;

    private String facebookAddress;

    private String instarAddress;

    private List<String> tag;

    public static ProgramFormDto createProgramFormDto(Program program){
        ProgramFormDto programFormDto = new ProgramFormDto();

        programFormDto.setTitle(program.getTitle());
        programFormDto.setShortIntroduce(program.getShortIntroduce());
        programFormDto.setOnlineOffline(program.getOnlineOffline());
        programFormDto.setOfflinePlace(program.getOfflinePlace());
        programFormDto.setOpenDate(program.getOpenDate());
        programFormDto.setCloseDate(program.getCloseDate());
        programFormDto.setRunningTime(program.getRunningTime());
        programFormDto.setMaxPeople(program.getMaxPeople());
        programFormDto.setPrice(program.getPrice());
        programFormDto.setRecommendedTarget(program.getRecommendedTarget());
        programFormDto.setCurriculumTitleList(program.getCurriculumTitleList());
        programFormDto.setCurriculumTextList(program.getCurriculumTextList());
        programFormDto.setTag(program.getTag());

        return programFormDto;
    }
}
