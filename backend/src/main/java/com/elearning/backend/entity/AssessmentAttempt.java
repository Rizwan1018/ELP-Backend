
package com.elearning.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(
        name = "assessment_attempts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"assessment_id", "student_id"})
)
public class AssessmentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The assessment attempted
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    // We use User/Student ID (from JWT email -> UserRepo)
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "attempted_at", nullable = false)
    private Instant attemptedAt;

    // Answers: questionId -> selected option index
    @ElementCollection
    @CollectionTable(name = "attempt_answers", joinColumns = @JoinColumn(name = "attempt_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "selected_index", nullable = false)
    private Map<Long, Integer> answers = new HashMap<>();

    // getters & setters
    public Long getId() { return id; }

    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Instant getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(Instant attemptedAt) { this.attemptedAt = attemptedAt; }

    public Map<Long, Integer> getAnswers() { return answers; }
    public void setAnswers(Map<Long, Integer> answers) { this.answers = answers; }
}
