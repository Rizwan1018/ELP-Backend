package com.elearning.backend.repository;



import com.elearning.backend.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
}
