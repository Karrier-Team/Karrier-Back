package com.karrier.mentoring.controller;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Role;
import com.karrier.mentoring.repository.MemberRepository;
import com.karrier.mentoring.repository.MentorRepository;
import com.karrier.mentoring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/admins")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final MentorRepository mentorRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping(value = "/controlMentors")
    public ResponseEntity<List<Member>> controlMentorsRole(){
        List<Member> members = memberRepository.findAllByRole(Role.MENTOR_WAIT);

        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    @PostMapping(value = "/controlMentors")
    public ResponseEntity<Member> controlMentorsRole(String email){
        Member member = memberRepository.findByEmail(email);

        Member updatedMember = Member.changeMentorRole(member);
        Member savedMember = memberService.modifyMember(updatedMember);

        return ResponseEntity.status(HttpStatus.OK).body(savedMember);
    }
}
