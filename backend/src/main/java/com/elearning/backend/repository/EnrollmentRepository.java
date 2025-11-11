package com.elearning.backend.repository;

import com.elearning.backend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // compute average rating for course (returns null if no ratings)
    @Query("SELECT AVG(e.rating) FROM Enrollment e WHERE e.courseId = :courseId AND e.rating IS NOT NULL")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);

    // optional: count of enrolled students for course
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.courseId = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);
}
