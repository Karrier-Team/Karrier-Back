package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.QuestionComment;
import com.karrier.mentoring.key.QuestionCommentKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCommentRepository extends JpaRepository<QuestionComment, QuestionCommentKey> {
}
