package edu.bellevue.cis530.studentservice.controller;

import edu.bellevue.cis530.studentservice.client.StudentProfileClient;
import edu.bellevue.cis530.studentservice.dto.StudentProfileDto;
import edu.bellevue.cis530.studentservice.entity.Student;
import edu.bellevue.cis530.studentservice.exception.StudentNotFoundException;
import edu.bellevue.cis530.studentservice.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// standalone MockMvc keeps the controller test independent of MySQL, Eureka, and the profile service
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentProfileClient studentProfileClient;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private Student student;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        student = new Student(1L, "Ali", "Rezaei", "ali.rezaei@example.com");
    }

    @Test
    void createStudentReturns201() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Ali","lastName":"Rezaei","email":"ali.rezaei@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllStudentsReturns200() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getStudentByIdReturns200() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ali"));
    }

    @Test
    void getStudentByIdReturns404WhenMissing() throws Exception {
        when(studentService.getStudentById(99L)).thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentReturns200() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student deleted successfully"));

        verify(studentService).deleteStudent(1L);
    }

    @Test
    void getStudentWithProfileCombinesBothServices() throws Exception {
        StudentProfileDto profile = new StudentProfileDto();
        profile.setId(1L);
        profile.setStudentId(1L);
        profile.setMajor("Computer Science");
        profile.setGpa(new BigDecimal("3.75"));

        when(studentService.getStudentById(1L)).thenReturn(student);
        when(studentProfileClient.getProfileByStudentId(1L)).thenReturn(profile);

        mockMvc.perform(get("/api/students/1/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student.firstName").value("Ali"))
                .andExpect(jsonPath("$.profile.major").value("Computer Science"));
    }
}
