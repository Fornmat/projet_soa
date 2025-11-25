package fr.insa.ms.reviewMS.controller;

import org.springframework.web.bind.annotation.*;

import fr.insa.ms.reviewMS.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewResource {

    private final String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_056?serverTimezone=UTC&useSSL=false";
    private final String user = "projet_gei_056";
    private final String password = "eNaesh1W";

	public Connection connect() {
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void disconnect(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/{studentId}")
	public String addReview(@PathVariable int studentId, @RequestBody Review review) {
		// Vérifier que le texte de l'avis est non nul et non vide
		if (review.getReviewText() == null || review.getReviewText().trim().isEmpty()) {
			return "Review text cannot be empty.";
		}

		String sql = "INSERT INTO REVIEWS (STUDENT_ID, REVIEW_TEXT) VALUES (?, ?)";

		try (Connection con = connect(); 
			PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setInt(1, studentId);
			stmt.setString(2, review.getReviewText());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				return "Review added successfully.";
			} else {
				return "Failed to add review.";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return "Error occurred while adding review.";
		}
	}

	@GetMapping("/{studentId}")
	public List<String> getReviews(@PathVariable int studentId) {
		List<String> reviews = new ArrayList<>();

		String sql = "SELECT REVIEW_TEXT FROM REVIEWS WHERE STUDENT_ID = ?";

		try (Connection con = connect(); 
			PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setInt(1, studentId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					reviews.add(rs.getString("REVIEW_TEXT"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reviews;
	}
}
