package com.elearning.backend.EnrollementTest;

import com.elearning.backend.controller.EnrollmentController;
import com.elearning.backend.dto.EnrollmentDTO;
import com.elearning.backend.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    private EnrollmentDTO sample(Long id, Long studentId, Long courseId) {
        EnrollmentDTO d = new EnrollmentDTO();
        d.setId(id);
        d.setStudentId(studentId);
        d.setCourseId(courseId);
        d.setEnrollmentDate(LocalDate.now());
        d.setProgress(10);
        d.setStatus("enrolled");
        d.setWatched(false);
        d.setDone(false);
        d.setRating(null);
        d.setLastWatchedPosition(null);
        return d;
    }

    @Test
    @DisplayName("getAll delegates to service with null studentId")
    void testGetAll() {
        List<EnrollmentDTO> list = Arrays.asList(sample(1L, 5L, 9L));
        when(enrollmentService.getEnrollmentsByStudent(isNull())).thenReturn(list);

        List<EnrollmentDTO> result = enrollmentController.getAll();

        assertEquals(1, result.size());
        verify(enrollmentService).getEnrollmentsByStudent(isNull());
    }

    @Test
    @DisplayName("getByStudent returns list")
    void testGetByStudent() {
        List<EnrollmentDTO> list = Arrays.asList(sample(2L, 7L, 9L), sample(3L, 7L, 10L));
        when(enrollmentService.getEnrollmentsByStudent(7L)).thenReturn(list);

        List<EnrollmentDTO> result = enrollmentController.getByStudent(7L);

        assertEquals(2, result.size());
        verify(enrollmentService).getEnrollmentsByStudent(7L);
    }

    @Test
    @DisplayName("getByCourse returns list")
    void testGetByCourse() {
        List<EnrollmentDTO> list = Arrays.asList(sample(4L, 8L, 11L));
        when(enrollmentService.getEnrollmentsByCourse(11L)).thenReturn(list);

        List<EnrollmentDTO> result = enrollmentController.getByCourse(11L);

        assertEquals(1, result.size());
        verify(enrollmentService).getEnrollmentsByCourse(11L);
    }

    @Test
    @DisplayName("getByCourseParam returns list (duplicate mapping)")
    void testGetByCourseParam() {
        List<EnrollmentDTO> list = Arrays.asList(sample(5L, 9L, 12L));
        when(enrollmentService.getEnrollmentsByCourse(12L)).thenReturn(list);

        List<EnrollmentDTO> result = enrollmentController.getByCourseParam(12L);

        assertEquals(1, result.size());
        verify(enrollmentService).getEnrollmentsByCourse(12L);
    }

    @Test
    @DisplayName("enroll calls service.enroll")
    void testEnroll() {
        EnrollmentDTO returned = sample(6L, 20L, 30L);
        when(enrollmentService.enroll(20L, 30L)).thenReturn(returned);

        EnrollmentDTO request = new EnrollmentDTO();
        request.setStudentId(20L);
        request.setCourseId(30L);

        EnrollmentDTO result = enrollmentController.enroll(request);

        assertEquals(6L, result.getId());
        verify(enrollmentService).enroll(20L, 30L);
    }

    @Test
    @DisplayName("updateProgress calls service and returns updated DTO")
    void testUpdateProgress() {
        EnrollmentDTO updated = sample(7L, 21L, 31L);
        updated.setProgress(55);
        when(enrollmentService.updateProgress(7L, 55)).thenReturn(updated);

        EnrollmentDTO result = enrollmentController.updateProgress(7L, 55);

        assertEquals(55, result.getProgress());
        verify(enrollmentService).updateProgress(7L, 55);
    }

    @Test
    @DisplayName("updateStatus updates status")
    void testUpdateStatus() {
        EnrollmentDTO updated = sample(8L, 22L, 32L);
        updated.setStatus("paused");
        when(enrollmentService.updateStatus(8L, "paused")).thenReturn(updated);

        EnrollmentDTO result = enrollmentController.updateStatus(8L, "paused");

        assertEquals("paused", result.getStatus());
        verify(enrollmentService).updateStatus(8L, "paused");
    }


    @Test
    @DisplayName("markWatched with position")
    void testMarkWatchedWithPosition() {
        EnrollmentDTO updated = sample(10L, 23L, 33L);
        updated.setWatched(true);
        updated.setLastWatchedPosition(120);
        when(enrollmentService.markWatched(10L, 120)).thenReturn(updated);

        EnrollmentDTO result = enrollmentController.markWatched(10L, 120);

        assertTrue(result.getWatched());
        assertEquals(120, result.getLastWatchedPosition());
        verify(enrollmentService).markWatched(10L, 120);
    }

    @Test
    @DisplayName("markWatched without position (null)")
    void testMarkWatchedNullPosition() {
        EnrollmentDTO updated = sample(11L, 24L, 34L);
        updated.setWatched(true);
        when(enrollmentService.markWatched(11L, null)).thenReturn(updated);

        EnrollmentDTO result = enrollmentController.markWatched(11L, null);

        assertTrue(result.getWatched());
        assertNull(result.getLastWatchedPosition());
        verify(enrollmentService).markWatched(11L, null);
    }

    @Test
    @DisplayName("markDone calls service")
    void testMarkDone() {
        EnrollmentDTO updated = sample(12L, 25L, 35L);
        updated.setWatched(true);
        updated.setDone(true);
        updated.setStatus("completed");
        updated.setProgress(100);
        when(enrollmentService.markDone(12L)).thenReturn(updated);

        EnrollmentDTO result = enrollmentController.markDone(12L);

        assertTrue(result.getDone());
        assertEquals("completed", result.getStatus());
        assertEquals(100, result.getProgress());
        verify(enrollmentService).markDone(12L);
    }

    @Test
    @DisplayName("markDone propagates IllegalStateException")
    void testMarkDoneException() {
        when(enrollmentService.markDone(13L)).thenThrow(new IllegalStateException("Cannot mark done before watching the course"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> enrollmentController.markDone(13L));
        assertTrue(ex.getMessage().contains("Cannot mark done"));
        verify(enrollmentService).markDone(13L);
    }

    @Test
    @DisplayName("setRating calls service")
    void testSetRating() {
        EnrollmentDTO updated = sample(14L, 26L, 36L);
        updated.setDone(true);
        updated.setRating(5);
        when(enrollmentService.setRating(14L, 5)).thenReturn(updated);

        EnrollmentDTO result = enrollmentController.setRating(14L, 5);

        assertEquals(5, result.getRating());
        verify(enrollmentService).setRating(14L, 5);
    }

    @Test
    @DisplayName("setRating propagates IllegalArgumentException for invalid rating")
    void testSetRatingInvalid() {
        when(enrollmentService.setRating(15L, 0)).thenThrow(new IllegalArgumentException("Rating must be 1..5"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> enrollmentController.setRating(15L, 0));
        assertTrue(ex.getMessage().contains("1..5"));
        verify(enrollmentService).setRating(15L, 0);
    }
}
