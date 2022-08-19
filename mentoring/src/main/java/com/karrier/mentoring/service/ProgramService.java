package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.repository.MentorRepository;
import com.karrier.mentoring.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;

    @Transactional
    public Program createProgram(Program program){
        return programRepository.save(program);
    }

    @Transactional
    public Program updateProgram(Program program){
        return programRepository.save(program);
    }

    public Program getProgram(Long programNo){
        return programRepository.findByProgramNo(programNo);
    }
}
