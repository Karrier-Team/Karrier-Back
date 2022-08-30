package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Curriculum;
import com.karrier.mentoring.repository.CurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurriculumService {

    CurriculumRepository curriculumRepository;

    @Transactional
    public Curriculum createCurriculum(Curriculum curriculum){
        return curriculumRepository.save(curriculum);
    }

    public List<Curriculum> getCurriculumByProgramNo(long programNo){
        return curriculumRepository.findByProgramNo(programNo);

    }
}
