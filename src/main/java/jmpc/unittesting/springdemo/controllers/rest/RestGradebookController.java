package jmpc.unittesting.springdemo.controllers.rest;

import jmpc.unittesting.springdemo.exceptionhandling.StudentOrGradeErrorResponse;
import jmpc.unittesting.springdemo.exceptionhandling.StudentOrGradeNotFoundException;
import jmpc.unittesting.springdemo.models.GradeType;
import jmpc.unittesting.springdemo.models.GradebookCollegeStudent;
import jmpc.unittesting.springdemo.models.entities.CollegeStudent;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gradebook")
public class RestGradebookController {

  private final StudentAndGradeService studentAndGradeService;

  @Autowired
  public RestGradebookController(StudentAndGradeService studentAndGradeService) {
    this.studentAndGradeService = studentAndGradeService;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public List<GradebookCollegeStudent> getStudents() {
    var gradebook = studentAndGradeService.getGradebook();
    return gradebook.getStudents();
  }

  @GetMapping("/student-information/{id}")
  public GradebookCollegeStudent studentInformation(@PathVariable int id) {

    var studentEntityOpt = studentAndGradeService.getInformation(id);
    if (studentEntityOpt.isEmpty()) {
      throw new StudentOrGradeNotFoundException("Student or Grade was not found");
    }

    return studentEntityOpt.get();
  }

  @PostMapping(value = "/")
  public List<GradebookCollegeStudent> createStudent(@RequestBody CollegeStudent student) {

    studentAndGradeService.createStudent(student.getFirstname(), student.getLastname(), student.getEmail());
    var gradebook = studentAndGradeService.getGradebook();
    return gradebook.getStudents();
  }

  @DeleteMapping("/student/{id}")
  public List<GradebookCollegeStudent> deleteStudent(@PathVariable int id) throws Exception {

    if (!studentAndGradeService.checkIfStudentIsNull(id)) {
      throw new StudentOrGradeNotFoundException("Student or Grade was not found");
    }

    studentAndGradeService.deleteStudentById(id);
    var gradebook = studentAndGradeService.getGradebook();
    return gradebook.getStudents();
  }

  @PostMapping(value = "/grades")
  public GradebookCollegeStudent createGrade(
      @RequestParam("grade") double grade,
      @RequestParam("gradeType") GradeType gradeType,
      @RequestParam("studentId") int studentId) {

    if (!studentAndGradeService.checkIfStudentIsNull(studentId)) {
      throw new StudentOrGradeNotFoundException("Student or Grade was not found");
    }

    try {
      studentAndGradeService.createGrade(grade, studentId, gradeType);
    } catch (Exception e) {
      throw new StudentOrGradeNotFoundException("Student or Grade was not found");
    }

    var studentEntityOpt = studentAndGradeService.getInformation(studentId);

    if (studentEntityOpt.isEmpty()) {
      throw new StudentOrGradeNotFoundException("Student or Grade was not found");
    }

    return studentEntityOpt.get();
  }

  @DeleteMapping("/grades/{id}/{gradeType}")
  public GradebookCollegeStudent deleteGrade(@PathVariable int id, @PathVariable GradeType gradeType) {
    int studentId;

    try {
      studentId = studentAndGradeService.deleteGradeById(id, gradeType);
    } catch (Exception e) {
      throw new StudentOrGradeNotFoundException("Student or Grade was not found");
    }

    var studentEntityOpt = studentAndGradeService.getInformation(studentId);

    if (studentEntityOpt.isEmpty()) {
      throw new StudentOrGradeNotFoundException("Student was not found");
    }

    return studentEntityOpt.get();
  }

  @ExceptionHandler
  public ResponseEntity<StudentOrGradeErrorResponse> handleException(StudentOrGradeNotFoundException exc) {

    StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

    error.setStatus(HttpStatus.NOT_FOUND.value());
    error.setMessage(exc.getMessage());
    error.setTimeStamp(System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<StudentOrGradeErrorResponse> handleException(Exception exc) {

    StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

    error.setStatus(HttpStatus.BAD_REQUEST.value());
    error.setMessage(exc.getMessage());
    error.setTimeStamp(System.currentTimeMillis());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}
