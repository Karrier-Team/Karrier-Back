package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.CommentFormDto;
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
        review.setReviewNo(max); // 수강후기 번호 세팅

        return reviewRepository.save(review);
    }

    //리뷰 1개 찾기
    public Review findReview(long programNo, long reviewNo) {
        return reviewRepository.findByProgramNoAndReviewNo(programNo, reviewNo);
    }

    //한 프로그램의 모든 리뷰 찾기
    public List<Review> findReviewList(long programNo) {
        return reviewRepository.findByProgramNo(programNo);
    }

    @Transactional
    public Review updateReview(Review review) {
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
