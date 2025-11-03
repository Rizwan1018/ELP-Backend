package com.elearning.backend.mapper;



import com.elearning.backend.dto.AssessmentDto;
import com.elearning.backend.dto.QuestionDto;
import com.elearning.backend.model.Assessment;
import com.elearning.backend.model.Question;

import java.util.ArrayList;
import java.util.List;

public class AssessmentMapper {

    public static AssessmentDto toDto(Assessment entity) {
        AssessmentDto dto = new AssessmentDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());

        List<QuestionDto> qDtos = new ArrayList<>();
        if (entity.getQuestions() != null) {
            for (Question q : entity.getQuestions()) {
                QuestionDto qDto = new QuestionDto();
                qDto.setText(q.getText());
                qDto.setOptions(q.getOptions());
                qDto.setCorrectAnswer(q.getCorrectAnswer());
                qDtos.add(qDto);
            }
        }
        dto.setQuestions(qDtos);
        return dto;
    }

    public static Assessment toEntity(AssessmentDto dto) {
        Assessment entity = new Assessment();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        if (dto.getQuestions() != null) {
            for (QuestionDto qDto : dto.getQuestions()) {
                Question q = new Question();
                q.setText(qDto.getText());
                q.setOptions(qDto.getOptions());
                q.setCorrectAnswer(qDto.getCorrectAnswer());
                entity.addQuestion(q);
            }
        }
        return entity;
    }

    public static void copyToExisting(AssessmentDto dto, Assessment existing) {
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());

        existing.clearQuestions();
        if (dto.getQuestions() != null) {
            for (QuestionDto qDto : dto.getQuestions()) {
                Question q = new Question();
                q.setText(qDto.getText());
                q.setOptions(qDto.getOptions());
                q.setCorrectAnswer(qDto.getCorrectAnswer());
                existing.addQuestion(q);
            }
        }
    }
}
