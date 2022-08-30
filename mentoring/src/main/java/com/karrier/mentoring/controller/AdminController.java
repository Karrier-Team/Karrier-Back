package com.karrier.mentoring.controller;

import com.karrier.mentoring.dto.MentorRoleDto;
import com.karrier.mentoring.entity.Curriculum;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Role;
import com.karrier.mentoring.repository.CurriculumRepository;
import com.karrier.mentoring.service.AdminService;
import com.karrier.mentoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final AdminService adminService;
    private final CurriculumRepository curriculumRepository;

    // MENTOR_WAIT 상태인 멘토 정보 보여주기
    @GetMapping(value = "/control-mentors")
    public ResponseEntity<Object> controlMentorsRole(@RequestParam("mentorRole") Role mentorRole){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //접속중인 사람이 ADMIN 일때만 작동, mentorRole 따라서 다르게 보여줌
        if(memberService.getMember(email).getRole().equals(Role.ADMIN)){
            List<MentorRoleDto> mentorRoleDtoList = adminService.MembersByRole(mentorRole);

            return ResponseEntity.status(HttpStatus.OK).body(mentorRoleDtoList);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not admin");
        }
    }

    // MENTOR_WAIT 상태인 멘토 한명 MENTOR_APPROVED 로 바꿔주기
    @PostMapping(value = "/control-mentors")
    public ResponseEntity<Object> controlMentorRole(@RequestParam("mentorEmail") String mentorEmail){
        Member member = memberService.getMember(mentorEmail);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        //접속중인 사람이 ADMIN 일때만 작동
        if(memberService.getMember(email).getRole().equals(Role.ADMIN)){
            if(member.getRole().equals(Role.MENTOR_WAIT)){
                Member updatedMember = Member.changeMentorRole(member);
                Member savedMember = adminService.modifyMember(updatedMember);

                return ResponseEntity.status(HttpStatus.OK).body(savedMember);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not admin");
        }
    }
}
