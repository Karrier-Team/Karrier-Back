package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProgramNoData {
    private List<Long> programNoList;

    public ProgramNoData(List<Long> programNoList){
        this.programNoList = programNoList;
    }
}
