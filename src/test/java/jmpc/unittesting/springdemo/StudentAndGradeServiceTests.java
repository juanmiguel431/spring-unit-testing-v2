package jmpc.unittesting.springdemo;

import jmpc.unittesting.springdemo.models.CollegeStudent;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTests {

  @Autowired
  private StudentAndGradeService studentService;

  @Autowired
  private StudentRepository studentRepository;

  @Test
  public void createStudentService() {

    studentService.createStudent("Juan", "Paulino", "juanmiguel431@gmail.com");

    CollegeStudent student = studentRepository.findByEmailAddress("juanmiguel431@gmail.com");

    Assertions.assertEquals("juanmiguel431@gmail.com", student.getEmailAddress(), "Find by email");
  }
}
