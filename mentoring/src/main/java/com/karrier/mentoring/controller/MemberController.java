package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.MemberFormDto;
import com.karrier.mentoring.dto.MemberManagePasswordDto;
import com.karrier.mentoring.dto.MemberPasswordDto;
import com.karrier.mentoring.dto.MemberWithoutPasswordDto;
import com.karrier.mentoring.entity.*;
import com.karrier.mentoring.http.BasicResponse;
import com.karrier.mentoring.http.SuccessDataResponse;
import com.karrier.mentoring.http.SuccessResponse;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.*;
import com.karrier.mentoring.repository.*;
import com.karrier.mentoring.service.MemberService;
import com.karrier.mentoring.service.S3Uploader;
import com.karrier.mentoring.service.mail.EmailService;
import com.karrier.mentoring.service.mail.PwdCertEmailServiceImpl;
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

@CrossOrigin("http://localhost:3000")
@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final S3Uploader s3Uploader;

    public static final String profileImageBaseUrl = "https://karrier.s3.ap-northeast-2.amazonaws.com/profile-image/";

    private final PwdCertEmailServiceImpl pwdCertEmailService;

    //회원가입 요청시
    @PostMapping(value = "/new")
    public ResponseEntity<? extends BasicResponse> memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult) {

        //빈칸 있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }

        //비밀번호와 비밀번호 확인이 일치하지 않을 경우
        if (!memberFormDto.getPassword().equals(memberFormDto.getPasswordCheck())) {
            throw new BadRequestException(ErrorCode.PASSWORD_CHECK_MISMATCH);
        }

        //중복된 닉네임일 경우
        if (memberService.checkDuplicateNickName(memberFormDto.getNickname())) {
            throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
        }

        try { // member 형태로 변환 후 데이터베이스에 member 정보 저장
            Member member = Member.createMember(memberFormDto, passwordEncoder);

            //비밀번호를 제외한 member 정보 반환
            MemberWithoutPasswordDto memberWithoutPasswordDto = MemberWithoutPasswordDto.createMemberWithoutPasswordDto(memberService.saveMember(member));
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(memberWithoutPasswordDto));

        } catch (IllegalStateException e) { //이미 가입된 이메일일 경우

            throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
        }
    }


    //로그인시 아이디 패스워드가 틀릴경우
    @GetMapping(value = "/login/error") 

    public ResponseEntity<? extends BasicResponse> loginError() {
        throw new BadRequestException(ErrorCode.ACCOUNT_MISMATCH);
    }

    //유저가 로그인시 로그인 시간 저장
    @GetMapping(value = "/update-login/{email}")
    public ResponseEntity<? extends BasicResponse> updateLoginTime(@PathVariable("email") String email) {

        //로그인 한 회원 정보 찾기
        Member member = memberRepository.findByEmail(email);
        
        //로그인 날짜 업데이트 후 저장
        Member.updateRecentlyLoginDate(member);
        
        //비밀번호를 제외한 멤버 정보 전달
        MemberWithoutPasswordDto memberWithoutPasswordDto = MemberWithoutPasswordDto.createMemberWithoutPasswordDto(memberService.modifyMember(member));

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(memberWithoutPasswordDto));
    }

    //비밀번호 변경 요청시
    @PutMapping(value = "/manage/password")
    public ResponseEntity<? extends BasicResponse> mentorManagePassword(@Valid MemberManagePasswordDto memberManagePasswordDto, BindingResult bindingResult) {

        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
        
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //DB에 저장된 패스워드 가져와서 사용자가 입력한 패스워드와 일치 확인
        Member member = memberService.getMember(email);
        if (!passwordEncoder.matches(memberManagePasswordDto.getOldPassword(), member.getPassword())) {
            throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
        }

        //새 비밀번호와 비밀번호 확인 일치 체크
        if (!memberManagePasswordDto.getNewPassword().equals(memberManagePasswordDto.getPasswordCheck())) {
            throw new BadRequestException(ErrorCode.PASSWORD_CHECK_MISMATCH);
        }

        //비밀번호 변경 후 저장
        Member.updatePassword(member, memberManagePasswordDto, passwordEncoder);
        MemberWithoutPasswordDto memberWithoutPasswordDto = MemberWithoutPasswordDto.createMemberWithoutPasswordDto(memberService.modifyMember(member));

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(memberWithoutPasswordDto));
    }

    //프로필 변경 화면 띄웠을 경우 이전 프로필 사진 보여주기 위해
    @GetMapping(value = "/manage/profile-image")
    public ResponseEntity<? extends BasicResponse> modifyProfile() {

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //해당 member 정보에서 S3에 저장된 파일 이름 가져와서 url 전송
        Member member = memberRepository.findByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(member.getProfileImage()));
    }

    //프로필 이미지 변경 요청시
    @PutMapping(value = "/manage/profile-image")
    public ResponseEntity<? extends BasicResponse> modifyProfileImage(@RequestParam MultipartFile profileImageFile) throws IOException {

        //프로필 사진이 없을 때
        if (profileImageFile.isEmpty()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //프로필 사진 저장을 위해 유저 정보 가져오기
        Member member = memberService.getMember(email);

        //S3 스토리지에 이전 파일 삭제 후 새로운 파일 저장, 저장된 파일 이름 반환
        UploadFile profileImage = s3Uploader.modifyProfileImage(profileImageFile, "profile-image", member.getProfileImage().getStoreFileName());


        //member 프로필 사진 정보 수정
        Member.modifyProfile(member, profileImage);

        //DB에 저장
        MemberWithoutPasswordDto memberWithoutPasswordDto = MemberWithoutPasswordDto.createMemberWithoutPasswordDto(memberService.modifyMember(member));

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(memberWithoutPasswordDto));
    }

    @GetMapping(value = "/manage/nickname")
    public ResponseEntity<? extends BasicResponse> checkNicknameDuplicate(@RequestParam String nickname) {

        //중복된 닉네임일 경우
        if (memberService.checkDuplicateNickName(nickname)) {
            throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
        }
        //중복이 아닐 경우
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(nickname));
    }

    //닉네임 변경 요청시
    @PutMapping(value = "/manage/nickname")
    public ResponseEntity<? extends BasicResponse> modifyNickname(@RequestParam String nickname) throws IOException {

        //닉네임이 없을 때
        if (nickname.isEmpty()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }

        //중복된 닉네임일 경우
        if (memberService.checkDuplicateNickName(nickname)) {
            throw new ConflictException(ErrorCode.DUPLICATE_NICKNAME);
        }

        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //닉네임 변경을 위해 유저 정보 가져오기
        Member member = memberService.getMember(email);

        //member 프로필 사진 이름 정보 수정
        Member.modifyNickName(member, nickname);

        //DB에 저장
        MemberWithoutPasswordDto memberWithoutPasswordDto = MemberWithoutPasswordDto.createMemberWithoutPasswordDto(memberService.modifyMember(member));

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(memberWithoutPasswordDto));
    }

    @DeleteMapping(value = "/manage")
    public ResponseEntity<? extends BasicResponse> deleteMember(@RequestParam String email) {

        Member byEmail = memberRepository.findByEmail(email);
        if (byEmail == null) { //해당 이메일의 회원정보를 찾을 수 없을 때
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        //사용자 email 얻기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String myEmail = ((UserDetails) principal).getUsername();

        if (!myEmail.equals(email)) { //삭제하려는 계정과 로그인된 계정이 다를 경우
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
        memberService.deleteMember(byEmail);

        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @PostMapping(value = "/change/password")
    public ResponseEntity<? extends BasicResponse> sendPasswordChangeTokenEmail(@RequestParam(required = true) String email) throws Exception {
        pwdCertEmailService.sendSimpleMessage(email);
        return ResponseEntity.ok().body(new SuccessResponse());
    }

    @PutMapping(value = "/change/password")
    public ResponseEntity<? extends BasicResponse> changePasswordWithToken(@Valid MemberPasswordDto memberPasswordDto, BindingResult bindingResult) {
        //빈칸있을 경우
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(ErrorCode.BLANK_FORM);
        }
       memberService.changePasswordWithToken(memberPasswordDto,passwordEncoder);
        return ResponseEntity.ok().body(new SuccessResponse());
}

    @PostMapping(value = "/verify/password/token")
    public ResponseEntity<? extends BasicResponse> verifyEmail(@RequestParam(required = true) String token){
        pwdCertEmailService.verifyEmail(token);
        return ResponseEntity.ok().body(new SuccessResponse());
    }




}