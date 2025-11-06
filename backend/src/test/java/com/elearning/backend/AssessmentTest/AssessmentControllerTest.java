package com.elearning.backend.AssessmentTest;


import com.elearning.backend.controller.AssessmentController;
import com.elearning.backend.dto.AssessmentDto;
import com.elearning.backend.dto.ScoreResponse;
import com.elearning.backend.repository.AssessmentRepository;
import com.elearning.backend.service.AssessmentService;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentControllerTest {

    AssessmentService service = mock(AssessmentService.class);
    AssessmentRepository repo = mock(AssessmentRepository.class); // Unused in controller
    AssessmentController controller = new AssessmentController(service, repo);

    @Test
    void getAll_ReturnsAssessmentList() {
        AssessmentDto a1 = new AssessmentDto();
        AssessmentDto a2 = new AssessmentDto();
        List<AssessmentDto> expected = List.of(a1, a2);

        when(service.findAll()).thenReturn(expected);

        List<AssessmentDto> result = controller.getAll();

        assertEquals(expected, result);
        verify(service).findAll();
    }

    @Test
    void getOne_ReturnsSingleAssessment() {
        Long id = 77L;
        AssessmentDto dto = new AssessmentDto();

        when(service.findById(id)).thenReturn(dto);

        AssessmentDto result = controller.getOne(id);

        assertEquals(dto, result);
        verify(service).findById(id);
    }

    @Test
    void create_ReturnsCreatedAssessment() {
        AssessmentDto input = new AssessmentDto();
        AssessmentDto created = new AssessmentDto();

        when(service.create(input)).thenReturn(created);

        AssessmentDto result = controller.create(input);

        assertEquals(created, result);
        verify(service).create(input);
    }

    @Test
    void update_ReturnsUpdatedAssessment() {
        AssessmentDto input = new AssessmentDto();
        AssessmentDto updated = new AssessmentDto();
        Long id = 55L;

        when(service.update(id, input)).thenReturn(updated);

        AssessmentDto result = controller.update(id, input);

        assertEquals(updated, result);
        verify(service).update(id, input);
    }

    @Test
    void delete_CallsServiceDelete() {
        Long id = 99L;

        controller.delete(id);

        verify(service).delete(id);
    }

    @Test
    void evaluate_ReturnsScoreResponse() {
        Long id = 100L;
        Map<Integer, Integer> answers = Map.of(1, 2, 3, 4);
        ScoreResponse expected = new ScoreResponse();

        when(service.evaluateAnswers(id, answers)).thenReturn(expected);

        ScoreResponse result = controller.evaluate(id, answers);

        assertEquals(expected, result);
        verify(service).evaluateAnswers(id, answers);
    }
}