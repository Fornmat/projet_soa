package fr.insa.ms.matchStudentSupportMS.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import fr.insa.ms.matchStudentSupportMS.model.RecommendationResult;
import fr.insa.ms.matchStudentSupportMS.model.Student;
import fr.insa.ms.matchStudentSupportMS.model.Request;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {

	@Autowired
	private RestTemplate rest;
	
    @GetMapping("/{requestId}")
    public RecommendationResult recommend(@PathVariable int requestId) {

        Request req = rest.getForObject("http://supportRequestMS/request/" + requestId, Request.class);

        if (req == null) {
            return new RecommendationResult(requestId, new ArrayList<>());
        }

        List<String> keywords = req.getKeywords();

        ResponseEntity<List<Student>> response = rest.exchange(
            "http://studentManagementMS/students/list",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Student>>() {}
        );

        List<Student> students = response.getBody();

        if (students == null) {
            return new RecommendationResult(requestId, new ArrayList<>());
        }

        List<Integer> recommended = new ArrayList<>();

        for (Student student : students) {
            List<String> skills = student.getCompetences();
            long matchCount = keywords.stream()
                    .filter(skills::contains)
                    .count();

            if (matchCount > 0) {
                recommended.add(student.getId());
            }
        }
        
        return new RecommendationResult(requestId, recommended);
    }
}
