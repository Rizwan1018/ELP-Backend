package com.elearning.backend.controller;

import com.elearning.backend.entity.Instructor;
import com.elearning.backend.entity.Student;
import com.elearning.backend.repository.InstructorRepository;
import com.elearning.backend.repository.StudentRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CatalogController {
    private final StudentRepository studentRepo;
    private final InstructorRepository instructorRepo;
    public CatalogController(StudentRepository studentRepo, InstructorRepository instructorRepo){
        this.studentRepo = studentRepo;
        this.instructorRepo = instructorRepo;
    }

    @GetMapping("/students")
    public List<Student> listStudents(){
        return studentRepo.findAll();
    }

    @GetMapping("/instructors")
    public List<Instructor> listInstructors(){
        return instructorRepo.findAll();
    }
}
