package fr.insa.ms.gatewayMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import fr.insa.ms.gatewayMS.model.Student;
import fr.insa.ms.gatewayMS.model.Request;
import fr.insa.ms.gatewayMS.model.RecommendationRequest;
import fr.insa.ms.gatewayMS.model.Response;

import java.util.List;

@RestController
public class GatewayController {

    @Autowired
    private RestTemplate rest;

    private Student currentUser = null;

    private static final String STUDENT_MS = "http://studentManagementMS/students";
    private static final String REQUEST_MS = "http://supportRequestMS/requests";
    private static final String RECOMMEND_MS = "http://recommendationMS/recommend";

    // ---- StudentProxy ----
    private Student studentLogin(String fn, String ln, String email) {
        return rest.getForObject(
            STUDENT_MS + "/login/" + fn + "/" + ln + "/" + email + "/",
            Student.class
        );
    }

    private Student getStudentById(int id) {
        return rest.getForObject(STUDENT_MS + "/id/" + id, Student.class);
    }

    private List<Student> listAllStudents() {
        return rest.getForObject(STUDENT_MS + "/list", List.class);
    }

    private boolean signUpStudent(Student s) {
        return rest.postForObject(STUDENT_MS + "/signUp", s, Boolean.class);
    }

    // ---- RequestProxy ----
    private Request createRequestMS(Request r) {
        return rest.postForObject(REQUEST_MS, r, Request.class);
    }

    private List<Request> listRequestsByRequester(int requesterId) {
        return rest.getForObject(REQUEST_MS + "/requester/" + requesterId, List.class);
    }

    private Request selectHelperForRequest(int requestId, int helperId) {
        // Récupérer la demande
        Request req = rest.getForObject(REQUEST_MS + "/" + requestId, Request.class);
        if (req == null) throw new RuntimeException("Request not found");

        // Mettre à jour le helper
        req.setHelperId(helperId);
        return rest.putForObject(REQUEST_MS + "/" + requestId, req, Request.class);
    }

    private Request updateRequestStatus(int requestId, String newStatusStr) {
        return rest.putForObject(
            REQUEST_MS + "/" + requestId + "/status",
            newStatusStr,
            Request.class
        );
    }

    // ---- RecommendationProxy ----
    private List<Student> recommendHelpers(RecommendationRequest req) {
        return rest.postForObject(
            RECOMMEND_MS + "/best-helpers",
            req,
            List.class
        );
    }


    // --- UTILITAIRES ---
    private void checkLoggedIn() {
        if (currentUser == null) {
            throw new RuntimeException("Action interdite : vous devez être connecté !");
        }
    }

    // ---------------- Gateway Endpoints ----------------

    @PostMapping("/students/signUp")
    public boolean signUp(@RequestBody Student s) {
        return signUpStudent(s);
    }

    @GetMapping("/login/{fn}/{ln}/{email}")
    public Student login(
            @PathVariable String fn,
            @PathVariable String ln,
            @PathVariable String email) {

        Student logged = studentLogin(fn, ln, email);
        if (logged != null) {
            currentUser = logged;
        }

        return logged;
    }

    @PostMapping("/requests")
    public Response createRequest(@RequestBody Request request) {

        // 1. Vérifier que le demandeur existe
        Student requester = getStudentById(request.getRequesterId());
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
                all
        );

        // 5. Appel au moteur de recommandation
        List<Student> recommended = recommendHelpers(recReq);

        // 6. Retour complet
        return new Response(created, recommended);
    }

    @GetMapping("/requests/requester/{id}")
    public List<Request> getRequestsByRequester(@PathVariable int id) {
        return listRequestsByRequester(id);
    }

    @PostMapping("/requests/{requestId}/select-helper/{helperId}")
    public Request selectHelper(@PathVariable int requestId, @PathVariable int helperId) {
        return selectHelperForRequest(requestId, helperId);
    }

    @PostMapping("/requests/{requestId}/update-status/{status}")
    public Request changeRequestStatus(@PathVariable int requestId, @PathVariable String status) {
        return updateRequestStatus(requestId, status);
    }
}
