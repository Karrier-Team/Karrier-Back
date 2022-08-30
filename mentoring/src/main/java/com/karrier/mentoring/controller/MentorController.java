package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.*;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.UploadFile;
import com.karrier.mentoring.service.MemberService;
import com.karrier.mentoring.service.MentorService;
import com.karrier.mentoring.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;

@RequestMapping("/mentors")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MentorController {

    private final MentorService mentorService;

    private final MemberService memberService;

    private final S3Uploader s3Uploader;

    @PostMapping(value = "/new")
    public ResponseEntity<Object> mentorForm(@Valid MentorFormDto mentorFormDto, BindingResult bindingResult, Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        //프로필 사진이 없을 때
        if (mentorFormDto.getProfileImageFile().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("profile image empty error");
        }

        //프로필 사진이 없을 때
        if (mentorFormDto.getStudentInfoFile().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("student info file empty error");
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //S3 스토리지에 파일 저장 후 파일 이름 반환
        UploadFile studentInfo = s3Uploader.upload(mentorFormDto.getStudentInfoFile(), "student_info");
        UploadFile profileImage = s3Uploader.upload(mentorFormDto.getProfileImageFile(), "profile_image");

        //멘토 정보 저장
        Mentor mentor = Mentor.createMentor(mentorFormDto, studentInfo, email);

        //프로필 사진 저장을 위해 유저 정보 가져온 후 프로필 사진 업데이트
        Member member = memberService.getMember(email);
        Member updatedMember = Member.signUpMentor(member, profileImage);

        //DB에 저장
        ArrayList<Object> objects = mentorService.createMentor(mentor, updatedMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(objects);
    }

    @GetMapping(value = "/manage/basic")
    public ResponseEntity<MentorManageBasicDto> mentorManageBasic(){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //사용자 mentor 얻어서 MentorManageBasicDto로 변환
        Mentor mentor = mentorService.getMentor(email);
        MentorManageBasicDto mentorManageBasicDto = MentorManageBasicDto.createMentorManageBasicDto(mentor);

        return ResponseEntity.status(HttpStatus.OK).body(mentorManageBasicDto);
    }

    @PostMapping(value = "/manage/basic")
    public ResponseEntity<Object> mentorManageBasic(@Valid MentorManageBasicDto mentorManageBasicDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //멘토 정보 업데이트
        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorBasic(mentor, mentorManageBasicDto);

        //멘토 정보 저장
        Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

        return ResponseEntity.status(HttpStatus.OK).body(updatedMentorInfo);
    }

    @GetMapping(value = "/manage/detail")
    public ResponseEntity<MentorManageDetailDto> mentorManageDetail(){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //사용자 mentor 얻어서 MentorManageDetailDto로 변환
        Mentor mentor = mentorService.getMentor(email);
        MentorManageDetailDto mentorManageDetailDto = MentorManageDetailDto.createMentorManageDetailDto(mentor);

        return ResponseEntity.status(HttpStatus.OK).body(mentorManageDetailDto);
    }

    @PostMapping(value = "/manage/detail")
    public ResponseEntity<Object> mentorManageDetail(@Valid MentorManageDetailDto mentorManageDetailDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //멘토 정보 업데이트
        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorDetail(mentor, mentorManageDetailDto);

        //멘토 정보 저장
        Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

        return ResponseEntity.status(HttpStatus.OK).body(updatedMentorInfo);
    }

    @GetMapping(value = "/manage/contact")
    public ResponseEntity<MentorManageContactDto> mentorManageContact(){

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //사용자 mentor 얻어서 MentorManageContactDto로 변환
        Mentor mentor = mentorService.getMentor(email);
        MentorManageContactDto mentorManageContactDto = MentorManageContactDto.createMentorManageContactDto(mentor);

        return ResponseEntity.status(HttpStatus.OK).body(mentorManageContactDto);
    }

    @PostMapping(value = "/manage/contact")
    public ResponseEntity<Object> mentorManageContact(@Valid MentorManageContactDto mentorManageContactDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //멘토 정보 업데이트
        Mentor mentor = mentorService.getMentor(email);
        Mentor updatedMentor = Mentor.updateMentorContact(mentor, mentorManageContactDto);

        //멘토 정보 저장
        Mentor updatedMentorInfo = mentorService.updateMentor(updatedMentor);

        return ResponseEntity.status(HttpStatus.OK).body(updatedMentorInfo);
    }
}
