package com.elearning.backend.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    @NotBlank
    private String text;

    @NotNull
    private List<String> options;

    @NotNull
    private Integer correctAnswer;

//    public String getText() { return text; }
//    public void setText(String text) { this.text = text; }
//
//    public List<String> getOptions() { return options; }
//    public void setOptions(List<String> options) { this.options = options; }
//
//    public Integer getCorrectAnswer() { return correctAnswer; }
//    public void setCorrectAnswer(Integer correctAnswer) { this.correctAnswer = correctAnswer; }
}

