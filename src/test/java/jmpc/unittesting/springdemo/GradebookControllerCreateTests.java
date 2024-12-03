package jmpc.unittesting.springdemo;

import jmpc.unittesting.springdemo.services.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class GradebookControllerCreateTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentAndGradeService studentAndGradeServiceMock;

  @Test
  public void createStudentHttpRequest_getGradebook_isCalled() throws Exception {

    var firstname = "Juan";
    var lastname = "Paulino";
    var email = "juanmiguel431_v2@gmail.com";

    mockMvc.perform(MockMvcRequestBuilders.post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .param("firstname", firstname)
            .param("lastname", lastname)
            .param("email", email)
        )
        .andExpect(status().isOk());

    verify(studentAndGradeServiceMock, times(1)).createStudent(firstname, lastname, email);
  }
}
