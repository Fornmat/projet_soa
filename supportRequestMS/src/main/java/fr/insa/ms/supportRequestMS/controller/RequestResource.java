package fr.insa.ms.supportRequestMS.controller;

import fr.insa.ms.supportRequestMS.model.Request;
import fr.insa.ms.supportRequestMS.model.Request.Status;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/requests")
public class RequestResource {

    private final String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_056";
    private final String user = "projet_gei_056";
    private final String password = "eNaesh1W";

    @PostMapping
    public Request createRequest(@RequestBody Request request) {
        String sql = "INSERT INTO REQUESTS (TITLE, DESCRIPTION, REQUESTER_ID, HELPER_ID, DESIRED_DATE, STATUS, KEYWORDS) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, request.getTitle());
            stmt.setString(2, request.getDescription());
            stmt.setInt(3, request.getRequesterId());
            stmt.setInt(4, request.getHelperId());
            stmt.setTimestamp(5, Timestamp.valueOf(request.getDesiredDate()));
            stmt.setString(6, request.getStatus() != null ? request.getStatus().name() : Request.Status.ATTENTE.name());
            stmt.setString(7, request.getKeywords() != null ? String.join(",", request.getKeywords()) : null);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    request.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return request;
    }

	
	@GetMapping
	public ArrayList<Request> getAllRequests() {
	    ArrayList<Request> requests = new ArrayList<>();
	    String sql = "SELECT * FROM REQUESTS";
	
	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	
	        while (rs.next()) {
	            Request r = new Request();
	            r.setId(rs.getInt("id"));
	            r.setTitle(rs.getString("TITLE"));
	            r.setDescription(rs.getString("DESCRIPTION"));
	            r.setRequesterId(rs.getInt("REQUESTER_ID"));
	            r.setHelperId(rs.getInt("HELPER_ID"));
	            r.setDesiredDate(rs.getTimestamp("DESIRED_DATE").toLocalDateTime());
	            r.setStatus(Request.Status.valueOf(rs.getString("STATUS")));
	
	            String keywordsStr = rs.getString("KEYWORDS");
	            ArrayList<String> keywords = new ArrayList<>();
	            if (keywordsStr != null && !keywordsStr.isEmpty()) {
	                keywords.addAll(Arrays.asList(keywordsStr.split(",")));
	            }
	            r.setKeywords(keywords);
	
	            requests.add(r);
	        }
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return requests;
	}
	
	@GetMapping("/{id}")
	public Request getRequestById(@PathVariable int id) {
	    Request r = null;
	    String sql = "SELECT * FROM REQUESTS WHERE id = ?";
	
	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	
	        stmt.setInt(1, id);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                r = new Request();
	                r.setId(rs.getInt("id"));
	                r.setTitle(rs.getString("TITLE"));
	                r.setDescription(rs.getString("DESCRIPTION"));
	                r.setRequesterId(rs.getInt("REQUESTER_ID"));
	                r.setHelperId(rs.getInt("HELPER_ID"));
	                r.setDesiredDate(rs.getTimestamp("DESIRED_DATE").toLocalDateTime());
	                r.setStatus(Request.Status.valueOf(rs.getString("STATUS")));
	
	                String keywordsStr = rs.getString("KEYWORDS");
	                ArrayList<String> keywords = new ArrayList<>();
	                if (keywordsStr != null && !keywordsStr.isEmpty()) {
	                    keywords.addAll(Arrays.asList(keywordsStr.split(",")));
	                }
	                r.setKeywords(keywords);
	            }
	        }
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return r;
	}
	
	@GetMapping("/requester/{requesterId}")
	public ArrayList<Request> getRequestsByRequesterId(@PathVariable int requesterId) {
		ArrayList<Request> requests = new ArrayList<>();
	    String sql = "SELECT * FROM REQUESTS WHERE REQUESTER_ID = ?";

	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, requesterId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Request r = new Request();
	                r.setId(rs.getInt("id"));
	                r.setTitle(rs.getString("TITLE"));
	                r.setDescription(rs.getString("DESCRIPTION"));
	                r.setRequesterId(rs.getInt("REQUESTER_ID"));
	                r.setHelperId(rs.getInt("HELPER_ID"));
	                r.setDesiredDate(rs.getTimestamp("DESIRED_DATE").toLocalDateTime());
	                r.setStatus(Request.Status.valueOf(rs.getString("STATUS")));

	                String keywordsStr = rs.getString("KEYWORDS");
	                ArrayList<String> keywords = new ArrayList<>();
	                if (keywordsStr != null && !keywordsStr.isEmpty()) {
	                    keywords.addAll(Arrays.asList(keywordsStr.split(",")));
	                }
	                r.setKeywords(keywords);

	                requests.add(r);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return requests;
	}

	@GetMapping("/helper/{helperId}")
	public ArrayList<Request> getRequestsByHelperId(@PathVariable int helperId) {
		ArrayList<Request> requests = new ArrayList<>();
	    String sql = "SELECT * FROM REQUESTS WHERE HELPER_ID = ?";

	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, helperId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Request r = new Request();
	                r.setId(rs.getInt("id"));
	                r.setTitle(rs.getString("TITLE"));
	                r.setDescription(rs.getString("DESCRIPTION"));
	                r.setRequesterId(rs.getInt("REQUESTER_ID"));
	                r.setHelperId(rs.getInt("HELPER_ID"));
	                r.setDesiredDate(rs.getTimestamp("DESIRED_DATE").toLocalDateTime());
	                r.setStatus(Request.Status.valueOf(rs.getString("STATUS")));

	                String keywordsStr = rs.getString("KEYWORDS");
	                ArrayList<String> keywords = new ArrayList<>();
	                if (keywordsStr != null && !keywordsStr.isEmpty()) {
	                    keywords.addAll(Arrays.asList(keywordsStr.split(",")));
	                }
	                r.setKeywords(keywords);

	                requests.add(r);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return requests;
	}

	@PutMapping("/{id}")
	public Request updateRequest(@PathVariable int id, @RequestBody Request request) {
	    String sql = "UPDATE REQUESTS SET TITLE = ?, DESCRIPTION = ?, REQUESTER_ID = ?, HELPER_ID = ?, DESIRED_DATE = ?, STATUS = ?, KEYWORDS = ? WHERE id = ?";
	
	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	
	        stmt.setString(1, request.getTitle());
	        stmt.setString(2, request.getDescription());
	        stmt.setInt(3, request.getRequesterId());
	        stmt.setInt(4, request.getHelperId());
	        stmt.setTimestamp(5, Timestamp.valueOf(request.getDesiredDate()));
	        stmt.setString(6, request.getStatus().name());
	        stmt.setString(7, request.getKeywords() != null ? String.join(",", request.getKeywords()) : null);
	        stmt.setInt(8, id);
	
	        stmt.executeUpdate();
	        request.setId(id);
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return request;
	}
	
	@PutMapping("/{id}/status")
	public Request updateStatus(@PathVariable int id, @RequestBody String newStatusStr) {
	    Request r = null;
	
	    Status newStatus;
	    try {
	        newStatus = Status.valueOf(newStatusStr);
	    } catch (IllegalArgumentException e) {
	        throw new RuntimeException("Invalid status value");
	    }
	
	    String selectSql = "SELECT id, STATUS FROM REQUESTS WHERE id = ?";
	    String updateSql = "UPDATE REQUESTS SET STATUS = ? WHERE id = ?";
	
	    try (Connection conn = DriverManager.getConnection(url, user, password)) {
	
	        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
	            stmt.setInt(1, id);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    r = new Request();
	                    r.setId(rs.getInt("id"));
	                    r.setStatus(Status.valueOf(rs.getString("STATUS")));
	                } else {
	                    throw new RuntimeException("Request not found");
	                }
	            }
	        }
	
	        if (!r.getStatus().canTransitionTo(newStatus)) {
	            throw new RuntimeException("Invalid status transition");
	        }
	
	        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
	            stmt.setString(1, newStatus.name());
	            stmt.setInt(2, id);
	            stmt.executeUpdate();
	        }
	
	        r.setStatus(newStatus);
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Database error");
	    }
	
	    return r;
	}
	
	@DeleteMapping("/{id}")
	public boolean deleteRequest(@PathVariable int id) {
	    String sql = "DELETE FROM REQUESTS WHERE id = ?";
	
	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	
	        stmt.setInt(1, id);
	        int rowsAffected = stmt.executeUpdate();
	
	        return rowsAffected > 0;
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
