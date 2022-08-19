package com.karrier.mentoring.controller;

import com.karrier.mentoring.service.CommunityQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/community")
@RestController
@RequiredArgsConstructor
public class CommunityQuestionController {

    private final CommunityQuestionService communityQuestionService;

    //전체 프로그램 리스트 띄우기
    @GetMapping("/questions")
    public ResponseEntity<Object> programList() {
        //구현 예정

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //해당 프로그램 전체 질문 리스트 띄우기
    @GetMapping("/question")
    public ResponseEntity<Object> reviewList(@RequestParam("programNo") long programNo) {
        // 구현예정

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


}
