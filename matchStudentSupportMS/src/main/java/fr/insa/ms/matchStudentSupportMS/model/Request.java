package fr.insa.ms.matchStudentSupportMS.model;

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
        FERMEE
    }
    
    public Request() {}

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public ArrayList<String> getKeywords() { return keywords; }
    public int getRequesterId() { return requesterId; }
    public int getHelperId() { return helperId; }
    public LocalDateTime getDesiredDate() { return desiredDate; }
    public Status getStatus() { return status; }

}
