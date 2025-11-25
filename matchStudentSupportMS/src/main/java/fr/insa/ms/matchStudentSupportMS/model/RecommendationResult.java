package fr.insa.ms.matchStudentSupportMS.model;

import java.util.List;

public class RecommendationResult {

    private int requestId;
    private List<Integer> recommendedStudents;

    public RecommendationResult() {}

    public RecommendationResult(int requestId, List<Integer> recommendedStudents) {
        this.requestId = requestId;
        this.recommendedStudents = recommendedStudents;
    }

    public int getRequestId() { return requestId; }
    public List<Integer> getRecommendedStudents() { return recommendedStudents; }
}

