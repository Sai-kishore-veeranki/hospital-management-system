package com.hms.hospital_management_system.service;


import com.hms.hospital_management_system.dto.PatientRequest;
import com.hms.hospital_management_system.dto.PatientResponse;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.exception.ResourceNotFoundException;
import com.hms.hospital_management_system.mapper.PatientMapper;
import com.hms.hospital_management_system.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Patient with this email already exists.");
        }
        Patient patient = patientMapper.toEntity(request);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
        return patientMapper.toDto(patient);
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(patientMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> searchPatients(String query, Pageable pageable) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query, pageable)
                .map(patientMapper::toDto);
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));

        // Check if email is being changed to an existing one (and not the current patient's email)
        if (patientRepository.findByEmail(request.getEmail()).isPresent() &&
                !patient.getEmail().equals(request.getEmail())) {
            throw new IllegalArgumentException("Another patient with this email already exists.");
        }

        patientMapper.updatePatientFromDto(request, patient);
        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toDto(updatedPatient);
    }

    @Transactional
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }
}
