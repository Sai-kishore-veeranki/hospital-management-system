package com.hms.hospital_management_system.entity.enums;

/**
 * Represents the status of an appointment in the hospital management system.
 */
public enum AppointmentStatus {

    /**
     * Appointment has been scheduled but not yet completed.
     */
    SCHEDULED,

    /**
     * Appointment was completed successfully.
     */
    COMPLETED,

    /**
     * Appointment was cancelled and will not occur.
     */
    CANCELLED,

    /**
     * Appointment has been rescheduled to a new time.
     */
    RESCHEDULED
}
