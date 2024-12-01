package jmpc.unittesting.springdemo.controllers;

import jmpc.unittesting.springdemo.models.CollegeStudent;
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
  public String delete(@PathVariable Integer id, Model model) {
    var exists = studentAndGradeService.checkIfStudentExists(id);
    if (!exists) {
      return "error";
    }

    studentAndGradeService.delete(id);

    var students = studentAndGradeService.getGradebook();
    model.addAttribute("students", students);

    return "index";
  }

  @GetMapping("/studentInformation/{id}")
  public String studentInformation(@PathVariable int id, Model m) {
    return "studentInformation";
  }
}
