package com.sunrise.dentalclinic.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentBillingInfo {

    private Long appointmentId;
    private String appointmentNumber;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private Long dentistId;
    private Long patientId;
    private Long treatmentId;
    private String patientName;

    public AppointmentBillingInfo(
            Long appointmentId,
            String appointmentNumber,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String status,
            Long dentistId,
            Long patientId,
            Long treatmentId,
            String patientName) {

        this.appointmentId = appointmentId;
        this.appointmentNumber = appointmentNumber;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.dentistId = dentistId;
        this.patientId = patientId;
        this.treatmentId = treatmentId;
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public String getPatientName() {
        return patientName;
    }
}