package com.elearning.backend.EnrollementTest;

import com.elearning.backend.dto.EnrollmentDTO;
import com.elearning.backend.entity.Enrollment;
import com.elearning.backend.repository.CourseRepository;
import com.elearning.backend.repository.EnrollmentRepository;
import com.elearning.backend.repository.NotificationRepository;
import com.elearning.backend.service.EnrollmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    // EnrollmentServiceTest.java

    // 1. Declare the new dependency as a Mock
    @Mock
    private NotificationRepository notificationRepository;


    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment(Long id, Long studentId, Long courseId,
                                  int progress, String status, boolean watched,
                                  boolean done, Integer rating, Integer lastPos) {
        Enrollment e = new Enrollment();
        e.setId(id);
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setEnrollmentDate(LocalDate.of(2025,1,1));
        e.setProgress(progress);
        e.setStatus(status);
        e.setWatched(watched);
        e.setDone(done);
        e.setRating(rating);
        e.setLastWatchedPosition(lastPos);
        return e;
    }

    @Test
    @DisplayName("getEnrollmentsByStudent maps entities to DTOs")
    void testGetByStudent() {
        Enrollment e = enrollment(10L, 5L, 7L, 20, "enrolled", false, false, null, null);
        when(enrollmentRepository.findByStudentId(5L)).thenReturn(List.of(e));

        List<EnrollmentDTO> list = enrollmentService.getEnrollmentsByStudent(5L);

        assertEquals(1, list.size());
        assertEquals(10L, list.get(0).getId());
        assertEquals(20, list.get(0).getProgress());
        verify(enrollmentRepository).findByStudentId(5L);
    }

    @Test
    @DisplayName("getEnrollmentsByCourse maps entities")
    void testGetByCourse() {
        Enrollment e = enrollment(11L, 8L, 9L, 0, "enrolled", false, false, null, null);
        when(enrollmentRepository.findByCourseId(9L)).thenReturn(List.of(e));

        List<EnrollmentDTO> list = enrollmentService.getEnrollmentsByCourse(9L);

        assertEquals(1, list.size());
        assertEquals(11L, list.get(0).getId());
        verify(enrollmentRepository).findByCourseId(9L);
    }

    @Test
    @DisplayName("enroll returns existing if present")
    void testEnrollExisting() {
        Enrollment existing = enrollment(12L, 2L, 3L, 0, "enrolled", false, false, null, null);
        when(enrollmentRepository.findByStudentIdAndCourseId(2L, 3L)).thenReturn(Optional.of(existing));

        EnrollmentDTO dto = enrollmentService.enroll(2L, 3L);

        assertEquals(12L, dto.getId());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("enroll creates new if not existing")
    void testEnrollNew() {
        when(enrollmentRepository.findByStudentIdAndCourseId(4L, 5L)).thenReturn(Optional.empty());
        ArgumentCaptor<Enrollment> cap = ArgumentCaptor.forClass(Enrollment.class);
        Enrollment saved = enrollment(20L, 4L, 5L, 0, "enrolled", false, false, null, null);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(saved);

        EnrollmentDTO dto = enrollmentService.enroll(4L, 5L);

        verify(enrollmentRepository).save(cap.capture());
        Enrollment toSave = cap.getValue();
        assertEquals(4L, toSave.getStudentId());
        assertEquals(5L, dto.getCourseId());
        assertEquals(0, dto.getProgress());
        assertEquals("enrolled", dto.getStatus());
    }

    @Test
    @DisplayName("updateProgress sets progress and completes if >=100")
    void testUpdateProgressComplete() {
        Enrollment e = enrollment(30L, 6L, 7L, 90, "enrolled", false, false, null, null);
        when(enrollmentRepository.findById(30L)).thenReturn(Optional.of(e));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO dto = enrollmentService.updateProgress(30L, 100);

        assertEquals(100, dto.getProgress());
        assertEquals("completed", dto.getStatus());
    }

    @Test
    @DisplayName("updateProgress retains status if <100")
    void testUpdateProgressNotComplete() {
        Enrollment e = enrollment(31L, 6L, 8L, 10, "enrolled", false, false, null, null);
        when(enrollmentRepository.findById(31L)).thenReturn(Optional.of(e));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO dto = enrollmentService.updateProgress(31L, 50);

        assertEquals(50, dto.getProgress());
        assertEquals("enrolled", dto.getStatus());
    }

    @Test
    @DisplayName("updateStatus changes status")
    void testUpdateStatus() {
        Enrollment e = enrollment(40L, 9L, 10L, 0, "enrolled", false, false, null, null);
        when(enrollmentRepository.findById(40L)).thenReturn(Optional.of(e));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO dto = enrollmentService.updateStatus(40L, "paused");

        assertEquals("paused", dto.getStatus());
    }

    @Test
    @DisplayName("markWatched sets watched and position")
    void testMarkWatchedWithPosition() {
        Enrollment e = enrollment(60L, 1L, 2L, 0, "enrolled", false, false, null, null);
        when(enrollmentRepository.findById(60L)).thenReturn(Optional.of(e));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO dto = enrollmentService.markWatched(60L, 120);

        assertTrue(dto.getWatched());
        assertEquals(120, dto.getLastWatchedPosition());
    }

    @Test
    @DisplayName("markWatched keeps existing position when null passed")
    void testMarkWatchedNullPosition() {
        Enrollment e = enrollment(61L, 1L, 3L, 0, "enrolled", false, false, null, 45);
        when(enrollmentRepository.findById(61L)).thenReturn(Optional.of(e));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO dto = enrollmentService.markWatched(61L, null);

        assertTrue(dto.getWatched());
        assertEquals(45, dto.getLastWatchedPosition());
    }

    @Test
    @DisplayName("markDone success sets done,status,progress")
    void testMarkDoneSuccess() {
        Enrollment e = enrollment(70L, 2L, 4L, 30, "enrolled", true, false, null, null);
        when(enrollmentRepository.findById(70L)).thenReturn(Optional.of(e));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO dto = enrollmentService.markDone(70L);

        assertTrue(dto.getDone());
        assertEquals("completed", dto.getStatus());
        assertEquals(100, dto.getProgress());
    }

    @Test
    @DisplayName("markDone throws when not watched")
    void testMarkDoneNotWatched() {
        Enrollment e = enrollment(71L, 2L, 5L, 0, "enrolled", false, false, null, null);
        when(enrollmentRepository.findById(71L)).thenReturn(Optional.of(e));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> enrollmentService.markDone(71L));
        assertTrue(ex.getMessage().contains("Cannot mark done"));
    }

    @Test
    @DisplayName("setRating success after done")
    void testSetRatingSuccess() {
        Enrollment e = enrollment(80L, 3L, 6L, 100, "completed", true, true, null, null);
        when(enrollmentRepository.findById(80L)).thenReturn(Optional.of(e));
        when(enrollmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO dto = enrollmentService.setRating(80L, 5);

        assertEquals(5, dto.getRating());
    }

    @Test
    @DisplayName("setRating invalid value throws IllegalArgumentException")
    void testSetRatingInvalid() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> enrollmentService.setRating(90L, 0));
        assertTrue(ex.getMessage().contains("1..5"));
        verify(enrollmentRepository, never()).findById(any());
    }

    @Test
    @DisplayName("setRating when not done throws IllegalStateException")
    void testSetRatingNotDone() {
        Enrollment e = enrollment(91L, 3L, 7L, 50, "enrolled", true, false, null, null);
        when(enrollmentRepository.findById(91L)).thenReturn(Optional.of(e));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> enrollmentService.setRating(91L, 4));
        assertTrue(ex.getMessage().contains("Cannot rate"));
    }
}
