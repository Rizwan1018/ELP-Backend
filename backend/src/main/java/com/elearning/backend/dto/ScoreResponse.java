package com.elearning.backend.dto;


import java.util.Objects;

public class ScoreResponse {
    private int totalQuestions;
    private int correct;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ScoreResponse that = (ScoreResponse) o;
        return totalQuestions == that.totalQuestions && correct == that.correct;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalQuestions, correct);
    }

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
