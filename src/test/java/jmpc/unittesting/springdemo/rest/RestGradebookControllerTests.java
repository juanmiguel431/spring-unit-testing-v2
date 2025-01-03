package jmpc.unittesting.springdemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jmpc.unittesting.springdemo.models.GradeType;
import jmpc.unittesting.springdemo.models.entities.CollegeStudent;
import jmpc.unittesting.springdemo.repositories.MathGradeRepository;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class RestGradebookControllerTests {

  private static MockHttpServletRequest request;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private MathGradeRepository mathGradeRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;

  @BeforeAll
  public static void beforeAll() {
    request = new MockHttpServletRequest();
    request.setParameter("firstname", "Firstname");
    request.setParameter("lastname", "Lastname");
    request.setParameter("email", "user@test.com");
  }

  @BeforeEach
  public void beforeEach() {
    jdbcTemplate.execute("ALTER TABLE students ALTER COLUMN ID RESTART WITH 1;");
    jdbcTemplate.execute("ALTER TABLE math_grades ALTER COLUMN ID RESTART WITH 1;");
    jdbcTemplate.execute("ALTER TABLE history_grades ALTER COLUMN ID RESTART WITH 1;");
    jdbcTemplate.execute("ALTER TABLE science_grades ALTER COLUMN ID RESTART WITH 1;");

    jdbcTemplate.execute("insert into students(firstname, lastname, email) values ('Firstname', 'Lastname', 'user@test.com')");
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

  /// https://github.com/json-path/JsonPath
  @Test
  public void getStudentsHttpRequest() throws Exception {

    var student = new CollegeStudent("User1", "", "user1@test.com");
    entityManager.persist(student);
    entityManager.flush();

    var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/gradebook/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andReturn();
  }

  @Test
  public void createStudentHttpRequest() throws Exception {
    var student = new CollegeStudent("User1", "", "user1@test.com");

    var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/gradebook/")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(student)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andReturn();

    var studentResult = studentRepository.findByEmail("user1@test.com");
    Assertions.assertNotNull(studentResult, "Student should be found");
  }

  @Test
  public void deleteStudentHttpRequest() throws Exception {
    var student = studentRepository.findById(1);
    Assertions.assertTrue(student.isPresent());

    var result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/gradebook/{id}", 1)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(0)))
        .andReturn();

    var studentResult = studentRepository.findById(1);
    Assertions.assertTrue(studentResult.isEmpty(), "Student should not be found");
  }

  @Test
  public void deleteStudentThatDoesNotExistsHttpRequest() throws Exception {
    var student = studentRepository.findById(0);
    Assertions.assertTrue(student.isEmpty());

    var result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/gradebook/{id}", 0)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
        .andReturn();
  }

  @Test
  public void studentInformationHttpRequest() throws Exception {
    var student = studentRepository.findById(1);
    Assertions.assertTrue(student.isPresent());

    var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/gradebook/student-information/{id}", 1)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstname", is("Firstname")))
        .andExpect(jsonPath("$.lastname", is("Lastname")))
        .andExpect(jsonPath("$.email", is("user@test.com")))
        .andReturn();
  }

  @Test
  public void studentInformationThatDoesNotExistsHttpRequest() throws Exception {
    var student = studentRepository.findById(0);
    Assertions.assertTrue(student.isEmpty());

    var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/gradebook/student-information/{id}", 0)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
        .andReturn();
  }

  @Test
  public void createGradeHttpRequest() throws Exception {

    var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/gradebook/grades")
            .contentType(APPLICATION_JSON)
            .param("studentId", "1")
            .param("grade", "85.5")
            .param("type", GradeType.MATH.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstname", is("Firstname")))
        .andExpect(jsonPath("$.lastname", is("Lastname")))
        .andExpect(jsonPath("$.email", is("user@test.com")))
        .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(2)))
        .andReturn();
  }

  @Test
  public void createGradeForStudentThatDoesNotExistsHttpRequest() throws Exception {
    var student = studentRepository.findById(0);
    Assertions.assertTrue(student.isEmpty());

    var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/gradebook/grades")
            .contentType(APPLICATION_JSON)
            .param("studentId", "0")
            .param("grade", "85.5")
            .param("type", GradeType.MATH.toString()))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
        .andReturn();
  }

  @Test
  public void deleteGradeHttpRequest() throws Exception {
    var grade = mathGradeRepository.findById(1);
    Assertions.assertTrue(grade.isPresent());

    var result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/gradebook/grades/{id}/{gradeType}", 1, GradeType.MATH)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstname", is("Firstname")))
        .andExpect(jsonPath("$.lastname", is("Lastname")))
        .andExpect(jsonPath("$.email", is("user@test.com")))
        .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(0)))
        .andReturn();
  }

  @Test
  public void deleteGradeThatDoesNotExistsHttpRequest() throws Exception {
    var grade = mathGradeRepository.findById(0);
    Assertions.assertTrue(grade.isEmpty());

    var result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/gradebook/grades/{id}/{gradeType}", 0, GradeType.MATH)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", is("Student or Grade was not found")))
        .andReturn();
  }

}
