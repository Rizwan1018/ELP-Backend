package com.elearning.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDate enrollmentDate;
    private Integer progress;
    private String status;
    // NEW:
    private Boolean watched;
    private Boolean done;
    private Integer rating;
    private Integer lastWatchedPosition;
}
