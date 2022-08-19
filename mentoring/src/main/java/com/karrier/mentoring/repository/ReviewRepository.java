package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.key.ReviewKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewKey> {

    List<Review> findByProgramNo(long programNo);

    Review findByProgramNoAndReviewNo(long programNo, long reviewNo);

    long deleteByProgramNoAndReviewNo(long programNo, long reviewNo);
}
