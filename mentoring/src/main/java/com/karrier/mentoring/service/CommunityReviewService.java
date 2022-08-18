package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.CommentFormDto;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.repository.MemberRepository;
import com.karrier.mentoring.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityReviewService {

    private final ReviewRepository reviewRepository;

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

    public Review findReview(CommentFormDto commentFormDto) {
        return reviewRepository.findByProgramNoAndReviewNo(commentFormDto.getProgramNo(), commentFormDto.getReviewNo());
    }

    @Transactional
    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }
}
