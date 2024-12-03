package jmpc.unittesting.springdemo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jmpc.unittesting.springdemo.models.entities.CollegeStudent;
import jmpc.unittesting.springdemo.repositories.HistoryGradeRepository;
import jmpc.unittesting.springdemo.repositories.MathGradeRepository;
import jmpc.unittesting.springdemo.repositories.ScienceGradeRepository;
import jmpc.unittesting.springdemo.repositories.StudentRepository;
import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
  private ScienceGradeRepository scienceGradeRepository;

  @Autowired
  private HistoryGradeRepository historyGradeRepository;

  @Autowired
  private StudentAndGradeService studentAndGradeService;

  @Mock
  private StudentAndGradeService studentAndGradeServiceMock;

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
}
