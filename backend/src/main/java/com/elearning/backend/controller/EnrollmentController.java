package com.elearning.backend.controller;

import com.elearning.backend.dto.EnrollmentDTO;
import com.elearning.backend.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "http://localhost:4200")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    public EnrollmentController(EnrollmentService enrollmentService){
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<EnrollmentDTO> getAll() {
        return enrollmentService.getEnrollmentsByStudent(null); // not used by FE; FE uses student/course-specific endpoints
    }

    @GetMapping(params = "studentId")
    public List<EnrollmentDTO> getByStudent(@RequestParam Long studentId){
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    @GetMapping("/by-course")
    public List<EnrollmentDTO> getByCourse(@RequestParam Long courseId){
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }

    @GetMapping(params = "courseId")
    public List<EnrollmentDTO> getByCourseParam(@RequestParam Long courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }


    @PostMapping
    public EnrollmentDTO enroll(@RequestBody EnrollmentDTO dto){
        return enrollmentService.enroll(dto.getStudentId(), dto.getCourseId());
    }

    @PutMapping("/{id}/progress")
    public EnrollmentDTO updateProgress(@PathVariable Long id, @RequestBody Integer progress){
        return enrollmentService.updateProgress(id, progress);
    }

    @PutMapping("/{id}/status")
    public EnrollmentDTO updateStatus(@PathVariable Long id, @RequestBody String status){
        return enrollmentService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        enrollmentService.deleteEnrollment(id);
    }
}
