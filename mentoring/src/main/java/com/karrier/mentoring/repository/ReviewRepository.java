package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.key.ReviewKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, ReviewKey> {
}
