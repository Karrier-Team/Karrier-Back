package com.karrier.mentoring.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedTargetKey implements Serializable {
    private long programNo;
    private String target;
}
