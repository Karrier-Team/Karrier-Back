package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.AnswerLike;
import com.karrier.mentoring.key.QuestionLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerLikeRepository extends JpaRepository<AnswerLike, QuestionLikeKey> {

    AnswerLike findByProgramNoAndQuestionNoAndEmail(long programNo, long questionNo, String email);
}
