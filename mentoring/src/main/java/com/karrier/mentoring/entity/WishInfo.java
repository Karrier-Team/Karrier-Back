package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WishInfo  implements Serializable {
    private String programNo;
    private String email;
}
