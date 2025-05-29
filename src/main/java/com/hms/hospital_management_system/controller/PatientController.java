package com.hms.hospital_management_system.controller;


import com.hms.hospital_management_system.dto.PatientRequest;
import com.hms.hospital_management_system.dto.PatientResponse;
import com.hms.hospital_management_system.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasAnyRole('ADMIN')") // Only ADMIN can create patients for now
    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        return new ResponseEntity<>(patientService.createPatient(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @GetMapping
    public ResponseEntity<Page<PatientResponse>> getAllPatients(Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @GetMapping("/search")
    public ResponseEntity<Page<PatientResponse>> searchPatients(@RequestParam String query, Pageable pageable) {
        return ResponseEntity.ok(patientService.searchPatients(query, pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN')") // For simplicity, only admin can update/delete
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
