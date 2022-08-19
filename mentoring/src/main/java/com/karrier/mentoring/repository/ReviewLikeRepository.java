package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.ReviewLike;
import com.karrier.mentoring.key.ReviewKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewKey> {

    ReviewLike findByProgramNoAndReviewNoAndEmail(long programNo, long reviewNo, String email);
}
