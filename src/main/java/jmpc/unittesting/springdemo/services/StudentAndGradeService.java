package jmpc.unittesting.springdemo.services;

import jmpc.unittesting.springdemo.models.Grade;
import jmpc.unittesting.springdemo.models.GradeType;
import jmpc.unittesting.springdemo.models.GradebookCollegeStudent;
import jmpc.unittesting.springdemo.models.StudentGrades;
import jmpc.unittesting.springdemo.models.entities.CollegeStudent;
import jmpc.unittesting.springdemo.models.entities.HistoryGrade;
import jmpc.unittesting.springdemo.models.entities.MathGrade;
import jmpc.unittesting.springdemo.models.entities.ScienceGrade;
import jmpc.unittesting.springdemo.repositories.HistoryGradeRepository;
import jmpc.unittesting.springdemo.repositories.MathGradeRepository;
import jmpc.unittesting.springdemo.repositories.ScienceGradeRepository;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StudentAndGradeService {

  private final StudentRepository studentRepository;
  private final MathGradeRepository mathGradeRepository;
  private final HistoryGradeRepository historyGradeRepository;
  private final ScienceGradeRepository scienceGradeRepository;

  @Autowired
  public StudentAndGradeService(StudentRepository studentRepository, MathGradeRepository mathGradeRepository, HistoryGradeRepository historyGradeRepository, ScienceGradeRepository scienceGradeRepository) {
    this.studentRepository = studentRepository;
    this.mathGradeRepository = mathGradeRepository;
    this.historyGradeRepository = historyGradeRepository;
    this.scienceGradeRepository = scienceGradeRepository;
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

  public void deleteStudentById(int studentId) throws Exception {
    studentRepository.deleteById(studentId);

    var mathGrades = mathGradeRepository.findMathGradeByStudentId(studentId);
    var scienceGrades = scienceGradeRepository.findScienceGradeByStudentId(studentId);
    var historyGrades = historyGradeRepository.findHistoryGradeGradeByStudentId(studentId);

    for (var grade : mathGrades) {
      deleteGradeById(grade.getId(), GradeType.MATH);
    }

    for (var grade : historyGrades) {
      deleteGradeById(grade.getId(), GradeType.HISTORY);
    }

    for (var grade : scienceGrades) {
      deleteGradeById(grade.getId(), GradeType.SCIENCE);
    }
  }

  public void deleteGradeById(int gradeId, GradeType type) throws Exception {
    switch (type){
      case MATH:
        this.mathGradeRepository.deleteById(gradeId);
        break;
      case SCIENCE:
        this.scienceGradeRepository.deleteById(gradeId);
        break;
      case HISTORY:
        this.historyGradeRepository.deleteById(gradeId);
        break;
      default:
        throw new Exception("type not allowed " + type);
    }
  }

  public Iterable<CollegeStudent> getGradebook() {
    return studentRepository.findAll();
  }

  public void createGrade(double grade, int studentId, GradeType type) throws Exception {
    var student = findById(studentId);
    if (student.isEmpty()) {
      throw new Exception("Student not found");
    }

    if (grade < 0 || grade > 100) {
      throw new Exception("Invalid grade");
    }

    switch (type){
      case MATH -> {
        var classGrade = new MathGrade();
        classGrade.setStudentId(studentId);
        classGrade.setGrade(grade);

        this.mathGradeRepository.save(classGrade);
        break;
      }
      case SCIENCE -> {
        var classGrade = new ScienceGrade();
        classGrade.setStudentId(studentId);
        classGrade.setGrade(grade);

        this.scienceGradeRepository.save(classGrade);
        break;
      }
      case HISTORY -> {
        var classGrade = new HistoryGrade();
        classGrade.setStudentId(studentId);
        classGrade.setGrade(grade);

        this.historyGradeRepository.save(classGrade);
        break;
      }
      default -> {
        throw new Exception("type not allowed " + type);
      }
    }
  }

  public GradebookCollegeStudent getInformation(int studentId) throws Exception {
    Optional<CollegeStudent> student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      throw new Exception("Student not found");
    }

    var mathGrades = mathGradeRepository.findMathGradeByStudentId(studentId);
    var scienceGrades = scienceGradeRepository.findScienceGradeByStudentId(studentId);
    var historyGrades = historyGradeRepository.findHistoryGradeGradeByStudentId(studentId);

    var mathGradeList = new ArrayList<Grade>();
    mathGrades.forEach(mathGradeList::add);

    var scienceGradeList = new ArrayList<Grade>();
    scienceGrades.forEach(scienceGradeList::add);

    var historyGradeList = new ArrayList<Grade>();
    historyGrades.forEach(historyGradeList::add);

    var studentGrades = new StudentGrades();
    studentGrades.setMathGradeResults(mathGradeList);
    studentGrades.setScienceGradeResults(scienceGradeList);
    studentGrades.setHistoryGradeResults(historyGradeList);

    var s = student.get();
    return new GradebookCollegeStudent(s.getId(), s.getFirstname(), s.getLastname(), s.getEmail(), studentGrades);
  }
}
