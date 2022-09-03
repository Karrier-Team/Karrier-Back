package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.*;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.entity.UploadFile;
import com.karrier.mentoring.service.*;
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
import java.util.List;

@CrossOrigin("http://localhost:3000")
@RequestMapping("/mentors")
@RestController
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    private final MemberService memberService;

    private final CommunityQuestionService communityQuestionService;

    private final CommunityReviewService communityReviewService;

    private final S3Uploader s3Uploader;

    //멘토 회원가입 요청시
    @PostMapping(value = "/new")
    public ResponseEntity<Object> mentorForm(@Valid MentorFormDto mentorFormDto, BindingResult bindingResult, Model model) throws IOException {

        //필수입력 값을 입력하지 않은 경우
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

        //유저 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //S3 스토리지에 파일 저장 후 파일 이름 반환
        UploadFile studentInfo = s3Uploader.upload(mentorFormDto.getStudentInfoFile(), "student-info");
        UploadFile profileImage = s3Uploader.upload(mentorFormDto.getProfileImageFile(), "profile-image");

        //멘토 정보 저장
        Mentor mentor = Mentor.createMentor(mentorFormDto, studentInfo, email);

        //프로필 사진 저장을 위해 유저 정보 가져온 후 프로필 사진 업데이트
        Member member = memberService.getMember(email);
        Member updatedMember = Member.signUpMentor(member, profileImage);

        //DB에 저장
        ArrayList<Object> objects = mentorService.createMentor(mentor, updatedMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(objects);
    }

    //멘토관리 - 기본정보 화면 띄울 때 이전 입력 정보 보여주기 위해
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

    //멘토관리 - 기본정보 변경 요청시
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

    //멘토관리 - 상세정보 화면 띄울 때 이전 입력 정보 보여주기 위해
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

    //멘토관리 - 상세정보 변경 요청시
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

    //멘토관리 - 연락 정보 화면 띄울 때 이전 입력 정보 보여주기 위해
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

    //멘토관리 - 연락 정보 변경 요청시
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

    //나의 프로그램 전체 질문 리스트 띄우기
    @GetMapping("/manage/question")
    public ResponseEntity<Object> questionList() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<Program> programList = mentorService.getProgramList(email);

        if (programList.size() == 0) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no program");
        }
        List<QuestionListDto> questionList = new ArrayList<>();
        for (Program program : programList) { // 해당 멘토의 모든 프로그램의 모든 질문 정보 가져오기
            List<QuestionListDto> questionList1 = communityQuestionService.findQuestionList(program.getProgramNo());
            if (questionList1 != null) {
                questionList.addAll(questionList1);
            }
        }
        if (questionList.size() == 0) {//해당 프로그램에 해당하는 질문이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no question");
        }

        return ResponseEntity.status(HttpStatus.OK).body(questionList);
    }

    //나의 프로그램 전체 리뷰 리스트에서 검색할 경우 (질문제목, 질문내용, 닉네임)
    @GetMapping("/manage/question/search")
    public ResponseEntity<Object> questionList(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {

        if (category.isEmpty() || keyword.isEmpty()) { //빈칸있을 때
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<Program> programList = mentorService.getProgramList(email);
        if (programList.size() == 0) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no program");
        }
        List<QuestionListDto> questionList = new ArrayList<>();
        for (Program program : programList) { // 해당멘토의 모든 프로그램의 검색조건에 부합하는 질문 정보 가져오기
            List<QuestionListDto> questionList1 = communityQuestionService.QuestionSearchList(program.getProgramNo(), category, keyword);
            if (questionList1 != null) {
                questionList.addAll(questionList1);
            }
        }
        if (questionList.size() == 0) {//해당 프로그램에 해당하는 질문이 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no question");
        }
        return ResponseEntity.status(HttpStatus.OK).body(questionList);
    }

    //나의 프로그램 전체 리뷰 리스트 띄우기
    @GetMapping("/manage/review")
    public ResponseEntity<Object> reviewList() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<Program> programList = mentorService.getProgramList(email);
        if (programList.size() == 0) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no program");
        }
        List<ReviewListDto> reviewList = new ArrayList<>();
        for (Program program : programList) { // 해당 멘토의 모든 프로그램의 모든 질문 정보 가져오기
            List<ReviewListDto> reviewList1 = communityReviewService.findReviewList(program.getProgramNo());
            if (reviewList1 != null) {
                reviewList.addAll(reviewList1);
            }
        }
        if (reviewList.size() == 0) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no review");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewList);
    }

    //나의 프로그램 전체 리뷰 리스트에서 검색할 경우 (후기제목, 후기내용, 닉네임)
    @GetMapping("/manage/review/search")
    public ResponseEntity<Object> reviewList(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {

        if (category.isEmpty() || keyword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        List<Program> programList = mentorService.getProgramList(email);
        if (programList.size() == 0) {//해당 프로그램에 해당하는 데이터가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no program");
        }
        List<ReviewListDto> reviewList = new ArrayList<>();
        for (Program program : programList) { // 해당멘토의 모든 프로그램의 검색조건에 부합하는 질문 정보 가져오기
            List<ReviewListDto> reviewList1 = communityReviewService.ReviewSearchList(program.getProgramNo(), category, keyword);
            if (reviewList1 != null) {
                reviewList.addAll(reviewList1);
            }
        }
        if (reviewList.size() == 0) {//나의 프로그램의 리뷰가 없을 때
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no review");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewList);
    }
}
