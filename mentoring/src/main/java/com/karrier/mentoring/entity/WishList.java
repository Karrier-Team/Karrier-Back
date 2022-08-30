package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
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

    private LocalDateTime wishDate;

    public static WishList createWishList(long programNo, String email){
        WishList wishList = new WishList();

        wishList.setProgramNo(programNo);
        wishList.setEmail(email);
        wishList.setWishDate(LocalDateTime.now());

        return wishList;
    }
}
