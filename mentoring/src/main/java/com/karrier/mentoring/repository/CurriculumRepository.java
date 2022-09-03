package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Curriculum;
import com.karrier.mentoring.key.CurriculumKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurriculumRepository extends JpaRepository<Curriculum, CurriculumKey> {

    List<Curriculum> findByProgramNo(long programNo);

    long deleteByProgramNo(long programNo);
}
