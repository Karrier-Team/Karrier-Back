package com.karrier.mentoring.key;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumKey  implements Serializable {
    private long programNo;
    private String curriculumTitle;
}
