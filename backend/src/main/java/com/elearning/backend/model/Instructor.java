package com.elearning.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "instructors")
public class Instructor {

    @Id
    private Long Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

///  @OneToMany(mappedBy = "instructor",cascade = CascadeType.ALL,orphanRemoval = true)
///    private List<Course> courses;



}
