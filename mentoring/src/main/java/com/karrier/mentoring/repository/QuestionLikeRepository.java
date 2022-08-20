package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.QuestionLike;
import com.karrier.mentoring.key.QuestionKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionLikeRepository extends JpaRepository<QuestionLike, QuestionKey> {

    QuestionLike findByProgramNoAndQuestionNoAndEmail(long programNo, long questionNo, String email);
}
