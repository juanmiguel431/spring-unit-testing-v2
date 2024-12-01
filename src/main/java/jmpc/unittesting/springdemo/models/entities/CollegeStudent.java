package jmpc.unittesting.springdemo.models.entities;

import jakarta.persistence.*;
import jmpc.unittesting.springdemo.models.Student;

@Entity
@Table(name = "students")
public class CollegeStudent implements Student {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name="email")
    private String email;

    public CollegeStudent() {

    }

    public CollegeStudent(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String toString() {
        return "CollegeStudent{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String studentInformation() {
       return getFullName() + " " + getEmail();
    }
}
