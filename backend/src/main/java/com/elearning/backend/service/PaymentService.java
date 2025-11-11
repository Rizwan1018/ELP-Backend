package com.elearning.backend.service;

import com.elearning.backend.exception.CourseNotFoundException;
import com.elearning.backend.entity.Course;
import com.elearning.backend.entity.Enrollment;
import com.elearning.backend.entity.Payment;
import com.elearning.backend.repository.CourseRepository;
import com.elearning.backend.repository.EnrollmentRepository;
import com.elearning.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public PaymentService(PaymentRepository paymentRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.paymentRepository = paymentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Payment processPayment(Long studentId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));

        // 💰 Mock amount (you can replace with actual course price if you add it later)
        double amount = 499.00;

        // 🧾 Generate unique transaction ID
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);

        Payment payment = new Payment();
        payment.setStudentId(studentId);
        payment.setCourseId(courseId);
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");
        payment.setTransactionId(transactionId);

        Payment savedPayment = paymentRepository.save(payment);

        // ✅ Auto-enroll after payment success
        var existing = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existing.isEmpty()) {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudentId(studentId);
            enrollment.setCourseId(courseId);
            enrollment.setEnrollmentDate(java.time.LocalDate.now());
            enrollment.setProgress(0);
            enrollment.setStatus("enrolled");
            enrollmentRepository.save(enrollment);
        }

        return savedPayment;
    }

    public List<Payment> getPaymentsByStudent(Long studentId) {
        return paymentRepository.findByStudentId(studentId);
    }
}
