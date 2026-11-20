package edu.bellevue.cis530.studentservice.service.impl;

import edu.bellevue.cis530.studentservice.entity.Student;
import edu.bellevue.cis530.studentservice.exception.StudentNotFoundException;
import edu.bellevue.cis530.studentservice.repository.StudentRepository;
import edu.bellevue.cis530.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
