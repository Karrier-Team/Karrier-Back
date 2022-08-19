package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.CommentFormDto;
import com.karrier.mentoring.dto.ReviewFormDto;
import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.entity.ReviewLike;
import com.karrier.mentoring.service.CommunityReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/community")
@RestController
@RequiredArgsConstructor
public class CommunityReviewController {

    private final CommunityReviewService communityReviewService;

    //전체 프로그램 리스트 띄우기
    @GetMapping("/reviews")
    public ResponseEntity<Object> programList(@RequestParam("department") String department) {

        //구현 예정
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //해당 프로그램 전체 리뷰 리스트 띄우기
    @GetMapping("/review")
    public ResponseEntity<List<Review>> reviewList(@RequestParam("programNo") long programNo) {

        List<Review> reviewList = communityReviewService.findReviewList(programNo);

        return ResponseEntity.status(HttpStatus.OK).body(reviewList);
    }

    //수강후기 등록 요청
    @PostMapping("/review/new")
    public ResponseEntity<Object> createReview(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult) {
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
    @PostMapping("/review/comment/new")
    public ResponseEntity<Object> createComment(@Valid CommentFormDto commentFormDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }
        Review review = communityReviewService.findReview(commentFormDto.getProgramNo(), commentFormDto.getReviewNo()); //이전에 저장했던 후기 정보 가져오기

        if (review == null) { //해당 프로그램 번호와 리뷰 번호에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no review error");
        }

        Review reviewWithComment = Review.createComment(commentFormDto, review); //후기에 댓글 정보 추가하기
        Review updatedReview = communityReviewService.updateReview(reviewWithComment); //DB에 저장

        return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
    }

    //수강후기 좋아요 요청시
    @PostMapping("/review/like")
    public ResponseEntity<Object> likeReview(@RequestParam("programNo") long programNo,@RequestParam("reviewNo") long reviewNo) {

        Review review = communityReviewService.findReview(programNo, reviewNo);
        if (review == null) { //해당 프로그램 번호와 리뷰 번호에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no review error");
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        ReviewLike reviewLike = communityReviewService.findReviewLike(programNo, reviewNo, email);
        if (reviewLike != null) { //이미 좋아요한 회원일 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("already like error");
        }

        review.setReviewLikeNo(review.getReviewLikeNo()+1); // 좋아요 1 증가
        ReviewLike newReviewLike = ReviewLike.createReviewLike(programNo, reviewNo, email);
        ArrayList<Object> objects = communityReviewService.likeReview(review, newReviewLike);

        return ResponseEntity.status(HttpStatus.OK).body(objects);
    }
}
