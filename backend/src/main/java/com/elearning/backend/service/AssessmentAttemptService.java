
package com.elearning.backend.service;

import com.elearning.backend.dto.*;
import com.elearning.backend.entity.*;
import com.elearning.backend.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
public class AssessmentAttemptService {

    private final AssessmentRepository assessmentRepo;
    private final AssessmentAttemptRepository attemptRepo;
    private final UserRepository userRepo;
    private final StudentRepository studentRepo;

    public AssessmentAttemptService(AssessmentRepository assessmentRepo,
                                    AssessmentAttemptRepository attemptRepo,
                                    UserRepository userRepo,
                                    StudentRepository studentRepo) {
        this.assessmentRepo = assessmentRepo;
        this.attemptRepo = attemptRepo;
        this.userRepo = userRepo;
        this.studentRepo = studentRepo;
    }

    private Long resolveCurrentUserId(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "Unauthenticated");
        }
        String email = auth.getName().trim().toLowerCase();
        return userRepo.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found by email: " + email));
    }

    public AssessmentAttemptResponse submitAttempt(AssessmentAttemptRequest req, Authentication auth) {
        Long userId = resolveCurrentUserId(auth);

        Assessment assessment = assessmentRepo.findById(req.getAssessmentId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Assessment not found: " + req.getAssessmentId()));

        // Build lookup of questions by id
        List<Question> questions = assessment.getQuestions();
        Map<Long, Question> qById = questions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        int totalQuestions = questions.size();
        int score = 0;

        Map<Long, Integer> cleanedAnswers = new HashMap<>();
        for (Map.Entry<Long, Integer> e : req.getAnswers().entrySet()) {
            Long qid = e.getKey();
            Integer selected = e.getValue();
            Question q = qById.get(qid);
            if (q == null) continue; // ignore answers for questions not in this assessment
            if (selected == null) continue;
            if (selected < 0 || selected >= q.getOptions().size()) continue; // invalid index -> ignore

            cleanedAnswers.put(qid, selected);
            if (selected.equals(q.getCorrectAnswer())) {
                score++;
            }
        }

        AssessmentAttempt attempt = attemptRepo
                .findByAssessmentIdAndStudentId(assessment.getId(), userId)
                .orElseGet(AssessmentAttempt::new);

        attempt.setAssessment(assessment);
        attempt.setStudentId(userId);
        attempt.setAnswers(cleanedAnswers);
        attempt.setScore(score);
        attempt.setTotalQuestions(totalQuestions);
        attempt.setAttemptedAt(Instant.now());

        AssessmentAttempt saved = attemptRepo.save(attempt);

        String studentName = studentRepo.findById(userId).map(Student::getName).orElse(null);

        AssessmentAttemptResponse res = new AssessmentAttemptResponse();
        res.setAssessmentId(assessment.getId());
        res.setAssessmentTitle(assessment.getTitle());
        res.setStudentId(userId);
        res.setStudentName(studentName);
        res.setScore(saved.getScore());
        res.setTotalQuestions(saved.getTotalQuestions());
        res.setAttemptedAt(saved.getAttemptedAt());
        return res;
    }

    public List<StudentAttemptSummaryDto> getMyAttempts(Authentication auth) {
        Long userId = resolveCurrentUserId(auth);
        List<AssessmentAttempt> attempts = attemptRepo.findAllByStudentIdOrderByAttemptedAtDesc(userId);

        return attempts.stream()
                .map(a -> new StudentAttemptSummaryDto(
                        a.getAssessment().getId(),
                        a.getAssessment().getTitle(),
                        a.getScore(),
                        a.getTotalQuestions(),
                        a.getAttemptedAt()
                ))
                .toList();
    }

    public List<AssessmentAttemptSummaryDto> getAttemptsByAssessment(Long assessmentId) {
        Assessment assessment = assessmentRepo.findById(assessmentId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Assessment not found: " + assessmentId));

        List<AssessmentAttempt> attempts = attemptRepo.findAllByAssessmentIdOrderByAttemptedAtDesc(assessment.getId());

        return attempts.stream()
                .map(a -> {
                    String studentName = studentRepo.findById(a.getStudentId()).map(Student::getName).orElse(null);
                    return new AssessmentAttemptSummaryDto(
                            a.getStudentId(),
                            studentName,
                            a.getScore(),
                            a.getTotalQuestions(),
                            a.getAttemptedAt()
                    );
                })
                .toList();
    }
}
