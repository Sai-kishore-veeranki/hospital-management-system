package com.hms.hospital_management_system.dto;


import com.hms.hospital_management_system.entity.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private Long id;
    private PatientResponse patient; // Nested DTO for patient details
    private DoctorResponse doctor;   // Nested DTO for doctor details
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
    private String notes;
}
