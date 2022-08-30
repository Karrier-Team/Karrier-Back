package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Follow;
import com.karrier.mentoring.key.FollowKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowKey> {

    List<Follow> findByMentorEmail(String mentorEmail);

    Follow  findByMemberEmailAndMentorEmail(String memberEmail, String mentorEmail);

    void deleteByMemberEmailAndMentorEmail(String memberEmail, String mentorEmail);

    List<Follow> findByMemberEmail(String memberEmail);
}
