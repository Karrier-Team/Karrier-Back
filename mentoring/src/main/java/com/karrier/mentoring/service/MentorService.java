package com.karrier.mentoring.service;

import com.karrier.mentoring.entity.Mentor;
import com.karrier.mentoring.repository.MentorInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MentorService {

    private final MentorInfoRepository mentorInfoRepository;

    public Mentor saveMentor(Mentor mentor) {
        return mentorInfoRepository.save(mentor);

    }
}
