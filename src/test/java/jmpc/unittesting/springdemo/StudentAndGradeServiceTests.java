package jmpc.unittesting.springdemo;

import jmpc.unittesting.springdemo.models.CollegeStudent;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTests {

  @Autowired
  private StudentAndGradeService studentService;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  public void beforeEach() {
    jdbcTemplate.execute("insert into students(id, firstname, lastname, email) values (1, 'Juan', 'Paulino', 'juanmiguel431@gmail.com')");
  }

  @AfterEach
  public void afterEach() {
    jdbcTemplate.execute("delete from students");
  }

  @Test
  public void isStudentNullCheck() {
    Assertions.assertFalse(studentService.checkIfStudentIsNull(1));
    Assertions.assertTrue(studentService.checkIfStudentIsNull(0));
  }

  @Test
  public void createStudentService() {

    studentService.createStudent("Juan", "Paulino", "juanmiguel431@gmail.com");

    CollegeStudent student = studentRepository.findByEmailAddress("juanmiguel431@gmail.com");

    Assertions.assertEquals("juanmiguel431@gmail.com", student.getEmailAddress(), "Find by email");
  }
}
