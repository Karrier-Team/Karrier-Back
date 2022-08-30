package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.RecommendedTarget;
import com.karrier.mentoring.key.RecommendedTargetKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedTargetRepository extends JpaRepository<RecommendedTarget, RecommendedTargetKey> {

    List<RecommendedTarget> findByProgramNo(long programNo);
}
