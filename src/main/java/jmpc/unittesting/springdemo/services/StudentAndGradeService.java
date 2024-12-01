package jmpc.unittesting.springdemo.services;

import jmpc.unittesting.springdemo.models.CollegeStudent;
import jmpc.unittesting.springdemo.models.Student;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentAndGradeService {

  private final StudentRepository repository;

  @Autowired
  public StudentAndGradeService(StudentRepository repository) {
    this.repository = repository;
  }

  public void createStudent(String firstName, String lastName, String email) {
    var student = new CollegeStudent(firstName, lastName, email);
    student.setId(0);

    repository.save(student);
  }

  public Optional<CollegeStudent> findById(int studentId) {
    return repository.findById(studentId);
  }

  public boolean checkIfStudentIsNull(int studentId) {
    var student = findById(studentId);
    return student.isEmpty();
  }

  public boolean checkIfStudentExists(int studentId) {
    var student = findById(studentId);
    return student.isPresent();
  }

  public void delete(int studentId) {
    repository.deleteById(studentId);
  }

  public Iterable<CollegeStudent> getGradebook() {
    return repository.findAll();
  }
}
