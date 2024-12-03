package jmpc.unittesting.springdemo.services;

import jmpc.unittesting.springdemo.models.*;
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
import java.util.List;
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

  public int deleteGradeById(int gradeId, GradeType type) throws Exception {
    switch (type){
      case MATH: {
        var grade = this.mathGradeRepository.findById(gradeId);
        if (grade.isPresent()) {
          this.mathGradeRepository.deleteById(gradeId);
          return grade.get().getStudentId();
        }
        break;
      }
      case SCIENCE: {
        var grade = this.scienceGradeRepository.findById(gradeId);
        if (grade.isPresent()) {
          this.scienceGradeRepository.deleteById(gradeId);
          return grade.get().getStudentId();
        }
        break;
      }
      case HISTORY: {
        var grade = this.historyGradeRepository.findById(gradeId);
        if (grade.isPresent()) {
          this.historyGradeRepository.deleteById(gradeId);
          return grade.get().getStudentId();
        }
        break;
      }
      default:
        throw new Exception("type not allowed " + type);
    }

    throw new Exception("Grade not found " + gradeId);
  }

  public Iterable<CollegeStudent> findAllStudents() {
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

  public Optional<GradebookCollegeStudent> getInformation(int studentId) {
    var student = studentRepository.findById(studentId);

    if (student.isEmpty()) {
      return Optional.empty();
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
    var gradebookCollegeStudent = new GradebookCollegeStudent(s.getId(), s.getFirstname(), s.getLastname(), s.getEmail(), studentGrades);

    return Optional.of(gradebookCollegeStudent);
  }

  public Optional<StudentInformationDto> getInformationWithAverage(int studentId) {
    var gradebookCollegeStudentOpt = this.getInformation(studentId);

    if (gradebookCollegeStudentOpt.isEmpty()) {
      return Optional.empty();
    }

    var gradebookCollegeStudent = gradebookCollegeStudentOpt.get();
    var result = new StudentInformationDto();
    result.setStudent(gradebookCollegeStudentOpt.get());

    var studentGrades = gradebookCollegeStudent.getStudentGrades();

    var mathGradeResults = studentGrades.getMathGradeResults();
    if (mathGradeResults.isEmpty()) {
      result.setMathAverage("N/A");
    } else {
      var average = studentGrades.findGradePointAverage(mathGradeResults);
      result.setMathAverage(String.format("%.2f", average));
    }

    var scienceGradeResults = studentGrades.getScienceGradeResults();
    if (scienceGradeResults.isEmpty()) {
      result.setScienceAverage("N/A");
    } else {
      var average = studentGrades.findGradePointAverage(scienceGradeResults);
      result.setScienceAverage(String.format("%.2f", average));
    }

    var historyGradeResults = studentGrades.getHistoryGradeResults();
    if (historyGradeResults.isEmpty()) {
      result.setHistoryAverage("N/A");
    } else {
      var average = studentGrades.findGradePointAverage(historyGradeResults);
      result.setHistoryAverage(String.format("%.2f", average));
    }

    return Optional.of(result);
  }

  public Gradebook getGradebook () {

    Iterable<CollegeStudent> collegeStudents = studentRepository.findAll();
    Iterable<MathGrade> mathGrades = mathGradeRepository.findAll();
    Iterable<ScienceGrade> scienceGrades = scienceGradeRepository.findAll();
    Iterable<HistoryGrade> historyGrades = historyGradeRepository.findAll();

    var gradebook = new Gradebook();

    var studentGrades = new StudentGrades();

    for (CollegeStudent collegeStudent : collegeStudents) {
      List<Grade> mathGradesPerStudent = new ArrayList<>();
      List<Grade> scienceGradesPerStudent = new ArrayList<>();
      List<Grade> historyGradesPerStudent = new ArrayList<>();

      for (MathGrade grade : mathGrades) {
        if (grade.getStudentId() == collegeStudent.getId()) {
          mathGradesPerStudent.add(grade);
        }
      }
      for (ScienceGrade grade : scienceGrades) {
        if (grade.getStudentId() == collegeStudent.getId()) {
          scienceGradesPerStudent.add(grade);
        }
      }

      for (HistoryGrade grade : historyGrades) {
        if (grade.getStudentId() == collegeStudent.getId()) {
          historyGradesPerStudent.add(grade);
        }
      }

      studentGrades.setMathGradeResults(mathGradesPerStudent);
      studentGrades.setScienceGradeResults(scienceGradesPerStudent);
      studentGrades.setHistoryGradeResults(historyGradesPerStudent);

      var gradebookCollegeStudent = new GradebookCollegeStudent(
          collegeStudent.getId(),
          collegeStudent.getFirstname(),
          collegeStudent.getLastname(),
          collegeStudent.getEmail(),
          studentGrades);

      gradebook.getStudents().add(gradebookCollegeStudent);
    }

    return gradebook;
  }
}
