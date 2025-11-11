package com.elearning.backend.AssessmentTest;

import com.elearning.backend.dto.*;
import com.elearning.backend.entity.*;
import com.elearning.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import com.elearning.backend.service.AssessmentAttemptService;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentAttemptServiceTest {

    AssessmentRepository assessmentRepo = mock(AssessmentRepository.class);
    AssessmentAttemptRepository attemptRepo = mock(AssessmentAttemptRepository.class);
    UserRepository userRepo = mock(UserRepository.class);
    StudentRepository studentRepo = mock(StudentRepository.class);

    AssessmentAttemptService service;

    Authentication mockAuth;
    User user;
    Student student;

    @BeforeEach
    void setUp() {
        service = new AssessmentAttemptService(assessmentRepo, attemptRepo, userRepo, studentRepo);

        mockAuth = mock(Authentication.class);
        user = new User();
        user.setId(1L);
        user.setEmail("student@email.com");
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");

        when(mockAuth.getName()).thenReturn(user.getEmail());
        when(userRepo.findByEmail(user.getEmail().toLowerCase())).thenReturn(Optional.of(user));
        when(studentRepo.findById(user.getId())).thenReturn(Optional.of(student));
    }

    @Test
    void submitAttempt_Correct_Answers_SavedAndReturned() {
        // Setup
        Question q1 = new Question();
        q1.setId(11L);
        q1.setCorrectAnswer(1);
        q1.setOptions(List.of("a", "b", "c"));
        Question q2 = new Question();
        q2.setId(12L);
        q2.setCorrectAnswer(0);
        q2.setOptions(List.of("x", "y"));

        Assessment assessment = new Assessment();
        assessment.setId(3L);
        assessment.setTitle("Quiz!");
        assessment.setQuestions(List.of(q1, q2));
        when(assessmentRepo.findById(3L)).thenReturn(Optional.of(assessment));

        Map<Long, Integer> answers = new HashMap<>();
        answers.put(11L, 1);
        answers.put(12L, 0);

        AssessmentAttemptRequest req = new AssessmentAttemptRequest();
        req.setAssessmentId(3L);
        req.setAnswers(answers);

        AssessmentAttempt attempt = new AssessmentAttempt();
        attempt.setAssessment(assessment);
        attempt.setStudentId(user.getId());
        attempt.setAnswers(answers);
        attempt.setScore(2);
        attempt.setTotalQuestions(2);
        attempt.setAttemptedAt(Instant.now());

        when(attemptRepo.findByAssessmentIdAndStudentId(3L, 1L)).thenReturn(Optional.empty());
        when(attemptRepo.save(any(AssessmentAttempt.class))).thenReturn(attempt);

        // Action
        AssessmentAttemptResponse res = service.submitAttempt(req, mockAuth);

        // Verify
        assertEquals(3L, res.getAssessmentId());
        assertEquals("Quiz!", res.getAssessmentTitle());
        assertEquals(2, res.getScore());
        assertEquals(2, res.getTotalQuestions());
        assertEquals("John Doe", res.getStudentName());
    }

    @Test
    void submitAttempt_AssessmentNotFound_Throws() {
        AssessmentAttemptRequest req = new AssessmentAttemptRequest();
        req.setAssessmentId(44L);

        when(assessmentRepo.findById(44L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.submitAttempt(req, mockAuth));
        assertEquals("404 NOT_FOUND \"Assessment not found: 44\"", ex.getMessage());
    }

    @Test
    void getMyAttempts_ReturnsSummaryDtos() {
        Assessment assessment = new Assessment();
        assessment.setId(5L);
        assessment.setTitle("Final");

        AssessmentAttempt a1 = new AssessmentAttempt();
        a1.setAssessment(assessment);
        a1.setScore(3);
        a1.setTotalQuestions(5);
        a1.setAttemptedAt(Instant.parse("2023-03-01T10:00:00Z"));

        List<AssessmentAttempt> attempts = List.of(a1);

        when(attemptRepo.findAllByStudentIdOrderByAttemptedAtDesc(1L)).thenReturn(attempts);

        List<StudentAttemptSummaryDto> result = service.getMyAttempts(mockAuth);

        assertEquals(1, result.size());
        StudentAttemptSummaryDto dto = result.get(0);
        assertEquals(5L, dto.getAssessmentId());
        assertEquals("Final", dto.getAssessmentTitle());
        assertEquals(3, dto.getScore());
        assertEquals(5, dto.getTotalQuestions());
        assertEquals(Instant.parse("2023-03-01T10:00:00Z"), dto.getAttemptedAt());
    }

    @Test
    void getAttemptsByAssessment_ReturnsSummaryDtos() {
        Assessment assessment = new Assessment();
        assessment.setId(6L);
        assessment.setTitle("Test Assessment");
        when(assessmentRepo.findById(6L)).thenReturn(Optional.of(assessment));

        AssessmentAttempt a1 = new AssessmentAttempt();
        a1.setAssessment(assessment);
        a1.setStudentId(1L);
        a1.setScore(5);
        a1.setTotalQuestions(7);
        a1.setAttemptedAt(Instant.parse("2023-03-02T10:00:00Z"));

        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        when(attemptRepo.findAllByAssessmentIdOrderByAttemptedAtDesc(6L)).thenReturn(List.of(a1));

        List<AssessmentAttemptSummaryDto> result = service.getAttemptsByAssessment(6L);

        assertEquals(1, result.size());
        AssessmentAttemptSummaryDto dto = result.get(0);
        assertEquals(1L, dto.getStudentId());
        assertEquals("John Doe", dto.getStudentName());
        assertEquals(5, dto.getScore());
        assertEquals(7, dto.getTotalQuestions());
        assertEquals(Instant.parse("2023-03-02T10:00:00Z"), dto.getAttemptedAt());
    }

    @Test
    void getAttemptsByAssessmentNotFound_Throws() {
        when(assessmentRepo.findById(100L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.getAttemptsByAssessment(100L));
        assertEquals("404 NOT_FOUND \"Assessment not found: 100\"", ex.getMessage());
    }

    @Test
    void submitAttempt_Unauthenticated_Throws() {
        Authentication badAuth = mock(Authentication.class);
        when(badAuth.getName()).thenReturn(null);

        AssessmentAttemptRequest req = new AssessmentAttemptRequest();
        req.setAssessmentId(1L);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.submitAttempt(req, badAuth));
        assertTrue(ex.getMessage().contains("401 UNAUTHORIZED \"Unauthenticated\""));
    }

    @Test
    void submitAttempt_InvalidAnswerIndex_DoesNotCountScore() {
        Question q1 = new Question();
        q1.setId(11L);
        q1.setCorrectAnswer(1);
        q1.setOptions(List.of("a", "b", "c"));

        Assessment assessment = new Assessment();
        assessment.setId(3L);
        assessment.setTitle("Quiz!");
        assessment.setQuestions(List.of(q1));
        when(assessmentRepo.findById(3L)).thenReturn(Optional.of(assessment));

        Map<Long, Integer> answers = new HashMap<>();
        answers.put(11L, 5); // invalid index

        AssessmentAttemptRequest req = new AssessmentAttemptRequest();
        req.setAssessmentId(3L);
        req.setAnswers(answers);

        AssessmentAttempt attempt = new AssessmentAttempt();
        attempt.setAssessment(assessment);
        attempt.setStudentId(user.getId());
        attempt.setAnswers(new HashMap<>());
        attempt.setScore(0);
        attempt.setTotalQuestions(1);
        attempt.setAttemptedAt(Instant.now());

        when(attemptRepo.findByAssessmentIdAndStudentId(3L, 1L)).thenReturn(Optional.empty());
        when(attemptRepo.save(any(AssessmentAttempt.class))).thenReturn(attempt);

        AssessmentAttemptResponse res = service.submitAttempt(req, mockAuth);

        assertEquals(0, res.getScore());
        assertEquals(1, res.getTotalQuestions());
    }
}