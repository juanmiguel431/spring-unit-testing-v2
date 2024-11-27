package jmpc.unittesting.springdemo.services;

import jmpc.unittesting.springdemo.models.CollegeStudent;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentAndGradeService {

  private final StudentRepository repository;

  @Autowired
  public StudentAndGradeService(StudentRepository repository) {
    this.repository = repository;
  }

  public void createStudent(String firstName, String lastName, String email) {
    CollegeStudent student = new CollegeStudent(firstName, lastName, email);
    student.setId(0);

    repository.save(student);
  }
}
