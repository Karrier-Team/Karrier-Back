package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Question;
import com.karrier.mentoring.key.QuestionKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, QuestionKey> {

    List<Question> findByProgramNo(long programNo);

    Question findByProgramNoAndQuestionNo(long programNo, long questionNo);

    long deleteByProgramNoAndQuestionNo(long programNo, long questionNo);

    List<Question> findByProgramNoAndTitleContaining(long programNo, String keyword);

    List<Question> findByProgramNoAndContentContaining(long programNo, String keyword);

    List<Question> findByProgramNoAndEmail(long programNo, String email);

    List<Question> findByEmail(String email);

    long deleteByProgramNo(long programNo);

    long deleteByEmail(String email);
}
