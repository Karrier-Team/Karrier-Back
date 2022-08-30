package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.ReviewListDto;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Role;
import com.karrier.mentoring.entity.Review;
import com.karrier.mentoring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //멤버 저장
    @Transactional
    public Member saveMember(Member member) {

        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //멤버 수정
    @Transactional
    public Member modifyMember(Member member) {
        return memberRepository.save(member);
    }

    //멤버 정보 가져오기
    public Member getMember(String email) {
        return memberRepository.findByEmail(email);
    }

    //닉네임 중복 체크
    public boolean checkDuplicateNickName(String nickname) {

        Member findMember = memberRepository.findByNickname(nickname);
        //이미 존재하는 닉네임일 경우
        if (findMember != null) {
            return true;
        }
        //중복이 아닐 경우
        return false;
    }

    //이메일 중복 체크
    private void validateDuplicateMember(Member member) {

        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
