package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "WishList")
@Getter
@Setter
@IdClass(WishList.class)
public class WishList implements Serializable {

    @Id
    private long programNo;

    @Id
    private String email;

    @Column(nullable = false)
    private LocalDateTime wishDate;

    public static WishList createWishList(long programNo, String email){
        WishList wishList = new WishList();

        wishList.setProgramNo(programNo);
        wishList.setEmail(email);
        wishList.setWishDate(LocalDateTime.now());

        return wishList;
    }
}
