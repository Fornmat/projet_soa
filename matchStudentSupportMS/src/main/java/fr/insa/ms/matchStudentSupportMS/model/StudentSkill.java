package fr.insa.ms.matchStudentSupportMS.model;

import java.util.List;

public class StudentSkill {

    private int studentId;
    private List<String> skills;

    public StudentSkill() {}

    public StudentSkill(int studentId, List<String> skills) {
        this.studentId = studentId;
        this.skills = skills;
    }

    public int getStudentId() { return studentId; }
    public List<String> getSkills() { return skills; }

    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}

