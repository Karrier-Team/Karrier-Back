package com.karrier.mentoring.key;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListKey  implements Serializable {
    private String programNo;
    private String email;
}
