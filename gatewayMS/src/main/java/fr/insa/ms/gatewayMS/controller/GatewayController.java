package fr.insa.ms.gatewayMS.controller;

import fr.insa.ms.gatewayMS.model.Student;
import fr.insa.ms.gatewayMS.model.Request;
import fr.insa.ms.gatewayMS.model.Response;
import fr.insa.ms.gatewayMS.model.Review;
import fr.insa.ms.gatewayMS.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    private Student currentUser = null;

    // --- Gateway Endpoints ---

    @PostMapping("/students/signUp")
    public boolean signUp(@RequestBody Student s) {
        return gatewayService.signUpStudent(s);
    }

    @GetMapping("/login/{fn}/{ln}/{email}")
    public Student login(
            @PathVariable String fn,
            @PathVariable String ln,
            @PathVariable String email) {

        Student logged = gatewayService.studentLogin(fn, ln, email);
        if (logged != null) {
            currentUser = logged;
        }

        return logged;
    }

    @PostMapping("/requests")
    public Response createRequest(@RequestBody Request request) {
        checkLoggedIn();
        return gatewayService.createRequest(request, currentUser);
    }

    @GetMapping("/requests/{requestId}")
    public Request getRequestsByHelper(@PathVariable int requestId) {
        checkLoggedIn();
        return gatewayService.getRequestById(requestId);
    }

    @GetMapping("/requests/requester")
    public List<Request> getRequestsByRequester() {
        checkLoggedIn();
        return gatewayService.listRequestsByRequester(currentUser.getId());
    }

    @GetMapping("/requests/helper")
    public List<Request> getRequestsByHelper() {
        checkLoggedIn();
        return gatewayService.listRequestsByHelper(currentUser.getId());
    }

    @PostMapping("/requests/requester/{requestId}/select-helper/{helperId}")
    public Request selectHelper(@PathVariable int requestId, @PathVariable int helperId) {
        checkLoggedIn();
        return gatewayService.selectHelperForRequest(requestId, helperId);
    }

    @PostMapping("/requests/{requestId}/update-status/{status}")
    public Request changeRequestStatus(@PathVariable int requestId, @PathVariable String status) {
        checkLoggedIn();
        return gatewayService.updateRequestStatus(requestId, status);
    }

    @PostMapping("/reviews/{studentId}")
    public String addReview(@PathVariable int studentId, @RequestBody Review review) {
        checkLoggedIn();
        return gatewayService.addReview(studentId, review);
    }

    @GetMapping("/reviews/{studentId}")
    public List<Review> getReviews(@PathVariable int studentId) {
        return gatewayService.getReviews(studentId);
    }

    // Utility method
    private void checkLoggedIn() {
        if (currentUser == null) {
            throw new RuntimeException("Action interdite : vous devez être connecté !");
        }
    }
}
