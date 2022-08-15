package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.repository.MemberRepository;
import com.karrier.mentoring.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public ArrayList<Object> createMentor(Mentor mentor, Member member) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(memberRepository.save(member));
        objects.add(mentorRepository.save(mentor));

        return objects;
    }

    @Transactional
    public Mentor updateMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public Mentor getMentor(String email) {

        return mentorRepository.findByEmail(email);
    }
}
