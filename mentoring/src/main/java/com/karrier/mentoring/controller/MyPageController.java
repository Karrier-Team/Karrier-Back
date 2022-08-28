package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.QuestionListDto;
import com.karrier.mentoring.dto.ReviewListDto;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.entity.Question;
import com.karrier.mentoring.repository.QuestionRepository;
import com.karrier.mentoring.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/my-page")
@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final QuestionRepository questionRepository;

    private final CommunityQuestionService communityQuestionService;

    private final CommunityReviewService communityReviewService;

    private final MyPageService myPageService;

    //나의 전체 질문 리스트 띄우기
    @GetMapping("/manage/question")
    public ResponseEntity<Object> questionList() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<QuestionListDto> myPageQuestionList = communityQuestionService.findMyPageQuestionList(email);
        if (myPageQuestionList == null) {//나의 질문이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no question");
        }
        return ResponseEntity.status(HttpStatus.OK).body(myPageQuestionList);
    }

    @PostMapping("/manage/question/solve")
    public ResponseEntity<Object> questionSolve(@RequestParam("programNo") long programNo, @RequestParam("questionNo") long questionNo) {

        Question byProgramNoAndQuestionNo = questionRepository.findByProgramNoAndQuestionNo(programNo, questionNo);
        if (byProgramNoAndQuestionNo == null) { // 해결할 프로그램이 존재하지 않을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no question error");
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        if (!email.equals(byProgramNoAndQuestionNo.getEmail())) { //작성자와 해결 누른사람이 일치하지 않을 때
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not writer error");
        }
        Question question = myPageService.solveQuestion(byProgramNoAndQuestionNo);//해결완료로 변경
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    //나의 전체 리뷰 리스트 띄우기
    @GetMapping("/manage/review")
    public ResponseEntity<Object> reviewList() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<ReviewListDto> myPageReviewList = communityReviewService.findMyPageReviewList(email);
        if (myPageReviewList == null) { //나의 수강후기 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no review");
        }
        return ResponseEntity.status(HttpStatus.OK).body(myPageReviewList);
    }
}
