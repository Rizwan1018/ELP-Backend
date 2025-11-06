
package com.elearning.backend.dto;

import java.time.Instant;

public class StudentAttemptSummaryDto {
    private Long assessmentId;
    private String assessmentTitle;
    private int score;
    private int totalQuestions;
    private Instant attemptedAt;

    public StudentAttemptSummaryDto() {}

    public StudentAttemptSummaryDto(Long assessmentId, String assessmentTitle, int score, int totalQuestions, Instant attemptedAt) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.attemptedAt = attemptedAt;
    }

    public Long getAssessmentId() { return assessmentId; }
    public String getAssessmentTitle() { return assessmentTitle; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public Instant getAttemptedAt() { return attemptedAt; }

    public void setAssessmentId(Long assessmentId) { this.assessmentId = assessmentId; }
    public void setAssessmentTitle(String assessmentTitle) { this.assessmentTitle = assessmentTitle; }
    public void setScore(int score) { this.score = score; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    public void setAttemptedAt(Instant attemptedAt) { this.attemptedAt = attemptedAt; }
}
