package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Tag;
import com.karrier.mentoring.key.TagKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, TagKey> {

    List<Tag> findByProgramNo(long programNo);

    long deleteByProgramNo(long programNo);
}
