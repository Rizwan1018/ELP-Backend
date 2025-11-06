
package com.elearning.backend.dto;

import java.time.Instant;

public class AssessmentAttemptSummaryDto {
    private Long studentId;
    private String studentName;
    private int score;
    private int totalQuestions;
    private Instant attemptedAt;

    public AssessmentAttemptSummaryDto() {}

    public AssessmentAttemptSummaryDto(Long studentId, String studentName, int score, int totalQuestions, Instant attemptedAt) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.attemptedAt = attemptedAt;
    }

    public Long getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public Instant getAttemptedAt() { return attemptedAt; }

    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setScore(int score) { this.score = score; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    public void setAttemptedAt(Instant attemptedAt) { this.attemptedAt = attemptedAt; }
}
