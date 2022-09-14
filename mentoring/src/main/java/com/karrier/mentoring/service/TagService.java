package com.karrier.mentoring.service;


import com.karrier.mentoring.entity.Tag;
import com.karrier.mentoring.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public Tag createTag(Tag tag){
        return tagRepository.save(tag);
    }

    @Transactional
    public Tag modifyTag(Tag tag){
        return tagRepository.save(tag);
    }

    public List<Tag> getTagByNo(long programNo){
        return tagRepository.findByProgramNo(programNo);
    }

    @Transactional
    public long removeTagByProgramNo(long programNo){
        return tagRepository.deleteByProgramNo(programNo);
    }
}