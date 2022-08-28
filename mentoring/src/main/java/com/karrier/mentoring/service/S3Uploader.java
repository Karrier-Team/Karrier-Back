package com.karrier.mentoring.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.karrier.mentoring.entity.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public UploadFile upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, dirName);
    }

    public UploadFile modifyProfileImage(MultipartFile multipartFile, String dirName, String profileImageStoreName) throws IOException {

        //이전 파일 삭제
        deleteProfileImage(profileImageStoreName);

        //새로운 파일 저장
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, dirName);
    }

    public void deleteProfileImage(String profileImageStoreName) { //프로필 사진 삭제
        try{
            amazonS3Client.deleteObject(bucket+"/profile_image",profileImageStoreName );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteStudentInfo(String studentInfoStoreName) { //재학증명서 삭제
        try{
            amazonS3Client.deleteObject(bucket+"/student_info",studentInfoStoreName );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteMainImage(String mainImageStoreName) { //프로그램 메인 이미지 삭제
        try{
            amazonS3Client.deleteObject(bucket+"/main_image",mainImageStoreName );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // S3로 파일 업로드하기
    private UploadFile upload(File uploadFile, String dirName) {
        String storeName = UUID.randomUUID() + uploadFile.getName();
        String fileName = dirName + "/" + storeName;   // S3에 저장된 파일 이름
        putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return new UploadFile(uploadFile.getName(), storeName);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}