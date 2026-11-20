package edu.bellevue.cis530.studentservice.repository;

import edu.bellevue.cis530.studentservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
