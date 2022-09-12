package com.karrier.mentoring.service;

import com.karrier.mentoring.dto.ProgramViewDto;
import com.karrier.mentoring.entity.Member;
import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.entity.ParticipationStudent;
import com.karrier.mentoring.entity.Program;
import com.karrier.mentoring.repository.MemberRepository;
import com.karrier.mentoring.repository.MentorRepository;
import com.karrier.mentoring.repository.ParticipationStudentRepository;
import com.karrier.mentoring.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationStudentService{
    private final ParticipationStudentRepository participationStudentRepository;

    private final ProgramRepository programRepository;

    private final MentorRepository mentorRepository;

    private final MemberRepository memberRepository;



    @Transactional
    public ParticipationStudent createParticipationStudent(ParticipationStudent participationStudent){
        return participationStudentRepository.save(participationStudent);
    }

    public List<ParticipationStudent> getParticipationStudentByEmail(String email){
        return participationStudentRepository.findByEmail(email);
    }

    public ParticipationStudent getParticipationStudentByEmailAndProgramNo(String email, long programNo){
        return participationStudentRepository.findByEmailAndProgramNo(email, programNo);
    }

    public List<ProgramViewDto> getParticipationProgramViewDto(List<ParticipationStudent> participationStudentList){

        List<ProgramViewDto> programViewDtoList = new ArrayList<>();

        for(ParticipationStudent participationStudent : participationStudentList){
            Program program = programRepository.findByProgramNo(participationStudent.getProgramNo());
            Mentor mentor = mentorRepository.findByEmail(program.getEmail());
            String name = mentor.getName();
            Member member = memberRepository.findByEmail(mentor.getEmail());
            String profileImage = member.getProfileImage().getStoreFileName();
            String major = mentor.getMajor();

            programViewDtoList.add(ProgramViewDto.createProgramViewDto(program, name, profileImage, major));
        }

        return programViewDtoList;
    }
}
