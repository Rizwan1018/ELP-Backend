package com.elearning.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;
    private String domain;
    private String level;
    private Integer durationHrs;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;

    @Column(length = 1000)
    private String description;
    private String thumbnail;
    private String videoUrl;


}
