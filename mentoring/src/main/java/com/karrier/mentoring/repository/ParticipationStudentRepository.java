package com.karrier.mentoring.repository;

import com.karrier.mentoring.key.ParticipationStudentKey;
import com.karrier.mentoring.entity.ParticipationStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationStudentRepository extends JpaRepository<ParticipationStudent, ParticipationStudentKey> {

    List<ParticipationStudent> findByProgramNo(Long programNo);

    List<ParticipationStudent> findByEmail(String email);

    long deleteByProgramNo(long programNo);

    long deleteByEmail(String email);

    ParticipationStudent findByEmailAndProgramNo(String email, long programNo);
}
