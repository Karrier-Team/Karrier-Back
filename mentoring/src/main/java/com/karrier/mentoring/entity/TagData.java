package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.TagDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagData {
    private List<TagDto> tagDtoList;

    public TagData(List<TagDto> tagDtoList){
        this.tagDtoList = tagDtoList;
    }
}
