package jmpc.unittesting.springdemo.models;

import jmpc.unittesting.springdemo.models.entities.CollegeStudent;

public class GradebookCollegeStudent extends CollegeStudent {

    private int id;

    private StudentGrades studentGrades;

    public GradebookCollegeStudent(String firstname, String lastname, String email) {
        super(firstname, lastname, email);
    }

    public GradebookCollegeStudent(int id, String firstname, String lastname, String email) {
        this(firstname, lastname, email);
        this.id = id;
    }

    public GradebookCollegeStudent(int id, String firstname, String lastname, String email, StudentGrades studentGrades) {
        this(id, firstname, lastname, email);
        this.studentGrades = studentGrades;
    }

    public StudentGrades getStudentGrades() {
        return studentGrades;
    }

    public void setStudentGrades(StudentGrades studentGrades) {
        this.studentGrades = studentGrades;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
