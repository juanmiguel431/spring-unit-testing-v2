package jmpc.unittesting.springdemo;

import jmpc.unittesting.springdemo.models.GradeType;
import jmpc.unittesting.springdemo.models.entities.CollegeStudent;
import jmpc.unittesting.springdemo.models.entities.MathGrade;
import jmpc.unittesting.springdemo.repositories.HistoryGradeRepository;
import jmpc.unittesting.springdemo.repositories.MathGradeRepository;
import jmpc.unittesting.springdemo.repositories.ScienceGradeRepository;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTests {

  @Autowired
  private StudentAndGradeService studentAndGradeService;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private MathGradeRepository mathGradeRepository;

  @Autowired
  private ScienceGradeRepository scienceGradeRepository;

  @Autowired
  private HistoryGradeRepository historyGradeRepository;

  @BeforeEach
  public void beforeEach() {
    jdbcTemplate.execute("ALTER TABLE students ALTER COLUMN ID RESTART WITH 1;");
    jdbcTemplate.execute("ALTER TABLE math_grades ALTER COLUMN ID RESTART WITH 1;");
    jdbcTemplate.execute("ALTER TABLE history_grades ALTER COLUMN ID RESTART WITH 1;");
    jdbcTemplate.execute("ALTER TABLE science_grades ALTER COLUMN ID RESTART WITH 1;");

    jdbcTemplate.execute("insert into students(firstname, lastname, email) values ('Juan', 'Paulino', 'juanmiguel431@gmail.com')");
    jdbcTemplate.execute("insert into math_grades (student_id, grade) values (1, 100.0)");
    jdbcTemplate.execute("insert into history_grades (student_id, grade) values (1, 100.0)");
    jdbcTemplate.execute("insert into science_grades (student_id, grade) values (1, 100.0)");
  }

  @AfterEach
  public void afterEach() {
    jdbcTemplate.execute("delete from students");
    jdbcTemplate.execute("delete from math_grades");
    jdbcTemplate.execute("delete from history_grades");
    jdbcTemplate.execute("delete from science_grades");
  }

  @Sql("/insertData.sql")
  @Test
  public void getGradebookService() {
    Iterable<CollegeStudent> iterableCollegeStudents = studentAndGradeService.getGradebook();

    List<CollegeStudent> collegeStudents = new ArrayList<>();

    for (var item : iterableCollegeStudents) {
      collegeStudents.add(item);
    }

    Assertions.assertEquals(5, collegeStudents.size());
  }

  @Test
  public void deleteStudentStudentByIdService() {
    var student = studentRepository.findById(1);
    Assertions.assertTrue(student.isPresent(), "Return true");

    studentAndGradeService.deleteStudentById(1);

    student = studentRepository.findById(1);
    Assertions.assertFalse(student.isPresent(), "Return false");
  }

  @Test
  public void isStudentNullCheck() {
    Assertions.assertFalse(studentAndGradeService.checkIfStudentIsNull(1));
    Assertions.assertTrue(studentAndGradeService.checkIfStudentIsNull(0));
  }

  @Test
  public void createStudentService() {

    studentAndGradeService.createStudent("Juan", "Paulino", "juanmiguel431@create.com");

    CollegeStudent student = studentRepository.findByEmail("juanmiguel431@create.com");

    Assertions.assertEquals("juanmiguel431@create.com", student.getEmail(), "Find by email");
  }

  @Test
  public void createGradeService() throws Exception {
    studentAndGradeService.createGrade(80.5, 1, GradeType.MATH);
    studentAndGradeService.createGrade(75.4, 1, GradeType.SCIENCE);
    studentAndGradeService.createGrade(88.6, 1, GradeType.HISTORY);

    var mathGrades = mathGradeRepository.findMathGradeByStudentId(1);
    var scienceGrades = scienceGradeRepository.findScienceGradeByStudentId(1);
    var historyGrades = historyGradeRepository.findHistoryGradeGradeByStudentId(1);

    Assertions.assertTrue(mathGrades.iterator().hasNext(), "Student has math grades");
    Assertions.assertTrue(scienceGrades.iterator().hasNext(), "Student has science grades");
    Assertions.assertTrue(historyGrades.iterator().hasNext(), "Student has history grades");

    var grades = (Collection<MathGrade>) mathGrades;
    Assertions.assertEquals(2, grades.size());
  }

  @Test
  public void createGradeWithInvalidParamsThrowsExceptionService1() {
    Assertions.assertThrows(Exception.class, () -> {
      studentAndGradeService.createGrade(-80.5, 1, GradeType.MATH);
    });
  }

  @Test
  public void createGradeWithInvalidParamsThrowsExceptionService2() {
    Assertions.assertThrows(Exception.class, () -> {
      var literature = GradeType.values()[5];
      studentAndGradeService.createGrade(80.5, 1, literature);
    }, "It should throws an exception");
  }

  @Test
  public void deleteGradeService() throws Exception {
    studentAndGradeService.deleteGradeById(1, GradeType.MATH);
    var grade = mathGradeRepository.findById(1);
    Assertions.assertTrue(grade.isEmpty(), "Grade must be not found");
  }
}
