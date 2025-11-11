package com.elearning.backend.AssessmentTest;


import com.elearning.backend.dto.AssessmentDto;
import com.elearning.backend.dto.ScoreResponse;
import com.elearning.backend.mapper.AssessmentMapper;
import com.elearning.backend.entity.Assessment;
import com.elearning.backend.entity.Question;
import com.elearning.backend.repository.AssessmentRepository;
import com.elearning.backend.service.AssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;



import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {

    AssessmentRepository repo = mock(AssessmentRepository.class);
    AssessmentService service = new AssessmentService(repo);

    @BeforeEach
    void setup() {
        // Clear interactions just to be safe
        clearInvocations(repo);
    }

    @Test
    void findAll_ReturnsDtoList() {
        Assessment a1 = new Assessment();
        Assessment a2 = new Assessment();
        List<Assessment> entities = List.of(a1, a2);

        // Stub repository findAll.
        when(repo.findAll()).thenReturn(entities);

        // You need to mock static calls for AssessmentMapper if using static methods
        try (var mocked = mockStatic(AssessmentMapper.class)) {
            AssessmentDto dto1 = new AssessmentDto();
            AssessmentDto dto2 = new AssessmentDto();
            mocked.when(() -> AssessmentMapper.toDto(a1)).thenReturn(dto1);
            mocked.when(() -> AssessmentMapper.toDto(a2)).thenReturn(dto2);

            List<AssessmentDto> actual = service.findAll();

            assertEquals(List.of(dto1, dto2), actual);
        }
    }

    @Test
    void findById_AssessmentExists_ReturnsDto() {
        Assessment entity = new Assessment();
        entity.setQuestions(List.of(new Question(), new Question()));
        entity.setId(5L);

        when(repo.findById(5L)).thenReturn(Optional.of(entity));
        try (var mocked = mockStatic(AssessmentMapper.class)) {
            AssessmentDto dto = new AssessmentDto();
            mocked.when(() -> AssessmentMapper.toDto(entity)).thenReturn(dto);

            AssessmentDto result = service.findById(5L);

            assertEquals(dto, result);
            verify(repo).findById(5L);
        }
    }

    @Test
    void findById_NotFound_Throws() {
        when(repo.findById(8L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.findById(8L));
        assertEquals("404 NOT_FOUND \"Assessment not found: 8\"", ex.getMessage());
    }

    @Test
    void create_CreatesAndReturnsDto() {
        AssessmentDto dto = new AssessmentDto();
        Assessment entity = new Assessment();
        Assessment saved = new Assessment();
        saved.setId(1L);

        try (var mocked = mockStatic(AssessmentMapper.class)) {
            mocked.when(() -> AssessmentMapper.toEntity(dto)).thenReturn(entity);
            when(repo.save(entity)).thenReturn(saved);
            mocked.when(() -> AssessmentMapper.toDto(saved)).thenReturn(dto);

            AssessmentDto result = service.create(dto);

            assertEquals(dto, result);
            verify(repo).save(entity);
        }
    }

    @Test
    void update_UpdatesAndReturnsDto() {
        AssessmentDto dto = new AssessmentDto();
        Assessment existing = new Assessment();
        Assessment saved = new Assessment();

        when(repo.findById(2L)).thenReturn(Optional.of(existing));
        try (var mocked = mockStatic(AssessmentMapper.class)) {
            mocked.when(() -> AssessmentMapper.copyToExisting(dto, existing)).then(inv -> null);
            when(repo.save(existing)).thenReturn(saved);
            mocked.when(() -> AssessmentMapper.toDto(saved)).thenReturn(dto);

            AssessmentDto result = service.update(2L, dto);

            assertEquals(dto, result);
            verify(repo).findById(2L);
            verify(repo).save(existing);
        }
    }

    @Test
    void update_NotFound_Throws() {
        AssessmentDto dto = new AssessmentDto();
        when(repo.findById(10L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.update(10L, dto));
        assertEquals("404 NOT_FOUND \"Assessment not found: 10\"", ex.getMessage());
    }

    @Test
    void delete_DeletesIfExists() {
        when(repo.existsById(7L)).thenReturn(true);
        service.delete(7L);
        verify(repo).deleteById(7L);
    }

    @Test
    void delete_NotFound_Throws() {
        when(repo.existsById(42L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.delete(42L));
        assertEquals("404 NOT_FOUND \"Assessment not found: 42\"", ex.getMessage());
    }

    @Test
    void evaluateAnswers_ReturnsScoreResponse() {
        Question q0 = new Question();
        q0.setCorrectAnswer(1);
        Question q1 = new Question();
        q1.setCorrectAnswer(0);
        List<Question> questions = List.of(q0, q1);

        Assessment assessment = new Assessment();
        assessment.setId(9L);
        assessment.setQuestions(questions);

        when(repo.findById(9L)).thenReturn(Optional.of(assessment));

        // The answer map is: index 0->1 is correct, index 1->0 is correct (both correct)
        Map<Integer, Integer> answers = Map.of(0, 1, 1, 0);

        ScoreResponse result = service.evaluateAnswers(9L, answers);

        assertEquals(new ScoreResponse(2, 2), result);
    }

    @Test
    void evaluateAnswers_NotFound_Throws() {
        when(repo.findById(5L)).thenReturn(Optional.empty());
        Map<Integer, Integer> answers = Map.of();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.evaluateAnswers(5L, answers));
        assertEquals("404 NOT_FOUND \"Assessment not found: 5\"", ex.getMessage());
    }

    @Test
    void evaluateAnswers_UnmatchedAnswers_ReturnsCorrectScore() {
        // Only one correct
        Question q0 = new Question();
        q0.setCorrectAnswer(1);
        Question q1 = new Question();
        q1.setCorrectAnswer(0);

        Assessment assessment = new Assessment();
        assessment.setId(15L);
        assessment.setQuestions(List.of(q0, q1));

        when(repo.findById(15L)).thenReturn(Optional.of(assessment));

        Map<Integer, Integer> answers = Map.of(0, 1, 1, 99); // second answer is incorrect
        ScoreResponse res = service.evaluateAnswers(15L, answers);

        assertEquals(new ScoreResponse(2, 1), res);
    }
}