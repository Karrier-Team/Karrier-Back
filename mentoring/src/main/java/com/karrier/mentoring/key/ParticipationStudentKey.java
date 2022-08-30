package com.karrier.mentoring.key;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationStudentKey implements Serializable {
    private long programNo;
    private String email;
}
