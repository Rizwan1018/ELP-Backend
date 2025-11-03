package com.elearning.backend.dto;



public class ScoreResponse {
    private int totalQuestions;
    private int correct;

    public ScoreResponse() {}
    public ScoreResponse(int totalQuestions, int correct) {
        this.totalQuestions = totalQuestions;
        this.correct = correct;
    }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrect() { return correct; }
    public void setCorrect(int correct) { this.correct = correct; }
}
