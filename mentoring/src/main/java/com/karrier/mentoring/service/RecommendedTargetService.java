package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.RecommendedTarget;
import com.karrier.mentoring.repository.RecommendedTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendedTargetService {
    private final RecommendedTargetRepository recommendedTargetRepository;

    @Transactional
    public RecommendedTarget createRecommendedTarget(RecommendedTarget recommendedTarget){
        return recommendedTargetRepository.save(recommendedTarget);
    }

    @Transactional
    public RecommendedTarget modifyRecommendedTarget(RecommendedTarget recommendedTarget){
        return recommendedTargetRepository.save(recommendedTarget);
    }

    public List<RecommendedTarget> getRecommendedTargetListByNo(long programNo){
        return recommendedTargetRepository.findByProgramNo(programNo);

    }

    @Transactional
    public long removeRecommendedTargetByProgramNo(long programNo){
        return recommendedTargetRepository.deleteByProgramNo(programNo);
    }
}
