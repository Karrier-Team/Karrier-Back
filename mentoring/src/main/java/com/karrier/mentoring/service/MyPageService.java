package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Question;
import com.karrier.mentoring.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {

    private final QuestionRepository questionRepository;

    //질문 해결완료시
    @Transactional
    public Question solveQuestion(Question question) {
        question.setSolve(true); // 해결으로 변경
        return questionRepository.save(question);
    }
}
