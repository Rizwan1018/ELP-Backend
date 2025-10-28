package com.elearning.backend.service;

import com.elearning.backend.dto.EnrollmentDTO;
import com.elearning.backend.model.Enrollment;
import com.elearning.backend.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    public EnrollmentService(EnrollmentRepository enrollmentRepository){
        this.enrollmentRepository = enrollmentRepository;
    }

    private EnrollmentDTO toDto(Enrollment e){
        EnrollmentDTO d = new EnrollmentDTO();
        d.setId(e.getId());
        d.setStudentId(e.getStudentId());
        d.setCourseId(e.getCourseId());
        d.setEnrollmentDate(e.getEnrollmentDate());
        d.setProgress(e.getProgress());
        d.setStatus(e.getStatus());
        return d;
    }

    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId){
        return enrollmentRepository.findByStudentId(studentId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getEnrollmentsByCourse(Long courseId){
        return enrollmentRepository.findByCourseId(courseId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public EnrollmentDTO enroll(Long studentId, Long courseId){
        // return existing if present
        var existing = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if(existing.isPresent()){
            return toDto(existing.get());
        }
        Enrollment e = new Enrollment();
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setEnrollmentDate(LocalDate.now());
        e.setProgress(0);
        e.setStatus("enrolled");
        Enrollment saved = enrollmentRepository.save(e);
        return toDto(saved);
    }

    public EnrollmentDTO updateProgress(Long id, Integer progress){
        var opt = enrollmentRepository.findById(id);
        if(opt.isEmpty()) return null;
        Enrollment e = opt.get();
        e.setProgress(progress);
        if(progress != null && progress >= 100){
            e.setStatus("completed");
        }
        return toDto(enrollmentRepository.save(e));
    }

    public EnrollmentDTO updateStatus(Long id, String status){
        var opt = enrollmentRepository.findById(id);
        if(opt.isEmpty()) return null;
        Enrollment e = opt.get();
        e.setStatus(status);
        return toDto(enrollmentRepository.save(e));
    }

    public void deleteEnrollment(Long id){
        enrollmentRepository.deleteById(id);
    }
}
