package com.elearning.backend.service;



import com.elearning.backend.dto.AssessmentDto;
import com.elearning.backend.dto.ScoreResponse;
import com.elearning.backend.model.Assessment;
import com.elearning.backend.model.Question;
import com.elearning.backend.mapper.AssessmentMapper;
import com.elearning.backend.repository.AssessmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

@Service
public class AssessmentService {

    private final AssessmentRepository repo;

    public AssessmentService(AssessmentRepository repo) {
        this.repo = repo;
    }

    public List<AssessmentDto> findAll() {
        return repo.findAll().stream().map(AssessmentMapper::toDto).toList();
    }

//    @Transactional
//    public AssessmentDto findById(Long id) {
//        Assessment entity = repo.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found: " + id));
//        return AssessmentMapper.toDto(entity);
//    }


    @Transactional
    public AssessmentDto findById(Long id) {
        Assessment entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found: " + id));

        // Force load questions and options
        entity.getQuestions().forEach(q -> q.getOptions().size());

        return AssessmentMapper.toDto(entity);
    }


    public AssessmentDto create(AssessmentDto dto) {
        Assessment entity = AssessmentMapper.toEntity(dto);
        Assessment saved = repo.save(entity);
        return AssessmentMapper.toDto(saved);
    }

    public AssessmentDto update(Long id, AssessmentDto dto) {
        Assessment existing = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found: " + id));
        AssessmentMapper.copyToExisting(dto, existing);
        Assessment saved = repo.save(existing);
        return AssessmentMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found: " + id);
        }
        repo.deleteById(id);
    }



    @Transactional
    public ScoreResponse evaluateAnswers(Long id, Map<Integer, Integer> answers) {
        Assessment assessment = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found: " + id));

        List<Question> questions = assessment.getQuestions();
        int total = questions.size();
        int correct = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            Integer chosen = answers.get(i);
            if (chosen != null && chosen.equals(q.getCorrectAnswer())) {
                correct++;
            }
        }
        return new ScoreResponse(total, correct);
    }

}
