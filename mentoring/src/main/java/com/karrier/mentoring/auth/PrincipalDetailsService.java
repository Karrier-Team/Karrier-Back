package com.karrier.mentoring.auth;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 해당 어노테이션을 통해 PrincipalDetailService 클래스를 IoC에 등록시킴
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;


    // Security seesion 안에있는 Authentication 타입객체의 안에 UserDetails 타입객체가 있다.
    // Security session(내부 Authentication(내부 UserDetails))
    // 아래의 함수는 UserDetails를 구현한 PrincipalDetails를 return한다
    @Override
    public UserDetails loadUserByUsername(String email) { // username == email
        Member member = memberRepository.findByEmail(email);
        if(member != null){
            return new PrincipalDetails(member);
        }
        else {
            throw new UsernameNotFoundException(email);
        }
    }
}