package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.WishList;
import com.karrier.mentoring.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;

    @Transactional
    public WishList createWishList(WishList wishList){ return wishListRepository.save(wishList);}

    @Transactional
    public void deleteWishList(long programNo, String email){
        wishListRepository.deleteByProgramNoAndEmail(programNo, email);
    }

    public WishList getWishList(long programNo, String email){
        return wishListRepository.findByProgramNoAndEmail(programNo, email);
    }

    public List<WishList> getMyWishLists(String email){
        return wishListRepository.findAllByEmail(email);
    }
}
