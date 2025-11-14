package com.elearning.backend.service;

import com.elearning.backend.dto.AnnouncementDTO;
import com.elearning.backend.entity.Announcement;
import com.elearning.backend.entity.Enrollment;
import com.elearning.backend.entity.Notification;
import com.elearning.backend.repository.AnnouncementRepository;
import com.elearning.backend.repository.EnrollmentRepository;
import com.elearning.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final NotificationRepository notificationRepo;

    public AnnouncementService(AnnouncementRepository a, EnrollmentRepository e, NotificationRepository n) {
        this.announcementRepo = a; this.enrollmentRepo = e; this.notificationRepo = n;
    }

    public List<Announcement> byCourse(Long courseId) {
        return announcementRepo.findByCourseIdOrderByCreatedAtDesc(courseId);
    }

    @Transactional
    public Announcement create(AnnouncementDTO dto, String courseTitleForMsg) {
        Announcement a = new Announcement();
        a.setCourseId(dto.courseId);
        a.setInstructorId(dto.instructorId);
        a.setTitle(dto.title);
        a.setMessage(dto.message);
        a.setCreatedAt(Instant.now());
        Announcement saved = announcementRepo.save(a);

        List<Enrollment> enrollments = enrollmentRepo.findByCourseId(dto.courseId);
        for (Enrollment en : enrollments) {
            Notification n = new Notification();
            n.setUserId(en.getStudentId());
            n.setType("announcement");
            n.setMessage("[" + courseTitleForMsg + "] " + dto.title + ": " + dto.message);
            n.setCourseId(dto.courseId);
            n.setCreatedAt(Instant.now());
            n.setRead(false);
            notificationRepo.save(n);
        }

        return saved;
    }
}
