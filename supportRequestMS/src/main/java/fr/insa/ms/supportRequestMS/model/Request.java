package fr.insa.ms.supportRequestMS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Request {

    private int id;

    private String title;

    private String description;

    private ArrayList<String> keywords;

    private int requesterId;
    
    private int helperId;

    private LocalDateTime desiredDate;

    private Status status;

    public enum Status {
        ATTENTE,
        EN_COURS,
        REALISEE,
        ABANDONNEE,
        FERMEE;

        public boolean canTransitionTo(Status newStatus) {
            return switch (this) {
                case ATTENTE -> newStatus == EN_COURS || newStatus == ABANDONNEE;
                case EN_COURS -> newStatus == REALISEE || newStatus == ABANDONNEE;
                case ABANDONNEE, REALISEE -> newStatus == FERMEE;
                case FERMEE -> false;
            };
        }
    }

    public Request() {}

    public Request(String title, String description, ArrayList<String> keywords, int requesterId, int helperId, LocalDateTime desiredDate, Status status) {
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.requesterId = requesterId;
        this.helperId = helperId;
        this.desiredDate = desiredDate;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ArrayList<String> getKeywords() { return keywords; }
    public void setKeywords(ArrayList<String> keywords) { this.keywords = keywords; }

    public int getRequesterId() { return requesterId; }
    public void setRequesterId(int requesterId) { this.requesterId = requesterId; }

    public int getHelperId() { return helperId; }
    public void setHelperId(int helperId) { this.helperId = helperId; }

    public LocalDateTime getDesiredDate() { return desiredDate; }
    public void setDesiredDate(LocalDateTime desiredDate) { this.desiredDate = desiredDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
