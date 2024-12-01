package jmpc.unittesting.springdemo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
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

//  Unnecessary code added by the teacher
//  @MockBean
//  private StudentAndGradeService studentAndGradeServiceMock;

  @BeforeEach
  public void beforeEach() {
    jdbcTemplate.execute("insert into students(id, firstname, lastname, email) values (1, 'Juan', 'Paulino', 'juanmiguel431@gmail.com')");
  }

  @AfterEach
  public void afterEach() {
    jdbcTemplate.execute("delete from students");
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
