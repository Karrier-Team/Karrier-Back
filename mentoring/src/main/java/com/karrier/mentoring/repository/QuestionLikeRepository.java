package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.QuestionLike;
import com.karrier.mentoring.key.QuestionLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionLikeRepository extends JpaRepository<QuestionLike, QuestionLikeKey> {

    QuestionLike findByProgramNoAndQuestionNoAndEmail(long programNo, long questionNo, String email);
}
