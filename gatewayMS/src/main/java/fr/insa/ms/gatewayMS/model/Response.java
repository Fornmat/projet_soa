package fr.insa.ms.gatewayMS.model;

import java.util.List;

public class Response {
    private Request request;
    private List<Student> recommendedHelpers;

    public Response(Request request, List<Student> recommendedHelpers) {
        this.request = request;
        this.recommendedHelpers = recommendedHelpers;
    }

    // ===== Getters =====
    public Request getRequest() {
        return request;
    }

    public List<Student> getRecommendedHelpers() {
        return recommendedHelpers;
    }

    // ===== Setters =====
    public void setRequest(Request request) {
        this.request = request;
    }

    public void setRecommendedHelpers(List<Student> recommendedHelpers) {
        this.recommendedHelpers = recommendedHelpers;
    }
}
