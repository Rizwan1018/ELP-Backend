
package com.elearning.backend.repository;

import com.elearning.backend.model.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {
    Optional<AssessmentAttempt> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    List<AssessmentAttempt> findAllByStudentIdOrderByAttemptedAtDesc(Long studentId);

    List<AssessmentAttempt> findAllByAssessmentIdOrderByAttemptedAtDesc(Long assessmentId);
}
