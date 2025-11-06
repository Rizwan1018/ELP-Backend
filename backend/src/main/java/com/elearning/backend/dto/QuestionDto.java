
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

    private Long id; // <-- add this

    @NotBlank
    private String text;

    @NotNull
    private List<String> options;

    @NotNull
    private Integer correctAnswer;
}
