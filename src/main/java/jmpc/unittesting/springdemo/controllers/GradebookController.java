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

    var gradebookCollegeStudentOpt = studentAndGradeService.getInformationWithAverage(id);

    if (gradebookCollegeStudentOpt.isEmpty()) {
      return "error";
    }

    var student = gradebookCollegeStudentOpt.get();

    m.addAttribute("student", student.getStudent());
    m.addAttribute("mathAverage", student.getMathAverage());
    m.addAttribute("scienceAverage", student.getScienceAverage());
    m.addAttribute("historyAverage", student.getHistoryAverage());

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
