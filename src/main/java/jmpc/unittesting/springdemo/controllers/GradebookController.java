package jmpc.unittesting.springdemo.controllers;

import jmpc.unittesting.springdemo.models.CollegeStudent;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jmpc.unittesting.springdemo.models.Gradebook;

@Controller
public class GradebookController {

  @Autowired
  private Gradebook gradebook;

  @Autowired
  private StudentAndGradeService studentAndGradeService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String getStudents(Model m) {
    var collegeStudents = studentAndGradeService.getGradebook();
    m.addAttribute("students", collegeStudents);
    return "index";
  }

  @PostMapping("/")
  public String createStudent(@ModelAttribute(name = "students") CollegeStudent student, Model model) {
    studentAndGradeService.createStudent(student.getFirstname(), student.getLastname(), student.getEmail());
    return "index";
  }

  @GetMapping("/studentInformation/{id}")
  public String studentInformation(@PathVariable int id, Model m) {
    return "studentInformation";
  }
}
