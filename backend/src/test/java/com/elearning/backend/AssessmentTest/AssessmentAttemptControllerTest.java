package com.elearning.backend.AssessmentTest;


import com.elearning.backend.controller.AssessmentAttemptController;
import com.elearning.backend.dto.*;
import com.elearning.backend.service.AssessmentAttemptService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentAttemptControllerTest {

    AssessmentAttemptService service = mock(AssessmentAttemptService.class);
    AssessmentAttemptController controller = new AssessmentAttemptController(service);
    Authentication auth = mock(Authentication.class);

    @Test
    void submit_ReturnsCreatedResponse() {
        AssessmentAttemptRequest request = new AssessmentAttemptRequest();
        AssessmentAttemptResponse response = new AssessmentAttemptResponse();

        when(service.submitAttempt(request, auth)).thenReturn(response);

        AssessmentAttemptResponse actual = controller.submit(request, auth);

        assertEquals(response, actual);
        verify(service).submitAttempt(request, auth);
    }

    @Test
    void myAttempts_ReturnsListOfStudentAttemptSummaryDto() {
        StudentAttemptSummaryDto summary1 = new StudentAttemptSummaryDto();
        StudentAttemptSummaryDto summary2 = new StudentAttemptSummaryDto();

        List<StudentAttemptSummaryDto> expected = Arrays.asList(summary1, summary2);

        when(service.getMyAttempts(auth)).thenReturn(expected);

        List<StudentAttemptSummaryDto> actual = controller.myAttempts(auth);

        assertEquals(expected, actual);
        verify(service).getMyAttempts(auth);
    }

    @Test
    void byAssessment_ReturnsListOfAssessmentAttemptSummaryDto() {
        Long assessmentId = 42L;
        AssessmentAttemptSummaryDto summary1 = new AssessmentAttemptSummaryDto();
        AssessmentAttemptSummaryDto summary2 = new AssessmentAttemptSummaryDto();

        List<AssessmentAttemptSummaryDto> expected = Arrays.asList(summary1, summary2);

        when(service.getAttemptsByAssessment(assessmentId)).thenReturn(expected);

        List<AssessmentAttemptSummaryDto> actual = controller.byAssessment(assessmentId);

        assertEquals(expected, actual);
        verify(service).getAttemptsByAssessment(assessmentId);
    }
}