package com.elearning.backend.controller;


import com.elearning.backend.dto.AssessmentDto;
import com.elearning.backend.dto.ScoreResponse;
import com.elearning.backend.repository.AssessmentRepository;
import com.elearning.backend.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentService service;
    private final AssessmentRepository repo;

    public AssessmentController(AssessmentService service, AssessmentRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @GetMapping
    public List<AssessmentDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AssessmentDto getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssessmentDto create(@Valid @RequestBody AssessmentDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public AssessmentDto update(@PathVariable Long id, @Valid @RequestBody AssessmentDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /**
     * Optional: Evaluate answers server-side.
     * Body should be: { "0": 2, "1": 0, ... } (questionIndex -> chosenOptionIndex)
     */
//    @PostMapping("/{id}/evaluate")
//    public ScoreResponse evaluate(@PathVariable Long id, @RequestBody Map<Integer, Integer> answers) {
//        Assessment assessment = repo.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found: " + id));
//
//        List<Question> questions = assessment.getQuestions();
//        int total = questions.size();
//        int correct = 0;
//
//        for (int i = 0; i < questions.size(); i++) {
//            Question q = questions.get(i);
//            Integer chosen = answers.get(i);
//            if (chosen != null && chosen.equals(q.getCorrectAnswer())) {
//                correct++;
//            }
//        }
//        return new ScoreResponse(total, correct);
//    }




    @PostMapping("/{id}/evaluate")
    public ScoreResponse evaluate(@PathVariable Long id, @RequestBody Map<Integer, Integer> answers) {
        return service.evaluateAnswers(id, answers);
    }

}
