package edu.bellevue.cis530.studentservice.service;

import edu.bellevue.cis530.studentservice.entity.Student;
import edu.bellevue.cis530.studentservice.exception.StudentNotFoundException;
import edu.bellevue.cis530.studentservice.repository.StudentRepository;
import edu.bellevue.cis530.studentservice.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student(1L, "Ali", "Rezaei", "ali.rezaei@example.com");
    }

    @Test
    void createStudent() {
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.createStudent(student);

        assertEquals("Ali", result.getFirstName());
        verify(studentRepository).save(student);
    }

    @Test
    void getAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentService.getAllStudents();

        assertEquals(1, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertEquals("ali.rezaei@example.com", result.getEmail());
        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentByIdNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    void deleteStudent() {
        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }
}
