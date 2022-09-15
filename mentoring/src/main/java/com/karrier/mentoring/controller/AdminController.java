package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.MentorRoleDto;
import com.karrier.mentoring.entity.Curriculum;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Role;
import com.karrier.mentoring.http.BasicResponse;
import com.karrier.mentoring.http.SuccessDataResponse;
import com.karrier.mentoring.http.SuccessResponse;
import com.karrier.mentoring.http.error.ErrorCode;
import com.karrier.mentoring.http.error.exception.BadRequestException;
import com.karrier.mentoring.http.error.exception.UnAuthorizedException;
import com.karrier.mentoring.repository.CurriculumRepository;
import com.karrier.mentoring.service.AdminService;
import com.karrier.mentoring.service.MemberService;
import com.karrier.mentoring.service.MentorService;
import com.karrier.mentoring.service.mail.MentorAcceptEmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin({"http://localhost:3000", "https://web-reactapp-48f224l75lf6ut.gksl1.cloudtype.app/"})
@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final AdminService adminService;
    private final MentorAcceptEmailServiceImpl mentorAcceptEmailService;

    // MENTOR_WAIT 상태인 멘토 정보 보여주기
    @GetMapping(value = "/control-mentors")
    public  ResponseEntity<? extends BasicResponse> controlMentorsRole(@RequestParam("mentorRole") Role mentorRole){

        //유저 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //접속중인 사람이 ADMIN 일때만 작동, mentorRole 따라서 다르게 보여줌
        if(memberService.getMember(email).getRole().equals(Role.ADMIN)){

            //해당 역할인 멤버들 찾기
            List<MentorRoleDto> mentorRoleDtoList = adminService.MembersByRole(mentorRole);

            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(mentorRoleDtoList));
        }
        //관리자가 아닌 사람이 접속할 때
        else{
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }

    // MENTOR_WAIT 상태인 멘토 한명 MENTOR_APPROVED 로 바꿔주기
    @PostMapping(value = "/control-mentors")
    public ResponseEntity<? extends BasicResponse> controlMentorRole(@RequestParam("mentorEmail") String mentorEmail){

        // 열할을 바꿔줄 member 찾기
        Member member = memberService.getMember(mentorEmail);

        //유저 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //접속중인 사람이 ADMIN 일때만 작동
        if(memberService.getMember(email).getRole().equals(Role.ADMIN)){
            if(member.getRole().equals(Role.MENTOR_WAIT)){

                //멤버 역할 바꿔주기
                Member updatedMember = Member.changeMentorRole(member);
                adminService.modifyMember(updatedMember);
                mentorAcceptEmailService.sendSimpleMessage(updatedMember);
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse());
            }
            //역할을 바꾸려는 멤버의 역할이 대기중이 아닐때
            else{
                throw new BadRequestException(ErrorCode.ROLE_MISMATCH_ERROR);
            }
        }
        //관리자가 아닌 사람이 접속할 때
        else{
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_USER);
        }
    }
}