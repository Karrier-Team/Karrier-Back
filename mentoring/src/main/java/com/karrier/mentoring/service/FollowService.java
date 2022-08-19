package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Follow;
import com.karrier.mentoring.entity.FollowInfo;
import com.karrier.mentoring.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    @Transactional
    public Follow createFollow(Follow follow){
        followRepository.save(follow);

        return follow;
    }

    @Transactional
    public void deleteFollow(String email, String mentorEmail){
        followRepository.deleteByMemberEmailAndMentorEmail(email, mentorEmail);
    }
}
