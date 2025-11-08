
package com.elearning.backend.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class AssessmentAttemptRequest {

    @NotNull
    private Long assessmentId;

    @NotNull
    private Map<Long, Integer> answers;

    public Long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }

    public Map<Long, Integer> getAnswers() { return answers; }
    public void setAnswers(Map<Long, Integer> answers) { this.answers = answers; }
}
