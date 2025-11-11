package com.elearning.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    // getters/setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        if (questions != null) {
            questions.forEach(this::addQuestion);
        }
    }

    public void addQuestion(Question q) {
        q.setAssessment(this);
        this.questions.add(q);
    }

    public void clearQuestions() {
        for (Question q : questions) {
            q.setAssessment(null);
        }
        questions.clear();
    }
}
