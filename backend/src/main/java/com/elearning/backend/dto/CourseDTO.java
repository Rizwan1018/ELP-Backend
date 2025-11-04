package com.elearning.backend.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private Long instructorId;
    private String domain;
    private String level;
    private Integer durationHrs;
    private String tags;
    private String description;
    private String thumbnail;
    private String videoUrl;
    private String preRequisite;
    private boolean enrolled = false;

    // NEW:
    private Double avgRating;
    private Long studentsCount;
}
