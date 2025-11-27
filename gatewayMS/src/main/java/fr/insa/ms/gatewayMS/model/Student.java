package fr.insa.ms.gatewayMS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;


//@XmlRootElement(name = "Student")
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
	
	public Student(int id, String lastName, String firstName,String email, String etablissement, String filiere) {
		this.id=id;
		this.lastName=lastName;
		this.firstName=firstName;
		this.email=email;
		this.etablissement=etablissement;
		this.filiere=filiere;
		this.competences = new ArrayList<String>();
		this.disponibilites = new ArrayList<LocalDateTime>();
		this.avis = new ArrayList<String>();
		
	}
	
	public Student(int id, String lastName, String firstName,String email, String etablissement, String filiere, ArrayList<String> competences, ArrayList<LocalDateTime> disponibilites, ArrayList<String> avis) {
		this.id=id;
		this.lastName=lastName;
		this.firstName=firstName;
		this.email=email;
		this.etablissement=etablissement;
		this.filiere=filiere;
		this.competences = competences;
		this.disponibilites = disponibilites;
		this.avis = avis;
		
	}
	
	public Student() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName=lastName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName=firstName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	public String getEtablissement() {
		return this.etablissement;
	}
	
	public void setEtablissement(String etablissement) {
		this.etablissement=etablissement;
	}
	
	public String getFiliere() {
		return this.filiere;
	}
	
	public void setFiliere(String filiere) {
		this.filiere=filiere;
	}
	
	public ArrayList<String> getCompetences() {
		return this.competences;
	}
	
	public void setCompetences(ArrayList<String> competences) {
		this.competences=competences;
	}
	
	public void addCompetence(String competence) {
		this.competences.add(competence);
	}
	
	public void delCompetence(String competence) {
		this.competences.remove(competence);
	}
	
	public ArrayList<LocalDateTime> getDisponibilites() {
		return this.disponibilites;
	}
	
	public void setDisponibilites(ArrayList<LocalDateTime> disponibilites) {
		this.disponibilites=disponibilites;
	}
	
	public void addDisponibilite(LocalDateTime date) {
		this.disponibilites.add(date);
	}
	
	public void delDisponibilite(LocalDateTime date) {
		this.disponibilites.remove(date);
	}
	
	public ArrayList<String> getAvis() {
		return this.avis;
	}
	
	public void setAvis(ArrayList<String> avis) {
		this.avis=avis;
	}
	
	public void addAvis(String commentaire) {
		this.avis.add(commentaire);
	}
	
}