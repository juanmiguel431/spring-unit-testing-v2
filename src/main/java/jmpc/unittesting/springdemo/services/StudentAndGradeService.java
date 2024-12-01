package jmpc.unittesting.springdemo.services;

import jmpc.unittesting.springdemo.models.entities.CollegeStudent;
import jmpc.unittesting.springdemo.models.entities.MathGrade;
import jmpc.unittesting.springdemo.repositories.MathGradeRepository;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentAndGradeService {

  private final StudentRepository studentRepository;
  private final MathGradeRepository mathGradeRepository;

  @Autowired
  public StudentAndGradeService(StudentRepository studentRepository, MathGradeRepository mathGradeRepository) {
    this.studentRepository = studentRepository;
    this.mathGradeRepository = mathGradeRepository;
  }

  public void createStudent(String firstName, String lastName, String email) {
    var student = new CollegeStudent(firstName, lastName, email);
    student.setId(0);

    studentRepository.save(student);
  }

  public Optional<CollegeStudent> findById(int studentId) {
    return studentRepository.findById(studentId);
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
    studentRepository.deleteById(studentId);
  }

  public Iterable<CollegeStudent> getGradebook() {
    return studentRepository.findAll();
  }

  public void createGrade(double grade, int studentId, String type) throws Exception {
    var student = findById(studentId);
    if (student.isEmpty()) {
      throw new Exception("Student not found");
    }

    if (grade < 0 || grade > 100) {
      throw new Exception("Invalid grade");
    }

    var mathGrade = new MathGrade();
    mathGrade.setStudentId(studentId);
    mathGrade.setGrade(grade);

    this.mathGradeRepository.save(mathGrade);
  }
}
