package com.elearning.backend.dto;


import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class AssessmentDto {
    private Long id;

    @NotBlank
    private String title;

    private String description;

    private List<QuestionDto> questions;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<QuestionDto> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDto> questions) { this.questions = questions; }
}
