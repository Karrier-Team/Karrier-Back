package com.karrier.mentoring.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class UploadFile {

    private String uploadFileName;

    private String storeFileName;

    private String fileUrl;

    protected UploadFile() {
    }

    public UploadFile(String uploadFileName, String storeFileName, String fileUrl) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileUrl = fileUrl;
    }
}
