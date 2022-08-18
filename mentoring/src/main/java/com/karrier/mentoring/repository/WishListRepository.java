package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {
}
