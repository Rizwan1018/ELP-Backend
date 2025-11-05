package com.elearning.backend.controller;

import com.elearning.backend.dto.EnrollmentDTO;
import com.elearning.backend.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "http://localhost:4200")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<EnrollmentDTO> getAll() {
        return enrollmentService.getEnrollmentsByStudent(null); // not used by FE; FE uses student/course-specific endpoints
    }

    @GetMapping(params = "studentId")
    public List<EnrollmentDTO> getByStudent(@RequestParam Long studentId) {
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }

    @GetMapping("/by-course")
    public List<EnrollmentDTO> getByCourse(@RequestParam Long courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }

    @GetMapping(params = "courseId")
    public List<EnrollmentDTO> getByCourseParam(@RequestParam Long courseId) {
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }


    @PostMapping
    public EnrollmentDTO enroll(@RequestBody EnrollmentDTO dto) {
        return enrollmentService.enroll(dto.getStudentId(), dto.getCourseId());
    }

    @PutMapping("/{id}/progress")
    public EnrollmentDTO updateProgress(@PathVariable Long id, @RequestBody Integer progress) {
        return enrollmentService.updateProgress(id, progress);
    }

    @PutMapping("/{id}/status")
    public EnrollmentDTO updateStatus(@PathVariable Long id, @RequestBody String status) {
        return enrollmentService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }

    @PutMapping("/{id}/watched")
    public EnrollmentDTO markWatched(@PathVariable Long id, @RequestBody(required = false) Integer lastWatchedPosition) {
        return enrollmentService.markWatched(id, lastWatchedPosition);
    }


    /**
     * Mark enrollment done (the user checked "Done"). Requires watched==true.
     */
    @PutMapping("/{id}/done")
    public EnrollmentDTO markDone(@PathVariable Long id) {
        return enrollmentService.markDone(id);
    }


    /**
     * Set rating (1..5). Requires done==true.
     * Body: {"rating": 5}
     */
    @PutMapping("/{id}/rating")
    public EnrollmentDTO setRating(@PathVariable Long id, @RequestBody Integer rating) {
        return enrollmentService.setRating(id, rating);
    }
}