package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.AnswerLike;
import com.karrier.mentoring.key.QuestionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerLikeRepository extends JpaRepository<AnswerLike, QuestionKey> {

    AnswerLike findByProgramNoAndQuestionNoAndEmail(long programNo, long questionNo, String email);
}
