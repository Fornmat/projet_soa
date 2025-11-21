package fr.insa.ms.studentManagementMS.controller;

import fr.insa.ms.studentManagementMS.model.Student;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class StudentManagementResource {
	
	public Connection connect() {
		String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_56";
		Connection con = null;
	    try {
	      con = DriverManager.getConnection(url,"projet_gei_56","eNaesh1W");
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
	
	public ResultSet bddSelect(String infos, String cible,Connection con) {
		String requete = "SELECT "+infos+" FROM "+cible;
		ResultSet resultats = null;
		try {
		      Statement stmt = con.createStatement();
		      resultats = stmt.executeQuery(requete);
		    } catch (SQLException e) {
		      e.printStackTrace();
		}
		return resultats;
	}
	
	public void bddInsert(String valeurs, String cible,Connection con) {
		String requete = "INSERT INTO "+cible+" VALUES "+valeurs;
		try {
		      Statement sntmt = con.createStatement();
		      int nbMaj = stmt.executeUpdate(requete);
		    } catch (SQLException e) {
		      e.printStackTrace();
		};
	}

	@GetMapping("/students/list")
	public ArrayList<Student> getListStudents() {
		ArrayList<Student> studentList = new ArrayList();
		Connection con= connect();
		ResultSet resultats = bddSelect("*","STUDENTS",con);
		try {
		      ResultSetMetaData rsmd = resultats.getMetaData();
		      int nbCols = rsmd.getColumnCount();
		      boolean encore = resultats.next();

		      while (encore) {

		        int id = resultats.getInt(1);
		        String firstName = resultats.getString(2);
		        String lastName = resultats.getString(3);
		        String email = resultats.getString(4);
		        String etablissement = resultats.getString(5);
		        String filiere = resultats.getString(6);
		        ArrayList<String> competences = (ArrayList<String>) resultats.getObject(7);
		        ArrayList<LocalDateTime> disponibilites= (ArrayList<LocalDateTime>) resultats.getObject(8);
		        HashMap<String,String> avis = (HashMap<String,String>) resultats.getObject(9);
		        Student student = new Student(id,firstName,lastName,email,etablissement,filiere,competences,disponibilites,avis);
		        studentList.add(student);
		        System.out.println("Etudiant --------- id: "+id+" firsName: "+firstName+" lastName: "+lastName);
		        encore = resultats.next();
		      }

		        resultats.close();
		    } catch (SQLException e) {
		      e.printStackTrace();
		}
		return studentList;
	}
	
	@GetMapping("/login")
	public Student getArticleFromStock(int id) {
		Student student = new Student();
		return student;
	}
	
	@PostMapping("/inventory/add/{id}/{name}/{qty}")
	public void addToStock(@PathVariable int id, @PathVariable String name, @PathVariable int qty) {
		stock.addArticle(new Article(id, name, qty));
	}
	
	@PutMapping("/inventory/update/{id}/{qty}")
	public void updateStock(@PathVariable int id, @PathVariable int qty) {
		stock.updateArticle(id, qty);
	}
	
	@DeleteMapping("/inventory/remove/{id}")
	public void removeFromStock(@PathVariable int id) {
		stock.removeArticle(id);
	}

	
}
