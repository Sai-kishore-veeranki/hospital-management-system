package com.hms.hospital_management_system.repository;


import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Find appointments for a specific doctor within a time range (for availability check)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetweenAndStatusNot(Long doctorId, LocalDateTime start, LocalDateTime end, AppointmentStatus status);

    // Find appointments by patient
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    // Find appointments by doctor
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);

    // Find appointments by status
    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);

    // Find appointments by patient and doctor
    Page<Appointment> findByPatientIdAndDoctorId(Long patientId, Long doctorId, Pageable pageable);

    // Find appointments within a date range
    Page<Appointment> findByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
