package com.karrier.mentoring.repository;

import com.karrier.mentoring.entity.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, String> {
    Mentor findByEmail(String email);

    long deleteByEmail(String email);
    
    List<Mentor> findByDepartment(String department);
}
