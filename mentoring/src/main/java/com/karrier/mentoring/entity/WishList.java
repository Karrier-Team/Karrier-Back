package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "WishList")
@Getter
@Setter
public class WishList {

    @Id
    private String program_no;

    private String email;

    private LocalDateTime wishDate;
}
