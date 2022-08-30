package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.repository.MemberRepository;
import com.karrier.mentoring.repository.MentorRepository;
import com.karrier.mentoring.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;

    private final MemberRepository memberRepository;

    private final ProgramRepository programRepository;

    //멘토 저장
    @Transactional
    public ArrayList<Object> createMentor(Mentor mentor, Member member) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.add(memberRepository.save(member));
        objects.add(mentorRepository.save(mentor));

        return objects;
    }

    //멘토 정보 수정
    @Transactional
    public Mentor updateMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    //멘토 정보 가져오기
    public Mentor getMentor(String email) {

        return mentorRepository.findByEmail(email);
    }

    //멘토 프로그램 정보 가져오기
    public List<Program> getProgramList(String email) {
        return programRepository.findByEmail(email);

    public List<Mentor> getEmailsByMajor(String major){
        return mentorRepository.findByMajor(major);
    }
}
