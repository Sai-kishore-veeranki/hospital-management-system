package com.hms.hospital_management_system.controller;


import com.hms.hospital_management_system.dto.DoctorRequest;
import com.hms.hospital_management_system.dto.DoctorResponse;
import com.hms.hospital_management_system.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can create doctors
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest request) {
        return new ResponseEntity<>(doctorService.createDoctor(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping
    public ResponseEntity<Page<DoctorResponse>> getAllDoctors(Pageable pageable) {
        return ResponseEntity.ok(doctorService.getAllDoctors(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/search")
    public ResponseEntity<Page<DoctorResponse>> searchDoctors(@RequestParam String query, Pageable pageable) {
        return ResponseEntity.ok(doctorService.searchDoctors(query, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
