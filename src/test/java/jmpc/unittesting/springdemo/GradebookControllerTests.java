package jmpc.unittesting.springdemo;

import jmpc.unittesting.springdemo.models.GradeType;
import jmpc.unittesting.springdemo.repositories.MathGradeRepository;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class GradebookControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private MathGradeRepository mathGradeRepository;

  @Autowired
  private StudentAndGradeService studentAndGradeService;

  private static MockHttpServletRequest request;

  @BeforeAll
  public static void beforeAll() {
    request = new MockHttpServletRequest();
    request.setParameter("firstname", "Juan");
    request.setParameter("lastname", "Paulino");
    request.setParameter("email", "juanmiguel431_v2@gmail.com");
  }

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

  @Test
  public void createStudentHttpRequest() throws Exception {
    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .param("firstname", request.getParameterValues("firstname"))
            .param("lastname", request.getParameterValues("lastname"))
            .param("email", request.getParameterValues("email")))
        .andExpect(status().isOk())
        .andReturn();

    var mnv = mvcResult.getModelAndView();

    Assertions.assertEquals("index", mnv.getViewName());

    var student = studentRepository.findByEmail("juanmiguel431_v2@gmail.com");

    Assertions.assertNotNull(student, "Student should be found");
  }

  @Test
  public void deleteStudentHttpRequest() throws Exception {
    var student = studentRepository.findByEmail("juanmiguel431@gmail.com");
    Assertions.assertNotNull(student, "Student should be found");

    var mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.delete("/{id}", student.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    var mnv = mvcResult.getModelAndView();

    var any = studentRepository.findById(student.getId());

    Assertions.assertTrue(any.isEmpty(), "Student should be not found");
  }

  @Test
  public void errorIfWhileDeletingStudentDoesNotExistHttpRequest() throws Exception {
    var studentId = 0;
    var student = studentRepository.findById(studentId);
    Assertions.assertTrue(student.isEmpty(), "Student should not be found");

    var mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.delete("/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    var mnv = mvcResult.getModelAndView();

    Assertions.assertEquals("error", mnv.getViewName());
  }

  @Test
  public void getStudentHttpRequest() throws Exception {
    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
        .andExpect(status().isOk())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "index");
  }

  @Test
  public void studentInformationRequest() throws Exception {
    var student = studentRepository.findById(1);
    Assertions.assertTrue(student.isPresent());

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/student-information/{id}", 1))
        .andExpect(status().isOk())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    Assertions.assertEquals("studentInformation", modelAndView.getViewName());
  }

  @Test
  public void studentInformationDoesNotExistsRequest() throws Exception {
    var student = studentRepository.findById(0);
    Assertions.assertTrue(student.isEmpty());

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/student-information/{id}", 0))
        .andExpect(status().isOk())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    Assertions.assertEquals("error", modelAndView.getViewName());
  }

  @Test
  public void createValidGradeHttpRequest() throws Exception {
    var studentInfoOpt = studentAndGradeService.getInformation(1);
    Assertions.assertTrue(studentInfoOpt.isPresent());

    var studentInfo = studentInfoOpt.get();
    Assertions.assertEquals(1, studentInfo.getStudentGrades().getMathGradeResults().size());

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .param("grade", "85.0")
        .param("type", GradeType.MATH.toString())
        .param("studentId", "1"))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    studentInfoOpt = studentAndGradeService.getInformation(1);
    studentInfo = studentInfoOpt.get();
    Assertions.assertEquals(2, studentInfo.getStudentGrades().getMathGradeResults().size());
  }

  @Test
  public void deleteAValidGradeHttpRequest() throws Exception {
    var mathGrade = mathGradeRepository.findById(1);
    Assertions.assertTrue(mathGrade.isPresent());

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{type}/{id}", GradeType.MATH, 1))
        .andExpect(status().is3xxRedirection())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    mathGrade = mathGradeRepository.findById(1);
    Assertions.assertTrue(mathGrade.isEmpty());
  }

  @Test
  public void deleteAnInValidGradeHttpRequest() throws Exception {
    var mathGrade = mathGradeRepository.findById(0);
    Assertions.assertTrue(mathGrade.isEmpty());

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{type}/{id}", GradeType.MATH, 0))
        .andExpect(status().isOk())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    Assertions.assertEquals("error", modelAndView.getViewName());
  }
}
