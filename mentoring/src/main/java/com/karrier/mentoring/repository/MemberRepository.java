package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String>{

    Member findByEmail(String email);

    Member findByNickname(String nickname);
}