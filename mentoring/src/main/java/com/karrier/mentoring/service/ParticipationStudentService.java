package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.ParticipationStudent;
import com.karrier.mentoring.repository.ParticipationStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationStudentService {
    private final ParticipationStudentRepository participationStudentRepository;

    @Transactional
    public ParticipationStudent createParticipationStudent(ParticipationStudent participationStudent){
        participationStudentRepository.save(participationStudent);

        return participationStudent;
    }
}
