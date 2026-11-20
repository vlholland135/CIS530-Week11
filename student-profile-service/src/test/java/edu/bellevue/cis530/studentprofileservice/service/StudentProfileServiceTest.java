package edu.bellevue.cis530.studentprofileservice.service;

import edu.bellevue.cis530.studentprofileservice.entity.StudentProfile;
import edu.bellevue.cis530.studentprofileservice.exception.ProfileNotFoundException;
import edu.bellevue.cis530.studentprofileservice.repository.StudentProfileRepository;
import edu.bellevue.cis530.studentprofileservice.service.impl.StudentProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentProfileServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @InjectMocks
    private StudentProfileServiceImpl studentProfileService;

    private StudentProfile profile;

    @BeforeEach
    void setUp() {
        profile = new StudentProfile(1L, 1L, "Computer Science", new BigDecimal("3.75"),
                "Enjoys backend development and databases.");
    }

    @Test
    void createProfile() {
        when(studentProfileRepository.save(profile)).thenReturn(profile);

        StudentProfile result = studentProfileService.createProfile(profile);

        assertEquals("Computer Science", result.getMajor());
        verify(studentProfileRepository).save(profile);
    }

    @Test
    void getAllProfiles() {
        when(studentProfileRepository.findAll()).thenReturn(List.of(profile));

        assertEquals(1, studentProfileService.getAllProfiles().size());
    }

    @Test
    void getProfileById() {
        when(studentProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        assertEquals(1L, studentProfileService.getProfileById(1L).getStudentId());
    }

    @Test
    void getProfileByIdNotFound() {
        when(studentProfileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> studentProfileService.getProfileById(99L));
    }

    @Test
    void getProfileByStudentId() {
        when(studentProfileRepository.findByStudentId(1L)).thenReturn(Optional.of(profile));

        assertEquals("Computer Science", studentProfileService.getProfileByStudentId(1L).getMajor());
        verify(studentProfileRepository).findByStudentId(1L);
    }

    @Test
    void getProfileByStudentIdNotFound() {
        when(studentProfileRepository.findByStudentId(99L)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> studentProfileService.getProfileByStudentId(99L));
    }

    @Test
    void deleteProfile() {
        studentProfileService.deleteProfile(1L);

        verify(studentProfileRepository).deleteById(1L);
    }
}
