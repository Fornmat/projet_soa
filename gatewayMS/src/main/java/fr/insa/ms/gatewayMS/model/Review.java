package fr.insa.ms.gatewayMS.model;

public class Review {

    private int studentId;
    private String reviewText;


    public Review(int studentId, String reviewText) {
        this.studentId = studentId;
        this.reviewText = reviewText;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReview(String reviewText) {
        this.reviewText = reviewText;
    }
}
