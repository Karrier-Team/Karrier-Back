package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.CommentFormDto;
import com.karrier.mentoring.dto.ReviewDetailDto;
import com.karrier.mentoring.dto.ReviewListDto;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.entity.ReviewLike;
import com.karrier.mentoring.repository.MemberRepository;
import com.karrier.mentoring.repository.ReviewLikeRepository;
import com.karrier.mentoring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final MemberRepository memberRepository;

    //새로운 수강후기 등록
    @Transactional
    public Review saveReview(Review review) {

        List<Review> reviewList = reviewRepository.findByProgramNo(review.getProgramNo());

        long max = 1;
        if (reviewList != null) { // 수강후기 번호 자동 생성
            for (Review review1 : reviewList) {
                if (review1.getReviewNo() > max) {
                    max = review1.getReviewNo();
                }
            }
        }
        review.setReviewNo(max+1); // 수강후기 번호 세팅

        return reviewRepository.save(review);
    }

    //리뷰 1개 찾기
    public Review findReview(long programNo, long reviewNo) {
        return reviewRepository.findByProgramNoAndReviewNo(programNo, reviewNo);
    }

    //한 프로그램의 모든 리뷰 찾아서 필요한 데이터 추가해서 반환
    public List<ReviewListDto> findReviewList(long programNo) {

        List<Review> reviewList = reviewRepository.findByProgramNo(programNo);

        /**
         * 프로그램 이름 찾는 코드 추가예정
         */

        ArrayList<ReviewListDto> reviewListDto = new ArrayList<>();

        for (Review review : reviewList) {
            String nickname = memberRepository.findByEmail(review.getEmail()).getNickname(); //닉네임 찾기
            reviewListDto.add(ReviewListDto.createReviewListDto(review, "프로그램 이름", nickname));
        }
        return reviewListDto;
    }

    //한 프로그램의 모든 리뷰 찾아서 필요한 데이터 추가해서 반환
    public ReviewDetailDto getReviewDetail(long programNo, long reviewNo) {

        Review review = reviewRepository.findByProgramNoAndReviewNo(programNo, reviewNo);

        /**
         * 프로그램 이름 찾는 코드 추가예정
         */

        String writerNickname = memberRepository.findByEmail(review.getEmail()).getNickname(); //작성자 닉네임 찾기
        /**
         * 프로그램 만든 멘토 이름 찾는 코드 추가 예정
         */
        return ReviewDetailDto.createReviewDetailDto(review, "프로그램 이름", writerNickname, "멘토이름");
    }

    @Transactional
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    //리뷰 삭제
    @Transactional
    public long deleteReview(long programNo, long reviewNo) {
        return reviewRepository.deleteByProgramNoAndReviewNo(programNo, reviewNo);
    }

    //리뷰 댓글 삭제
    @Transactional
    public Review deleteComment(long programNo, long reviewNo) {
        Review review = findReview(programNo, reviewNo); //해당리뷰 찾기
        if (review == null) {
            return null;
        }
        review.setComment(null);
        review.setCommentDate(null);
        return reviewRepository.save(review);
    }

    public ReviewLike findReviewLike(long programNo, long reviewNo, String email) {
        return reviewLikeRepository.findByProgramNoAndReviewNoAndEmail(programNo, reviewNo, email);
    }

    @Transactional
    public ArrayList<Object> likeReview(Review review, ReviewLike reviewLike) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(reviewRepository.save(review));
        objects.add(reviewLikeRepository.save(reviewLike));

        return objects;
    }
}
