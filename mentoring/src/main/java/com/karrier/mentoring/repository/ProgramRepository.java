package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    Program findByProgramNo(Long programNo);

    List<Program> findByEmail(String email);

    List<Program> findByEmailAndProgramState(String email, Boolean complete);

    List<Program> findByProgramStateAndEmailInAndTitleContaining(Boolean complete, List<String> emails, String Title);

    List<Program> findByProgramStateAndEmailIn(Boolean complete, List<String> emails);

}
