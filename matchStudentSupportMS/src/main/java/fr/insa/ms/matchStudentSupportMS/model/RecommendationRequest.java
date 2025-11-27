package fr.insa.ms.matchStudentSupportMS.model;


import java.time.LocalDateTime;
import java.util.List;

public class RecommendationRequest {

    private List<String> requestKeywords;
    private LocalDateTime desiredDate;
    private Student requester;
    private List<Student> candidates;

    public RecommendationRequest(List<String> requestKeywords, 
                                 LocalDateTime desiredDate,
                                 Student requester,
                                 List<Student> candidates) {
        this.requestKeywords = requestKeywords;
        this.desiredDate = desiredDate;
        this.requester = requester;
        this.candidates = candidates;
    }

    // ===== Getters =====
    public List<String> getRequestKeywords() {
        return requestKeywords;
    }

    public LocalDateTime getDesiredDate() {
        return desiredDate;
    }

    public Student getRequester() {
        return requester;
    }

    public List<Student> getCandidates() {
        return candidates;
    }

    // ===== Setters =====
    public void setRequestKeywords(List<String> requestKeywords) {
        this.requestKeywords = requestKeywords;
    }

    public void setDesiredDate(LocalDateTime desiredDate) {
        this.desiredDate = desiredDate;
    }

    public void setRequester(Student requester) {
        this.requester = requester;
    }

    public void setCandidates(List<Student> candidates) {
        this.candidates = candidates;
    }
}
