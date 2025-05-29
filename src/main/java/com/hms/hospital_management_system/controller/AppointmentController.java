package com.hms.hospital_management_system.controller;


import com.hms.hospital_management_system.dto.AppointmentRequest;
import com.hms.hospital_management_system.dto.AppointmentResponse;
import com.hms.hospital_management_system.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
//@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')") // ADMIN or PATIENT can schedule
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(appointmentService.createAppointment(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN')") // Only ADMIN can view all appointments
    @GetMapping
    public ResponseEntity<Page<AppointmentResponse>> getAllAppointments(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAllAppointments(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<AppointmentResponse>> getAppointmentsByPatient(@PathVariable Long patientId, Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId, pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<AppointmentResponse>> getAppointmentsByDoctor(@PathVariable Long doctorId, Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId, pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'DOCTOR')") // ADMIN, PATIENT (for their own), DOCTOR (for their own)
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRequest request) {
        // Further logic can be added here to restrict updates (e.g., patient can only update their own appt)
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')") // Only ADMIN can delete for simplicity
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
