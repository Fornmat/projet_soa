package fr.insa.ms.matchStudentSupportMS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Student {

    private int id;
    private String lastName;
    private String firstName;
    private String email;
    private String etablissement;
    private String filiere;
    private ArrayList<String> competences;
    private ArrayList<LocalDateTime> disponibilites;
    private ArrayList<String> avis;

    public Student() {}

    public int getId() { return id; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getEmail() { return email; }
    public String getEtablissement() { return etablissement; }
    public String getFiliere() { return filiere; }
    public ArrayList<String> getCompetences() { return competences; }
    public ArrayList<LocalDateTime> getDisponibilites() { return disponibilites; }
    public ArrayList<String> getAvis() { return avis; }
}
