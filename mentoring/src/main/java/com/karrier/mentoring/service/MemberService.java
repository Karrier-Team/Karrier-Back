package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member saveMember(Member member) {

        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    @Transactional
    public Member modifyMember(Member member) {
        return memberRepository.save(member);
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email);
    }

    public boolean checkDuplicateNickName(String nickname) {

        Member findMember = memberRepository.findByNickname(nickname);
        //이미 존재하는 닉네임일 경우
        if (findMember != null) {
            return true;
        }
        //중복이 아닐 경우
        return false;
    }

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
