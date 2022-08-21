package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.QuestionComment;
import com.karrier.mentoring.key.QuestionLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionCommentRepository extends JpaRepository<QuestionComment, QuestionLikeKey> {

    List<QuestionComment> findByProgramNoAndQuestionNo(long programNo, long questionNo);

    QuestionComment findByProgramNoAndQuestionNoAndCommentNo(long programNo, long questionNo, long commentNo);

    long deleteByProgramNoAndQuestionNoAndCommentNo(long programNo, long questionNo, long commentNo);
}
