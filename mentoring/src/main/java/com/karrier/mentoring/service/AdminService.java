package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.MentorRoleDto;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Role;
import com.karrier.mentoring.repository.MemberRepository;
import com.karrier.mentoring.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    private final MentorRepository mentorRepository;

    public List<MentorRoleDto> MembersByRole(Role mentorRole){

        List<Member> memberList = memberRepository.findByRole(mentorRole);

        List<Mentor> mentorList = new ArrayList<>();

        for(Member member : memberList){
            mentorList.add(mentorRepository.findByEmail(member.getEmail()));
        }

        ArrayList<MentorRoleDto> mentorRoleDtoList = getMentorRoleDtoList(mentorList);

        return mentorRoleDtoList;
    }

    private ArrayList<MentorRoleDto> getMentorRoleDtoList(List<Mentor> mentorList){
        ArrayList<MentorRoleDto> mentorRoleDtoList = new ArrayList<>();

        for(Mentor mentor : mentorList){
            mentorRoleDtoList.add(MentorRoleDto.createMentorRoleDto(mentor));
        }
        return mentorRoleDtoList;
    }

    @Transactional
    public Member modifyMember(Member member) {
        return memberRepository.save(member);
    }
}
