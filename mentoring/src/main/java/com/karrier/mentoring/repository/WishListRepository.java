package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.WishList;
import com.karrier.mentoring.key.WishListKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WishListRepository extends JpaRepository<WishList, WishListKey> {
    void deleteByProgramNoAndEmail(long programNo, String email);

    WishList findByProgramNoAndEmail(long programNo, String email);

    List<WishList> findAllByEmail(String email);

    long deleteByProgramNo(long programNo);

    long deleteByEmail(String email);
}
