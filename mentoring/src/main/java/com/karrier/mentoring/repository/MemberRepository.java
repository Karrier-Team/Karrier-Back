package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String>{

    Member findByEmail(String email);

    Member findByNickname(String nickname);

    List<Member> findAllByRole(Role role);

    List<Member> findAllByEmailIn(List<String> emails);
}