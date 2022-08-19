package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    Program findByProgramNo(Long programNo);

    List<Program> findAllByEmail(String email);

    List<Program> findAllByEmailInOrderByLikeCount(List<String> emails);
}

