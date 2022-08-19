package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Follow;
import com.karrier.mentoring.entity.FollowInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowInfo> {

    List<String> findAllByMentorEmail(Long MentorEmail);

    Follow  findByMemberEmailAndMentorEmail(String memberEmail, String mentorEmail);

    void deleteByMemberEmailAndMentorEmail(String memberEmail, String mentorEmail);
}
