package com.hms.hospital_management_system.mapper;


import com.hms.hospital_management_system.dto.DoctorRequest;
import com.hms.hospital_management_system.dto.DoctorResponse;
import com.hms.hospital_management_system.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequest request) {
        return Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .qualifications(request.getQualifications())
                .availability(request.getAvailability())
                .build();
    }

    public DoctorResponse toDto(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialization(doctor.getSpecialization())
                .qualifications(doctor.getQualifications())
                .availability(doctor.getAvailability())
                .build();
    }

    public void updateDoctorFromDto(DoctorRequest request, Doctor doctor) {
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualifications(request.getQualifications());
        doctor.setAvailability(request.getAvailability());
    }
}