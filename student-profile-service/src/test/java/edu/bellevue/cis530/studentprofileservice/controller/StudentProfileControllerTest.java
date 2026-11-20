package edu.bellevue.cis530.studentprofileservice.controller;

import edu.bellevue.cis530.studentprofileservice.entity.StudentProfile;
import edu.bellevue.cis530.studentprofileservice.exception.ProfileNotFoundException;
import edu.bellevue.cis530.studentprofileservice.service.StudentProfileService;
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

@ExtendWith(MockitoExtension.class)
class StudentProfileControllerTest {

    @Mock
    private StudentProfileService studentProfileService;

    @InjectMocks
    private StudentProfileController studentProfileController;

    private MockMvc mockMvc;
    private StudentProfile profile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentProfileController).build();
        profile = new StudentProfile(1L, 1L, "Computer Science", new BigDecimal("3.75"),
                "Enjoys backend development and databases.");
    }

    @Test
    void createProfileReturns201() throws Exception {
        when(studentProfileService.createProfile(any(StudentProfile.class))).thenReturn(profile);

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"studentId":1,"major":"Computer Science","gpa":3.75}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(1));
    }

    @Test
    void getAllProfilesReturns200() throws Exception {
        when(studentProfileService.getAllProfiles()).thenReturn(List.of(profile));

        mockMvc.perform(get("/api/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getProfileByIdReturns200() throws Exception {
        when(studentProfileService.getProfileById(1L)).thenReturn(profile);

        mockMvc.perform(get("/api/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.major").value("Computer Science"));
    }

    @Test
    void getProfileByStudentIdReturns200() throws Exception {
        when(studentProfileService.getProfileByStudentId(1L)).thenReturn(profile);

        mockMvc.perform(get("/api/profiles/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1));
    }

    @Test
    void getProfileByStudentIdReturns404WhenMissing() throws Exception {
        when(studentProfileService.getProfileByStudentId(99L))
                .thenThrow(new ProfileNotFoundException("Profile not found for student id 99"));

        mockMvc.perform(get("/api/profiles/student/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProfileReturns200() throws Exception {
        mockMvc.perform(delete("/api/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile deleted successfully"));

        verify(studentProfileService).deleteProfile(1L);
    }
}
