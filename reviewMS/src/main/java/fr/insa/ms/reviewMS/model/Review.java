package fr.insa.ms.reviewMS.model;

public class Review {

    private int studentId;
    private String reviewText;
    private int reviewerId;

    public Review(int studentId, String reviewText, int reviewerId) {
        this.studentId = studentId;
        this.reviewText = reviewText;
        this.reviewerId = reviewerId;
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

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }
}
