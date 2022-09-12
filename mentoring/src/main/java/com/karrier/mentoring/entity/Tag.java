package com.karrier.mentoring.entity;

import com.karrier.mentoring.dto.TagDto;
import com.karrier.mentoring.key.TagKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Tag")
@Getter
@Setter
@IdClass(TagKey.class)
public class Tag implements Serializable {

    @Id
    private long programNo;

    @Id
    private String tagName;

    public static Tag createTag(long programNo, TagDto tagDto){
        Tag tag = new Tag();

        tag.setProgramNo(programNo);
        tag.setTagName(tagDto.getTagName());

        return tag;
    }

}
