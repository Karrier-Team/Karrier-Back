package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.CommentFormDto;
import com.karrier.mentoring.dto.ReviewFormDto;
import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.service.CommunityReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/community")
@RestController
@RequiredArgsConstructor
public class CommunityReviewController {

    private final CommunityReviewService communityReviewService;

    //전체 프로그램 리스트 띄우기
    @GetMapping("/reviews")
    public ResponseEntity<Object> programList() {

        //구현 예정
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //해당 프로그램 전체 리뷰 리스트 띄우기
    @GetMapping("/review")
    public ResponseEntity<Object> reviewList(@RequestParam("programNo") String programNo) {

        //구현 예정
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //수강후기 등록 요청
    @PostMapping("/new")
    public ResponseEntity<Object> createReview(@ModelAttribute ReviewFormDto reviewFormDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Review review = Review.createReview(reviewFormDto, email);

        Review savedReview = communityReviewService.saveReview(review);

        return ResponseEntity.status(HttpStatus.OK).body(savedReview);
    }

    //수강후기 댓글 등록 요청
    @PostMapping("/comment/new")
    public ResponseEntity<Object> createComment(@ModelAttribute CommentFormDto commentFormDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }
        Review review = communityReviewService.findReview(commentFormDto); //이전에 저장했던 후기 정보 가져오기
        Review reviewWithComment = Review.createComment(commentFormDto, review); //후기에 댓글 정보 추가하기
        Review updatedReview = communityReviewService.updateReview(reviewWithComment); //DB에 저장

        return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
    }
}
