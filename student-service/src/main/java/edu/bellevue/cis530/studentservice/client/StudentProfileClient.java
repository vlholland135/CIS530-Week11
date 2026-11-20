package edu.bellevue.cis530.studentservice.client;

import edu.bellevue.cis530.studentservice.dto.StudentProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Eureka resolves "student-profile-service" to a running instance, so no URL is hard coded here
@FeignClient(name = "student-profile-service")
public interface StudentProfileClient {

    @GetMapping("/api/profiles/student/{studentId}")
    StudentProfileDto getProfileByStudentId(@PathVariable("studentId") Long studentId);
}
