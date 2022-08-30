package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.ReviewLike;
import com.karrier.mentoring.key.ReviewLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewLikeKey> {

    ReviewLike findByProgramNoAndReviewNoAndEmail(long programNo, long reviewNo, String email);
}
