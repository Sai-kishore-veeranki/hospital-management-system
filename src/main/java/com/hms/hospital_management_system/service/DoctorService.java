package com.hms.hospital_management_system.service;


import com.hms.hospital_management_system.dto.DoctorRequest;
import com.hms.hospital_management_system.dto.DoctorResponse;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.exception.ResourceNotFoundException;
import com.hms.hospital_management_system.mapper.DoctorMapper;
import com.hms.hospital_management_system.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        if (doctorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Doctor with this email already exists.");
        }
        Doctor doctor = doctorMapper.toEntity(request);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        return doctorMapper.toDto(doctor);
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(doctorMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> searchDoctors(String query, Pageable pageable) {
        return doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(query, query, query, pageable)
                .map(doctorMapper::toDto);
    }

    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));

        // Check if email is being changed to an existing one (and not the current doctor's email)
        if (doctorRepository.findByEmail(request.getEmail()).isPresent() &&
                !doctor.getEmail().equals(request.getEmail())) {
            throw new IllegalArgumentException("Another doctor with this email already exists.");
        }

        doctorMapper.updateDoctorFromDto(request, doctor);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(updatedDoctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }
}
