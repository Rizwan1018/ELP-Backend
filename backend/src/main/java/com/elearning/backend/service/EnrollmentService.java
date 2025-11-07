package com.elearning.backend.service;

import com.elearning.backend.dto.EnrollmentDTO;
import com.elearning.backend.model.Enrollment;
import com.elearning.backend.repository.CourseRepository;
import com.elearning.backend.repository.EnrollmentRepository;
import com.elearning.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final NotificationRepository notificationRepository;
    private final CourseRepository courseRepository;


    public EnrollmentService(EnrollmentRepository enrollmentRepository, NotificationRepository notificationRepository,
                             CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.notificationRepository = notificationRepository;
        this.courseRepository = courseRepository;

    }

    private EnrollmentDTO toDto(Enrollment e) {
        EnrollmentDTO d = new EnrollmentDTO();
        d.setId(e.getId());
        d.setStudentId(e.getStudentId());
        d.setCourseId(e.getCourseId());
        d.setEnrollmentDate(e.getEnrollmentDate());
        d.setProgress(e.getProgress());
        d.setStatus(e.getStatus());
        // new fields:
        d.setWatched(e.getWatched());
        d.setDone(e.getDone());
        d.setRating(e.getRating());
        d.setLastWatchedPosition(e.getLastWatchedPosition());
        return d;


    }

    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public EnrollmentDTO enroll(Long studentId, Long courseId) {
        var existing = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existing.isPresent()) {
            // Don't duplicate notification for already-enrolled
            return toDto(existing.get());
        }
        Enrollment e = new Enrollment();
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setEnrollmentDate(LocalDate.now());
        e.setProgress(0);
        e.setStatus("enrolled");
        Enrollment saved = enrollmentRepository.save(e);

        // create notification to the student
        String title = courseRepository.findById(courseId)
                .map(c -> c.getTitle()).orElse("the course");
        com.elearning.backend.model.Notification n = new com.elearning.backend.model.Notification();
        n.setUserId(studentId);
        n.setType("enrollment");
        n.setCourseId(courseId);
        n.setMessage("You have successfully enrolled in \"" + title + "\".");
        notificationRepository.save(n);

        return toDto(saved);
    }


    public EnrollmentDTO updateProgress(Long id, Integer progress) {
        var opt = enrollmentRepository.findById(id);
        if (opt.isEmpty()) return null;
        Enrollment e = opt.get();
        e.setProgress(progress);
        if (progress != null && progress >= 100) {
            e.setStatus("completed");
        }
        return toDto(enrollmentRepository.save(e));
    }

    public EnrollmentDTO updateStatus(Long id, String status) {
        var opt = enrollmentRepository.findById(id);
        if (opt.isEmpty()) return null;
        Enrollment e = opt.get();
        e.setStatus(status);
        return toDto(enrollmentRepository.save(e));
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

// ---------- NEW API ACTIONS ----------


    @Transactional
    public EnrollmentDTO markWatched(Long enrollmentId, Integer lastWatchedPosition) {
        Enrollment e = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        e.setWatched(true);
        if (lastWatchedPosition != null) e.setLastWatchedPosition(lastWatchedPosition);
        Enrollment saved = enrollmentRepository.save(e);
        return toDto(saved);
    }


    @Transactional
    public EnrollmentDTO markDone(Long enrollmentId) {
        Enrollment e = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));


        // require watched for now
        if (!Boolean.TRUE.equals(e.getWatched())) {
            throw new IllegalStateException("Cannot mark done before watching the course");
        }


        e.setDone(true);
        e.setStatus("completed");
        // ensure progress reflects completion
        e.setProgress(100);


        Enrollment saved = enrollmentRepository.save(e);
        return toDto(saved);
    }


    @Transactional
    public EnrollmentDTO setRating(Long enrollmentId, Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1..5");
        }
        Enrollment e = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        // rating only allowed after done
        if (!Boolean.TRUE.equals(e.getDone())) {
            throw new IllegalStateException("Cannot rate before completing the course");
        }
        e.setRating(rating);
        Enrollment saved = enrollmentRepository.save(e);
        return toDto(saved);
    }


}