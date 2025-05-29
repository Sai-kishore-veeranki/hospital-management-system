package com.hms.hospital_management_system.repository;


import com.hms.hospital_management_system.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);
    Page<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(String firstName, String lastName, String specialization, Pageable pageable);
}
