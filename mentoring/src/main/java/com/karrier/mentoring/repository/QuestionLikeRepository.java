package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.QuestionLike;
import com.karrier.mentoring.key.QuestionLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionLikeRepository extends JpaRepository<QuestionLike, QuestionLikeKey> {

    QuestionLike findByProgramNoAndQuestionNoAndEmail(long programNo, long questionNo, String email);

    long deleteByProgramNoAndQuestionNo(long programNo, long questionNo);

    long deleteByProgramNoAndQuestionNoAndEmail(long programNo, long questionNo, String email);

    long deleteByProgramNo(long programNo);

    long deleteByEmail(String email);
}
