package fr.insa.ms.matchStudentSupportMS.controller;

import fr.insa.ms.matchStudentSupportMS.model.RecommendationRequest;
import fr.insa.ms.matchStudentSupportMS.model.Student;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {

    @PostMapping("/best-helpers")
    public List<Student> getBestHelpers(@RequestBody RecommendationRequest req) {

        Map<Student, Integer> scores = new HashMap<>();

        for (Student s : req.getCandidates()) {

            if (s.getId() == req.getRequester().getId()) continue;

            int score = 0;

            if (Objects.equals(s.getFiliere(), req.getRequester().getFiliere())) {
                score += 5;
            }

            for (String kw : req.getRequestKeywords()) {
                if (s.getCompetences().contains(kw.toLowerCase())) {
                    score += 2;
                }
            }

            if (!s.getDisponibilites().contains(req.getDesiredDate())) {
                score = 0;
            }

            scores.put(s, score);
        }

        return scores.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .filter(e -> e.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
