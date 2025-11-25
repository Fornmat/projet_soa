package fr.insa.ms.matchStudentSupportMS.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

        List<Student> students = rest.getForObject("http://studentManagementMS/students", ArrayList.class);

        if (students == null) {
            return new RecommendationResult(requestId, new ArrayList<>());
        }

        List<Integer> recommended = new ArrayList<>();

        for (Object o : students) {
            var map = (java.util.LinkedHashMap<?, ?>) o;

            int id = (int) map.get("id");
            List<String> skills = (List<String>) map.get("competences");
            long matchCount = keywords.stream()
                    .filter(skills::contains)
                    .count();

            if (matchCount > 0) {
                recommended.add(id);
            }
        }
        
        return new RecommendationResult(requestId, recommended);
    }
}
