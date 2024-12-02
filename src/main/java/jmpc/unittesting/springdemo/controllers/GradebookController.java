package jmpc.unittesting.springdemo.controllers;

import jmpc.unittesting.springdemo.models.GradeType;
import jmpc.unittesting.springdemo.models.entities.CollegeStudent;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

  @Autowired
  private StudentAndGradeService studentAndGradeService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String get(Model m) {
    var collegeStudents = studentAndGradeService.getGradebook();
    m.addAttribute("students", collegeStudents);
    return "index";
  }

  @PostMapping("/")
  public String create(@ModelAttribute(name = "students") CollegeStudent student, Model model) {
    studentAndGradeService.createStudent(student.getFirstname(), student.getLastname(), student.getEmail());

    var students = studentAndGradeService.getGradebook();
    model.addAttribute("students", students);

    return "index";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable Integer id, Model model) throws Exception {
    var exists = studentAndGradeService.checkIfStudentExists(id);
    if (!exists) {
      return "error";
    }

    studentAndGradeService.deleteStudentById(id);

    return "redirect:/";
  }

  @GetMapping("/student-information/{id}")
  public String getStudentInformation(@PathVariable int id, Model m) throws Exception {

    var studentOpt = studentAndGradeService.findById(id);

    if (studentOpt.isEmpty()) {
      return "error";
    }

    var gradebookCollegeStudent = studentAndGradeService.getInformation(id);

    var student = studentOpt.get();
    m.addAttribute("student", student);

    var studentGrades = gradebookCollegeStudent.getStudentGrades();

    var mathGradeResults = studentGrades.getMathGradeResults();
    if (mathGradeResults.isEmpty()) {
      m.addAttribute("mathAverage", "N/A");
    } else {
      var average = studentGrades.findGradePointAverage(mathGradeResults);
      m.addAttribute("mathAverage", average);
    }

    var scienceGradeResults = studentGrades.getScienceGradeResults();
    if (scienceGradeResults.isEmpty()) {
      m.addAttribute("scienceAverage", "N/A");
    } else {
      var average = studentGrades.findGradePointAverage(scienceGradeResults);
      m.addAttribute("scienceAverage", average);
    }

    var historyGradeResults = studentGrades.getHistoryGradeResults();
    if (historyGradeResults.isEmpty()) {
      m.addAttribute("historyAverage", "N/A");
    } else {
      var average = studentGrades.findGradePointAverage(historyGradeResults);
      m.addAttribute("historyAverage", average);
    }

    return "studentInformation";
  }

  @PostMapping("/grades")
  public String createGrade(
      @RequestParam("grade") double grade,
      @RequestParam("type") GradeType type,
      @RequestParam("studentId") int studentId
  ) throws Exception {

    var studentExists = studentAndGradeService.checkIfStudentExists(studentId);
    if (!studentExists) {
      return "error";
    }

    studentAndGradeService.createGrade(grade, studentId, type);

    return String.format("redirect:/student-information/%d", studentId) ;
  }
}
