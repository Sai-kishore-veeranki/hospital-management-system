package com.hms.hospital_management_system.mapper;


import com.hms.hospital_management_system.dto.AppointmentRequest;
import com.hms.hospital_management_system.dto.AppointmentResponse;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;

    public Appointment toEntity(AppointmentRequest request, Patient patient, Doctor doctor) {
        return Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(request.getAppointmentTime())
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();
    }

    public AppointmentResponse toDto(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patient(patientMapper.toDto(appointment.getPatient()))
                .doctor(doctorMapper.toDto(appointment.getDoctor()))
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .build();
    }

    public void updateAppointmentFromDto(AppointmentRequest request, Appointment appointment, Patient patient, Doctor doctor) {
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(request.getStatus());
        appointment.setNotes(request.getNotes());
    }
}
