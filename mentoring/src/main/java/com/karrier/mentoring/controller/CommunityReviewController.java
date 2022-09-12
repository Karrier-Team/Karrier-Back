package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.ReviewCommentFormDto;
import com.karrier.mentoring.dto.ReviewDetailDto;
import com.karrier.mentoring.dto.ReviewFormDto;
import com.karrier.mentoring.dto.ReviewListDto;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.entity.ReviewLike;
import com.karrier.mentoring.http.BasicResponse;
import com.karrier.mentoring.http.SuccessDataResponse;
import com.karrier.mentoring.http.SuccessResponse;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.BadRequestException;
import com.karrier.mentoring.http.error.exception.ConflictException;
import com.karrier.mentoring.http.error.exception.NotFoundException;
import com.karrier.mentoring.http.error.exception.UnAuthorizedException;
import com.karrier.mentoring.repository.ProgramRepository;
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

@CrossOrigin("http://localhost:3000")
@RequestMapping("/community")
@RestController
@RequiredArgsConstructor
public class CommunityReviewController {

    private final CommunityReviewService communityReviewService;

    private final ProgramRepository programRepository;

    //해당 프로그램 전체 리뷰 리스트 띄우기
    @GetMapping("/review")
    public ResponseEntity<? extends BasicResponse> reviewList(@RequestParam("programNo") long programNo) {

        List<ReviewListDto> reviewList = communityReviewService.findReviewList(programNo);
        if (reviewList == null) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(new ArrayList()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(reviewList));
    }

    //해당 프로그램 전체 리뷰 리스트에서 검색할 경우 (후기제목, 후기내용, 닉네임)
    @GetMapping("/review/search")
    public ResponseEntity<? extends BasicResponse> reviewList(@RequestParam("programNo") long programNo, @RequestParam("category") String category, @RequestParam("keyword") String keyword) {

        if (programNo == 0 || category.isEmpty() || keyword.isEmpty()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        List<ReviewListDto> reviewList = communityReviewService.ReviewSearchList(programNo, category, keyword);
        if (reviewList == null) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(new ArrayList()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(reviewList));
    }

    //수강후기 등록 요청
    @PostMapping("/review/new")
    public ResponseEntity<? extends BasicResponse> createReview(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        Program byProgramNo = programRepository.findByProgramNo(reviewFormDto.getProgramNo());
        if (byProgramNo == null) { // 프로그램이 존재하지 않을 경우
            throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
        }
        if (byProgramNo.getProgramState() == false) { // 임시저장일 경우
            throw new NotFoundException(ErrorCode.PROGRAM_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Review review = Review.createReview(reviewFormDto, email);

        Review savedReview = communityReviewService.saveReview(review);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(savedReview));
    }

    //해당 리뷰 세부내용 출력
    @GetMapping("/review/detail")
    public ResponseEntity<? extends BasicResponse> reviewList(@RequestParam("programNo") long programNo,@RequestParam("reviewNo") long reviewNo) {

        Review review = communityReviewService.findReview(programNo, reviewNo);
        if (review == null) { //해당 프로그램 번호와 리뷰 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }

        ReviewDetailDto reviewDetailDto = communityReviewService.getReviewDetail(programNo, reviewNo);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(reviewDetailDto));
    }

    //수강후기 댓글 등록 요청
    @PutMapping("/review/comment/new")
    public ResponseEntity<? extends BasicResponse> createComment(@Valid ReviewCommentFormDto reviewCommentFormDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        Review review = communityReviewService.findReview(reviewCommentFormDto.getProgramNo(), reviewCommentFormDto.getReviewNo()); //이전에 저장했던 후기 정보 가져오기

        if (review == null) { //해당 프로그램 번호와 리뷰 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Program program = programRepository.findByProgramNo(reviewCommentFormDto.getProgramNo());
        if (!program.getEmail().equals(email)) { // 해당 프로그램 멘토인지 확인
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }

        Review reviewWithComment = Review.createComment(reviewCommentFormDto, review); //후기에 댓글 정보 추가하기
        Review updatedReview = communityReviewService.updateReview(reviewWithComment); //DB에 저장

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(updatedReview));
    }

    //수강후기 좋아요 요청시
    @PostMapping("/review/like")
    public ResponseEntity<? extends BasicResponse> likeReview(@RequestParam("programNo") long programNo,@RequestParam("reviewNo") long reviewNo) {

        Review review = communityReviewService.findReview(programNo, reviewNo);
        if (review == null) { //해당 프로그램 번호와 리뷰 번호에 해당하는 데이터가 없을 때
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        ReviewLike reviewLike = communityReviewService.findReviewLike(programNo, reviewNo, email);

        if (reviewLike != null) { //이미 좋아요 한 경우
            throw new ConflictException(ErrorCode.DUPLICATE_LIKE);
        }

        review.setReviewLikeNo(review.getReviewLikeNo()+1); // 좋아요 1 증가
        ReviewLike newReviewLike = ReviewLike.createReviewLike(programNo, reviewNo, email);
        ArrayList<Object> objects = communityReviewService.likeReview(review, newReviewLike);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(objects));
    }

    //수강후기 삭제 요청시
    @DeleteMapping("/review")
    public ResponseEntity<? extends BasicResponse> deleteReview(@RequestParam("programNo") long programNo, @RequestParam("reviewNo") long reviewNo) {

        Review review = communityReviewService.findReview(programNo, reviewNo);
        if (review == null) { //해당 프로그램 번호와 리뷰 번호에 해당하는 데이터가 없을 때
            throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        if (!review.getEmail().equals(email)) { //작성자가 아닐 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        communityReviewService.deleteReview(programNo, reviewNo);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
    }

    //수강후기 댓글 삭제 요청시
    @DeleteMapping("/review/comment")
    public ResponseEntity<? extends BasicResponse> deleteComment(@RequestParam("programNo") long programNo, @RequestParam("reviewNo") long reviewNo) {

        Review review = communityReviewService.findReview(programNo, reviewNo);
        if (review == null) { //해당 프로그램 번호가 없을 때
            throw new NotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }
        if (review.getCommentDate() == null) { //리뷰의 댓글이 없을 때
            throw new NotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        String commentEmail = programRepository.findByProgramNo(programNo).getEmail();//수강후기 댓글 단 사람 이메일 찾기
        if (!email.equals(commentEmail)) { //작성자가 아닐 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        Review updatedReview = communityReviewService.deleteComment(programNo, reviewNo);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(updatedReview));
    }
}
