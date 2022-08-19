package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.PartInfo;
import com.karrier.mentoring.entity.ParticipationStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationStudentRepository extends JpaRepository<ParticipationStudent, PartInfo> {

    List<String> findAllByProgramNo(Long programNo);

}
