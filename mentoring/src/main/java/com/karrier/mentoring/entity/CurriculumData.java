package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.CurriculumDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurriculumData {
    private List<CurriculumDto> curriculumDtoList;

    public CurriculumData(List<CurriculumDto> curriculumDtoList){
        this.curriculumDtoList = curriculumDtoList;
    }
}
