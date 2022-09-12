package com.karrier.mentoring.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailData {
    private List<String> emailList;

    public EmailData(List<String> emailList){
        this.emailList = emailList;
    }
}
