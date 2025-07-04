package com.hms.hospital_management_system.service;


import com.hms.hospital_management_system.dto.AppointmentRequest;
import com.hms.hospital_management_system.dto.AppointmentResponse;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.entity.enums.AppointmentStatus;
import com.hms.hospital_management_system.exception.ResourceNotFoundException;
import com.hms.hospital_management_system.mapper.AppointmentMapper;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        // Basic availability check: Check for overlapping appointments for the same doctor
        if (isDoctorBooked(doctor.getId(), request.getAppointmentTime())) {
            throw new IllegalArgumentException("Doctor is already booked at this time. Please choose another slot.");
        }

        Appointment appointment = appointmentMapper.toEntity(request, patient, doctor);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
        return appointmentMapper.toDto(appointment);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByPatient(Long patientId, Pageable pageable) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with ID: " + patientId);
        }
        return appointmentRepository.findByPatientId(patientId, pageable)
                .map(appointmentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByDoctor(Long doctorId, Pageable pageable) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }
        return appointmentRepository.findByDoctorId(doctorId, pageable)
                .map(appointmentMapper::toDto);
    }


    @Transactional
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        // Basic availability check for the new time/doctor
        // Exclude the current appointment from the check to allow updating its own details
        if (isDoctorBookedExcludingCurrent(doctor.getId(), request.getAppointmentTime(), id)) {
            throw new IllegalArgumentException("Doctor is already booked at this time. Please choose another slot.");
        }

        appointmentMapper.updateAppointmentFromDto(request, appointment, patient, doctor);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with ID: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    // --- Helper for Availability Check ---
    private boolean isDoctorBooked(Long doctorId, LocalDateTime desiredTime) {
        // Assume appointments are 30 minutes long for simplicity in this check
        LocalDateTime startTime = desiredTime.minusMinutes(29); // Start check from 29 mins before
        LocalDateTime endTime = desiredTime.plusMinutes(29);  // End check 29 mins after
        List<Appointment> conflictingAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetweenAndStatusNot(
                        doctorId,
                        startTime,
                        endTime,
                        AppointmentStatus.CANCELLED // Exclude cancelled appointments
                );
        return !conflictingAppointments.isEmpty();
    }

    private boolean isDoctorBookedExcludingCurrent(Long doctorId, LocalDateTime desiredTime, Long currentAppointmentId) {
        LocalDateTime startTime = desiredTime.minusMinutes(29);
        LocalDateTime endTime = desiredTime.plusMinutes(29);
        List<Appointment> conflictingAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetweenAndStatusNot(
                        doctorId,
                        startTime,
                        endTime,
                        AppointmentStatus.CANCELLED
                );
        return conflictingAppointments.stream()
                .anyMatch(app -> !app.getId().equals(currentAppointmentId)); // True if any *other* appointment conflicts
    }
}
