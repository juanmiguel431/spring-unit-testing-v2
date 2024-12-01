package jmpc.unittesting.springdemo;

import jmpc.unittesting.springdemo.repositories.StudentRepository;
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
@TestPropertySource("/application.properties")
@SpringBootTest
public class GradebookControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private StudentRepository studentRepository;

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
    jdbcTemplate.execute("insert into students(firstname, lastname, email) values ('Juan', 'Paulino', 'juanmiguel431@gmail.com')");
  }

  @AfterEach
  public void afterEach() {
    jdbcTemplate.execute("delete from students");
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
    var studentId = 1;

    var student = studentRepository.findById(studentId);

    Assertions.assertTrue(student.isPresent(), "Student should be found");

    var mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.delete("/{id}", studentId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    var mnv = mvcResult.getModelAndView();

    Assertions.assertEquals("index", mnv.getViewName());

    student = studentRepository.findById(studentId);

    Assertions.assertTrue(student.isEmpty(), "Student should be not found");
  }

  @Test
  public void getStudentHttpRequest() throws Exception {
    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
        .andExpect(status().isOk())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "index");
  }
}
