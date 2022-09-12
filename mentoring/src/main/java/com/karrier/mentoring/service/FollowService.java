package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.FollowShowDto;
import com.karrier.mentoring.entity.Follow;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.repository.FollowRepository;
import com.karrier.mentoring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Follow createFollow(Follow follow) {
        return followRepository.save(follow);
    }

    @Transactional
    public void deleteFollow(String email, String mentorEmail) {
        followRepository.deleteByMemberEmailAndMentorEmail(email, mentorEmail);
    }

    public Follow getFollow(String memberEmail, String mentorEmail) {
        return followRepository.findByMemberEmailAndMentorEmail(memberEmail, mentorEmail);
    }

    public List<Follow> getFollowers(String mentorEmail) {
        return followRepository.findByMentorEmail(mentorEmail);
    }

    public List<Follow> getFollowings(String memberEmail) {
        return followRepository.findByMemberEmail(memberEmail);
    }

    public List<FollowShowDto> getFollowingDtoList(List<Mentor> mentors, String searchType, String searchWord) {
        List<Mentor> mentorList = new ArrayList<>();

        if (searchType.equals("멘토이름")) {
            if (searchWord == null) {
                mentorList = mentors;
            } else {
                for (Mentor mentor : mentors) {
                    if (mentor.getName().contains(searchWord)) {
                        mentorList.add(mentor);
                    }
                }
            }
        } else if (searchType.equals("학과")) {
            if (searchWord == null) {
                mentorList = mentors;
            } else {
                for (Mentor mentor : mentors) {
                    if (mentor.getMajor().contains(searchWord)) {
                        mentorList.add(mentor);
                    }
                }
            }
        }

        ArrayList<FollowShowDto> followShowDtoArrayList = getFollowShowDtoList(mentorList);


        return followShowDtoArrayList;
    }

    public ArrayList<FollowShowDto> getFollowShowDtoList(List<Mentor> mentors) {
        ArrayList<FollowShowDto> followShowDtoArrayList = new ArrayList<>();

        for (Mentor mentor : mentors) {
            String profileImage = memberRepository.findByEmail(mentor.getEmail()).getProfileImage().getStoreFileName();
            followShowDtoArrayList.add(FollowShowDto.createFollowShowDto(mentor, profileImage));
        }

        return followShowDtoArrayList;

    }
}
