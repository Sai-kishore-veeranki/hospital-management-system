package com.hms.hospital_management_system.mapper;


import com.hms.hospital_management_system.dto.PatientRequest;
import com.hms.hospital_management_system.dto.PatientResponse;
import com.hms.hospital_management_system.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequest request) {
        return Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .medicalHistory(request.getMedicalHistory())
                .allergies(request.getAllergies())
                .build();
    }

    public PatientResponse toDto(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .medicalHistory(patient.getMedicalHistory())
                .allergies(patient.getAllergies())
                .build();
    }

    public void updatePatientFromDto(PatientRequest request, Patient patient) {
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail()); // Email can be updated if logic allows, usually unique
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setAllergies(request.getAllergies());
    }
}
