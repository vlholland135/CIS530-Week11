package edu.bellevue.cis530.studentprofileservice.service.impl;

import edu.bellevue.cis530.studentprofileservice.entity.StudentProfile;
import edu.bellevue.cis530.studentprofileservice.exception.ProfileNotFoundException;
import edu.bellevue.cis530.studentprofileservice.repository.StudentProfileRepository;
import edu.bellevue.cis530.studentprofileservice.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;

    @Override
    public StudentProfile createProfile(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }

    @Override
    public List<StudentProfile> getAllProfiles() {
        return studentProfileRepository.findAll();
    }

    @Override
    public StudentProfile getProfileById(Long id) {
        return studentProfileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with id " + id));
    }

    @Override
    public StudentProfile getProfileByStudentId(Long studentId) {
        return studentProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for student id " + studentId));
    }

    @Override
    public void deleteProfile(Long id) {
        studentProfileRepository.deleteById(id);
    }
}
