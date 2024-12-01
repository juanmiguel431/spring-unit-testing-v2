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

//  Unnecessary code added by the teacher
//  @MockBean
//  private StudentAndGradeService studentAndGradeServiceMock;

  @BeforeAll
  public static void beforeAll() {
    request = new MockHttpServletRequest();
    request.setParameter("firstname", "Juan");
    request.setParameter("lastname", "Paulino");
    request.setParameter("emailAddress", "juanmiguel431_v2@gmail.com");
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
            .param("emailAddress", request.getParameterValues("emailAddress")))
        .andExpect(status().isOk())
        .andReturn();

    var mnv = mvcResult.getModelAndView();

    Assertions.assertEquals("index", mnv.getViewName());

    var student = studentRepository.findByEmailAddress("juanmiguel431_v2@gmail.com");

    Assertions.assertNotNull(student, "Student should be found");
  }

  @Test
  public void getStudentHttpRequest() throws Exception {

//    Unnecessary code added by the teacher
//    var collegeStudent1 = new GradebookCollegeStudent("Eric", "Roby", "eric_roby@test.com");
//    var collegeStudent2 = new GradebookCollegeStudent("Chad", "Darby", "chad_darby@test.com");
//    var collegeStudentList = new ArrayList<CollegeStudent>(Arrays.asList(collegeStudent1, collegeStudent2));
//    when(studentAndGradeServiceMock.getGradebook()).thenReturn(collegeStudentList);
//    var serviceResponse = studentAndGradeServiceMock.getGradebook();
//    Assertions.assertIterableEquals(collegeStudentList, serviceResponse);

    var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
        .andExpect(status().isOk())
        .andReturn();

    var modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "index");
  }
}
