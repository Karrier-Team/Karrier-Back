package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.MemberFormDto;
import com.karrier.mentoring.dto.MemberManagePasswordDto;
import com.karrier.mentoring.dto.ParticipationStudentFormDto;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.repository.*;
import com.karrier.mentoring.service.FollowService;
import com.karrier.mentoring.service.MemberService;
import com.karrier.mentoring.service.ParticipationStudentService;
import com.karrier.mentoring.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final FollowService followService;

    private final ParticipationStudentService participationStudentService;

    private final ParticipationStudentRepository participationStudentRepository;

    private final FollowRepository followRepository;

    private final MemberRepository memberRepository;

    private final MentorRepository mentorRepository;

    private final ProgramRepository programRepository;

    private final PasswordEncoder passwordEncoder;

    private final S3Uploader s3Uploader;

    String profileImageBaseUrl = "https://karrier.s3.ap-northeast-2.amazonaws.com/profile_image/";

    @PostMapping(value = "/new")
    public ResponseEntity<Object> memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        if (!memberFormDto.getPassword().equals(memberFormDto.getPasswordCheck())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("password check error");
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            Member newMember = memberService.saveMember(member);

            return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("duplicate email");
        }
    }

    @GetMapping(value = "/login/error")
    public ResponseEntity<String> loginError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("wrong id or password");
    }

    @GetMapping(value = "/update-login/{email}")
    public ResponseEntity<Member> updateLoginTime(@PathVariable("email") String email) {
        Member member = memberRepository.findByEmail(email);
        Member updatedMember = Member.updateRecentlyLoginDate(member);
        Member savedMember = memberService.modifyMember(updatedMember);
        return ResponseEntity.status(HttpStatus.OK).body(savedMember);
    }

    @PostMapping(value = "/manage/password")
    public ResponseEntity<Object> mentorManagePassword(@Valid MemberManagePasswordDto memberManagePasswordDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }
        
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //DB에 저장된 패스워드 가져와서 사용자가 입력한 패스워드와 일치 확인
        Member member = memberService.getMember(email);
        if (!passwordEncoder.matches(memberManagePasswordDto.getOldPassword(), member.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("current password match error");
        }

        //새 비밀번호와 비밀번호 확인 일치 체크
        if (!memberManagePasswordDto.getNewPassword().equals(memberManagePasswordDto.getPasswordCheck())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("password check error");
        }

        //비밀번호 변경 후 저장
        Member updatedMember = Member.updatePassword(member, memberManagePasswordDto, passwordEncoder);
        Member savedMember = memberService.modifyMember(updatedMember);

        return ResponseEntity.status(HttpStatus.OK).body(savedMember);
    }

    @GetMapping(value = "/manage/profile")
    public ResponseEntity<String> modifyProfile() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        Member member = memberRepository.findByEmail(email);
        String profileImageUrl = profileImageBaseUrl + member.getProfileImage().getStoreFileName();

        return ResponseEntity.status(HttpStatus.OK).body(profileImageUrl);
    }

    @PostMapping(value = "/manage/profile")
    public ResponseEntity<Object> modifyProfile(@RequestParam MultipartFile profileImageFile, @RequestParam String nickname) throws IOException {

        //프로필 사진이 없을 때
        if (profileImageFile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("profile image empty error");
        }

        //프로필 사진이 없을 때
        if (nickname.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("nickname empty error");
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //프로필 사진 저장을 위해 유저 정보 가져오기
        Member member = memberService.getMember(email);

        //S3 스토리지에 이전 파일 삭제 후 새로운 파일 저장, 저장된 파일 이름 반환
        UploadFile profileImage = s3Uploader.modifyProfileImage(profileImageFile, "profile_image", member.getProfileImage().getStoreFileName());

        //member 프로필 사진 정보 수정
        Member updatedMember = Member.modifyProfile(member, profileImage, nickname);

        //DB에 저장
        Member savedMember = memberService.modifyMember(updatedMember);

        return ResponseEntity.status(HttpStatus.OK).body(savedMember);
    }

    @PostMapping(value = "/manage/nickname")
    public ResponseEntity<String> modifyProfile(@RequestParam String nickname) {

        //중복된 닉네임일 경우
        if (memberService.checkDuplicateNickName(nickname)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("duplicate nickname");
        }
        //중복이 아닐 경우
        return ResponseEntity.status(HttpStatus.OK).body(nickname);
    }

    @GetMapping(value = "/viewPrograms/{major}")
    public ResponseEntity<List<Program>> viewMajorPrograms(@PathVariable("major") String major){
        List<String> emails = mentorRepository.findEmailByMajor(major);
        List<Program> programs = programRepository.findAllByEmailInOrderByLikeCount(emails);

        return ResponseEntity.status(HttpStatus.OK).body(programs);
    }

    @GetMapping(value = "/viewPrograms/{programNo}")
    public ResponseEntity<Object> viewProgram(@PathVariable("programNo") Long programNo){
        Program program = programRepository.findByProgramNo(programNo);
        Mentor mentor = mentorRepository.findByEmail(program.getEmail());
        List<String> emails = participationStudentRepository.findAllByProgramNo(programNo);
        List<Member> members = memberRepository.findAllByEmailIn(emails);
        //질의 응답과 수강 후기 추가해야 함

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(program);
        objects.add(mentor);
        objects.add(members);

        return ResponseEntity.status(HttpStatus.OK).body(objects);
    }

    @PostMapping(value = "/viewPrograms/{programNo}/participate")
    public ResponseEntity<Object> participateProgram(@PathVariable("programNo") Long programNo, @Valid ParticipationStudentFormDto participationStudentFormDto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("blank error");
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        ParticipationStudent participationStudent = ParticipationStudent.createParticipationStudent(participationStudentFormDto, email, programNo);

        ParticipationStudent newParticipationStudent = participationStudentService.createParticipationStudent(participationStudent);

        return ResponseEntity.status(HttpStatus.CREATED).body(newParticipationStudent);
    }

    @PostMapping(value = "/viewPrograms/{programNo}/follow")
    public ResponseEntity<Follow> followMentor(@PathVariable("programNo") Long programNo){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        String mentorEmail = programRepository.findByProgramNo(programNo).getEmail();
        Mentor mentor = mentorRepository.findByEmail(mentorEmail);


        if(followRepository. findByMemberEmailAndMentorEmail(email, mentorEmail).equals(null)){
            Follow follow = Follow.createFollow(email, mentorEmail);

            Follow newFollow = followService.createFollow(follow);

            mentor.setFollowNo(mentor.getFollowNo()+1);

            return ResponseEntity.status(HttpStatus.CREATED).body(newFollow);
        }
        else{
            followService.deleteFollow(email, mentorEmail);
            mentor.setFollowNo(mentor.getFollowNo()-1);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
}