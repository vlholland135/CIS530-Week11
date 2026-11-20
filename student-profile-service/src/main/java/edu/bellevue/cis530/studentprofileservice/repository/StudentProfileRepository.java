package edu.bellevue.cis530.studentprofileservice.repository;

import edu.bellevue.cis530.studentprofileservice.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByStudentId(Long studentId);
}
