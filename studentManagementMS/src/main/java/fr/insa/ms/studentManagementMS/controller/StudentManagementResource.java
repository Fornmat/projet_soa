package fr.insa.ms.studentManagementMS.controller;

import fr.insa.ms.studentManagementMS.model.Student;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentManagementResource {
	
	private String escape(String input) {
	    if (input == null) return "";
	    return input.replace("'", "''");
	}
	
	
	
	
	public Connection connect() {
		String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_056";
		Connection con = null;
	    try {
	      con = DriverManager.getConnection(url,"projet_gei_056","eNaesh1W");
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } 
	    return con;
	}
	
	public void disconnect(Connection con) {
		try {
		      con.close();
		    } catch (SQLException e) {
		      e.printStackTrace();
		    } 
	}
	
	public ResultSet bddSelect(Statement stmt, String infos, String cible) {
		String requete = "SELECT "+infos+" FROM "+cible;
		ResultSet resultats = null;
		try {
		      resultats = stmt.executeQuery(requete);
		    } catch (SQLException e) {
		      e.printStackTrace();
		}
		return resultats;
	}
	
	public void bddInsert(Statement stmt, String valeurs, String cible) {
		String requete = "INSERT INTO "+cible+" VALUES "+valeurs;
		try {
		      stmt.executeUpdate(requete);
		    } catch (SQLException e) {
		      e.printStackTrace();
		};
	}
	
	public void bddUpdate(Statement stmt,String affectations, String cible, String conditions) {
		String requete = "UPDATE "+cible+" SET "+affectations+ " WHERE "+conditions;
		try {
		      stmt.executeUpdate(requete);
		    } catch (SQLException e) {
		      e.printStackTrace();
		};
	}
	

	@GetMapping("/students/list")
	public List<Student> getListStudents() {

	    List<Student> studentList = new ArrayList<>();
	    ObjectMapper mapper = new ObjectMapper();  // Pour parser les colonnes JSON
	    
	    try (Connection con = connect();
	    		Statement stmt = con.createStatement();
	         ResultSet rs = bddSelect(stmt,"*", "STUDENTS")) {

	        while (rs.next()) {

	            int id = rs.getInt("ID");
	            String firstName = rs.getString("FIRSTNAME");
	            String lastName = rs.getString("LASTNAME");
	            String email = rs.getString("EMAIL");
	            String etablissement = rs.getString("ETABLISSEMENT");
	            String filiere = rs.getString("FILIERE");

	            // Récupération du JSON sous forme de String
	            String competencesJson = rs.getString("COMPETENCES");
	            String disponibilitesJson = rs.getString("DISPONIBILITES");
	            String avisJson = rs.getString("AVIS");

	            // Conversion JSON → ArrayList
	            List<String> competences = mapper.readValue(
	                competencesJson, new TypeReference<List<String>>() {}
	            );

	            List<LocalDateTime> disponibilites = mapper.readValue(
	                disponibilitesJson, new TypeReference<List<LocalDateTime>>() {}
	            );

	            List<String> avis = mapper.readValue(
	                avisJson, new TypeReference<List<String>>() {}
	            );

	            Student student = new Student(
	                    id, firstName, lastName, email,
	                    etablissement, filiere,
	                    new ArrayList<>(competences),
	                    new ArrayList<>(disponibilites),
	                    new ArrayList<>(avis)
	            );

	            studentList.add(student);
	        }
	        stmt.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return studentList;
	}
	
	
	@PostMapping("/students/signUp")
	public boolean signUp(@RequestBody Student student) {
		
		Connection con = connect();
		boolean signUpOk = true;
		
	    try  {

	        ObjectMapper mapper = new ObjectMapper();

	        // Convertir les listes en JSON
	        String competencesJson = mapper.writeValueAsString(student.getCompetences());
	        String disponibilitesJson = mapper.writeValueAsString(student.getDisponibilites());
	        String avisJson = mapper.writeValueAsString(student.getAvis());

	        // Échappement des chaînes pour les mettre entre quotes SQL
	        String fn = escape(student.getFirstName());
	        String ln = escape(student.getLastName());
	        String email = escape(student.getEmail());
	        String etab = escape(student.getEtablissement());
	        String filiere = escape(student.getFiliere());

	        String comp = escape(competencesJson);
	        String disp = escape(disponibilitesJson);
	        String av = escape(avisJson);
	        Statement stmt = con.createStatement();
	        ResultSet verif = bddSelect(stmt,"EMAIL","STUDENTS WHERE EMAIL='"+email+"'");
	        if (verif.next()) {
	        	signUpOk=false;
	        } else {
	        	// IMPORTANT : id est auto-incrément → mettre NULL
		        String valeurs = String.format(
		                "('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
		                fn, ln, email, etab, filiere, comp, disp, av
		        );
	
		        // Appel de ta fonction custom
		        bddInsert(stmt,valeurs, "STUDENTS (FIRSTNAME, LASTNAME, EMAIL, ETABLISSEMENT, FILIERE, COMPETENCES, DISPONIBILITES, AVIS)");
	
	        }
	        stmt.close();

	        

	    } catch (Exception e) {
	    	signUpOk=false;
	        e.printStackTrace();
	    }
	    disconnect(con);
	    return signUpOk;
	}
	
	@PostMapping("students/createTable")
	public boolean createTable() {
		String requete = "CREATE TABLE STUDENTS (ID int AUTO_INCREMNT PRIMARY KEY, FIRSTNAME varchar(30), LASTNAME varchar(30), EMAIL varchar(30), ETABLISSEMENT varchar(30), FILIERE varchar(30), COMPETENCES varchar(511), DISPONIBILITES varchar(511), AVIS varchar(511))";
		Connection con = connect();
		try {
		      Statement stmt = con.createStatement();
		      stmt.executeUpdate(requete);
		      stmt.close();
		      disconnect(con);
		      return true;
		    } catch (SQLException e) {
		    	disconnect(con);
		      e.printStackTrace();
		      return false;
		}
	}
	
	
	@PutMapping("/students/update/{firstName}/{last_name}/{email}/")
	public boolean updateProfile(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String email, @RequestBody Student student) {
		
		Connection con = connect();
		
		try {

	        ObjectMapper mapper = new ObjectMapper();

	        // Convertir les listes en JSON
	        String competencesJson = mapper.writeValueAsString(student.getCompetences());
	        String disponibilitesJson = mapper.writeValueAsString(student.getDisponibilites());
	        String avisJson = mapper.writeValueAsString(student.getAvis());

	        // Échappement des chaînes pour les mettre entre quotes SQL
	        String fn = escape(student.getFirstName());
	        String ln = escape(student.getLastName());
	        String email1 = escape(student.getEmail());
	        String etab = escape(student.getEtablissement());
	        String filiere = escape(student.getFiliere());

	        String comp = escape(competencesJson);
	        String disp = escape(disponibilitesJson);
	        String av = escape(avisJson);

	        // IMPORTANT : id est auto-incrément → mettre NULL
	        String affectations = String.format(
	                "FIRSTNAME='%s', LASTNAME='%s', EMAIL='%s', ETABLISSEMENT='%s', FILIERE='%s', COMPETENCES='%s', DISPONIBILITES='%s', AVIS='%s')",
	                fn, ln, email1, etab, filiere, comp, disp, av
	        );
	        
	        String conditions = String.format(
	                "FIRSNAME='%s', LASTNAME='%s', EMAIL='%s'",
	                firstName, lastName, email
	        );

	        // Appel de ta fonction custom
	        Statement stmt = con.createStatement();
	        bddUpdate(stmt,affectations, "STUDENTS", conditions);
	        stmt.close();
	        disconnect(con);
	        return true;


	    
		} catch (Exception e) {
			disconnect(con);
			e.printStackTrace();
			System.out.println("update failed");
			return false;
		}

        
	}
	
	@GetMapping("/students/login/{firstName}/{last_name}/{email}/")
	public Student login(@PathVariable String firstName, @PathVariable String lastName, @PathVariable String email) {
		
		
		Student student= new Student();
		ObjectMapper mapper = new ObjectMapper();
		try (Connection con = connect();
				Statement stmt = con.createStatement();
		ResultSet rs = bddSelect(stmt,"*", "STUDENTS WHERE FIRSTNAME="+firstName+" AND LASTNAME="+lastName+" AND EMAIL="+email)) {
			if (rs.next()) {
				
	            int id = rs.getInt("ID");
	            String firstName1 = rs.getString("FIRSTNAME");
	            String lastName1 = rs.getString("LASTNAME");
	            String email1 = rs.getString("EMAIL");
	            String etablissement = rs.getString("ETABLISSEMENT");
	            String filiere = rs.getString("FILIERE");
	
	            // Récupération du JSON sous forme de String
	            String competencesJson = rs.getString("COMPETENCES");
	            String disponibilitesJson = rs.getString("DISPONIBILITES");
	            String avisJson = rs.getString("AVIS");
	
	            // Conversion JSON → ArrayList
	            List<String> competences = mapper.readValue(
	                competencesJson, new TypeReference<List<String>>() {}
	            );
	
	            List<LocalDateTime> disponibilites = mapper.readValue(
	                disponibilitesJson, new TypeReference<List<LocalDateTime>>() {}
	            );
	
	            List<String> avis = mapper.readValue(
	                avisJson, new TypeReference<List<String>>() {}
	            );
	
	            student = new Student(
	                    id, firstName1, lastName1, email1,
	                    etablissement, filiere,
	                    new ArrayList<>(competences),
	                    new ArrayList<>(disponibilites),
	                    new ArrayList<>(avis)
	            );
	            System.out.println("Bienvenue "+firstName1+" "+lastName1+" !");
			} else {
				System.out.println("Vous n'êtes pas inscrit");
			}
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

        
		return student;
	}
	
	@GetMapping("/students/{id}")
	public Student getStudentFromId(@PathVariable int id) {
		
		
		Student student= new Student();
		ObjectMapper mapper = new ObjectMapper();
		try (Connection con = connect();
				Statement stmt = con.createStatement();
		ResultSet rs = bddSelect(stmt,"*", "STUDENTS WHERE ID='"+id+"'")) {
			if (rs.next()) {
				
	            String firstName = rs.getString("FIRSTNAME");
	            String lastName = rs.getString("LASTNAME");
	            String email = rs.getString("EMAIL");
	            String etablissement = rs.getString("ETABLISSEMENT");
	            String filiere = rs.getString("FILIERE");
	
	            // Récupération du JSON sous forme de String
	            String competencesJson = rs.getString("COMPETENCES");
	            String disponibilitesJson = rs.getString("DISPONIBILITES");
	            String avisJson = rs.getString("AVIS");
	
	            // Conversion JSON → ArrayList
	            List<String> competences = mapper.readValue(
	                competencesJson, new TypeReference<List<String>>() {}
	            );
	
	            List<LocalDateTime> disponibilites = mapper.readValue(
	                disponibilitesJson, new TypeReference<List<LocalDateTime>>() {}
	            );
	
	            List<String> avis = mapper.readValue(
	                avisJson, new TypeReference<List<String>>() {}
	            );
	
	            student = new Student(
	                    id, firstName, lastName, email,
	                    etablissement, filiere,
	                    new ArrayList<>(competences),
	                    new ArrayList<>(disponibilites),
	                    new ArrayList<>(avis)
	            );
	            System.out.println("Bienvenue "+firstName+" "+lastName+" !");
			} else {
				System.out.println("Vous n'êtes pas inscrit");
			}
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

        
		return student;
	}
	
	

	
}
