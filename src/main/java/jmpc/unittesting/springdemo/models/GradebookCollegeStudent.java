package jmpc.unittesting.springdemo.models;

import jmpc.unittesting.springdemo.models.entities.CollegeStudent;

public class GradebookCollegeStudent extends CollegeStudent {
    private StudentGrades studentGrades;

    public GradebookCollegeStudent(int id, String firstname, String lastname, String email, StudentGrades studentGrades) {
        super(firstname, lastname, email);
        this.setId(id);
        this.studentGrades = studentGrades;
    }

    public StudentGrades getStudentGrades() {
        return studentGrades;
    }

    public void setStudentGrades(StudentGrades studentGrades) {
        this.studentGrades = studentGrades;
    }
}
