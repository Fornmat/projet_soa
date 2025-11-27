package fr.insa.ms.gatewayMS.service;

import fr.insa.ms.gatewayMS.model.Student;
import fr.insa.ms.gatewayMS.model.Request;
import fr.insa.ms.gatewayMS.model.Response;
import fr.insa.ms.gatewayMS.model.Review;
import fr.insa.ms.gatewayMS.model.RecommendationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GatewayService {

    @Autowired
    private RestTemplate rest;

    private static final String STUDENT_MS = "http://studentManagementMS/students";
    private static final String REQUEST_MS = "http://supportRequestMS/requests";
    private static final String RECOMMEND_MS = "http://recommendationMS/recommend";
    private static final String REVIEW_MS = "http://reviewMS/reviews";

    // ---- StudentProxy ----
    public Student studentLogin(String fn, String ln, String email) {
        return rest.getForObject(STUDENT_MS + "/login/" + fn + "/" + ln + "/" + email, Student.class);
    }

    public Student getStudentById(int id) {
        return rest.getForObject(STUDENT_MS + "/id/" + id, Student.class);
    }

    public List<Student> listAllStudents() {
        return rest.getForObject(STUDENT_MS + "/list", List.class);
    }

    public boolean signUpStudent(Student s) {
        return rest.postForObject(STUDENT_MS + "/signUp", s, Boolean.class);
    }

    // ---- RequestProxy ----
    public Request createRequestMS(Request r) {
        return rest.postForObject(REQUEST_MS, r, Request.class);
    }

    public Request getRequestById(int id) {
        return rest.getForObject(REQUEST_MS + "/" + id, Request.class);
    }

    public List<Request> listRequestsByRequester(int requesterId) {
        return rest.getForObject(REQUEST_MS + "/requester/" + requesterId, List.class);
    }

    public List<Request> listRequestsByHelper(int helperId) {
        return rest.getForObject(REQUEST_MS + "/helper/" + helperId, List.class);
    }

    public Request selectHelperForRequest(int requestId, int helperId) {
        Request req = rest.getForObject(REQUEST_MS + "/" + requestId, Request.class);
        if (req == null)
            throw new RuntimeException("Request not found");

        req.setHelperId(helperId);
        return rest.postForObject(REQUEST_MS + "/" + requestId, req, Request.class);
    }

    public Request updateRequestStatus(int requestId, String newStatusStr) {
        return rest.patchForObject(REQUEST_MS + "/" + requestId + "/status", newStatusStr, Request.class);
    }

    // ---- RecommendationProxy ----
    public List<Student> recommendHelpers(RecommendationRequest req) {
        return rest.postForObject(RECOMMEND_MS + "/best-helpers", req, List.class);
    }

    // ---- ReviewProxy ----
    public String addReview(int studentId, Review review) {
        return rest.postForObject(REVIEW_MS + "/" + studentId, review, String.class);
    }

    public List<Review> getReviews(int studentId) {
        return rest.getForObject(REVIEW_MS + "/" + studentId, List.class);
    }

    public Response createRequest(Request request, Student requester) {
        // 1. Vérifier que le demandeur existe
        if (requester == null) {
            throw new RuntimeException("Erreur : étudiant demandeur introuvable.");
        }

        // 2. Création dans supportRequestMS
        Request created = createRequestMS(request);

        // 3. Récupérer tous les étudiants
        List<Student> all = listAllStudents();

        // 4. Construire la requête de recommandation
        RecommendationRequest recReq = new RecommendationRequest(
                request.getKeywords(),
                request.getDesiredDate(),
                requester,
                all);

        // 5. Appel au moteur de recommandation
        List<Student> recommended = recommendHelpers(recReq);

        // 6. Retour complet
        return new Response(created, recommended);
    }
}
