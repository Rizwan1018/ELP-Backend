package com.elearning.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id","course_id"}))
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="student_id", nullable = false)
    private Long studentId;

    @Column(name="course_id", nullable = false)
    private Long courseId;

    @Column(name="enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(nullable = false)
    private Integer progress = 0; // 0-100

    @Column(nullable = false)
    private String status = "enrolled"; // enrolled, paused, completed
}
