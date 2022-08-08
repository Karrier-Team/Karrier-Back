package com.karrier.mentoring.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class UploadFile {

    private String uploadFileName;

    private String storeFileName;

    protected UploadFile() {
    }

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
