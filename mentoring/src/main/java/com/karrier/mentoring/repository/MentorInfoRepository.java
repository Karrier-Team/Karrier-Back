package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorInfoRepository extends JpaRepository<Mentor, String> {

}
