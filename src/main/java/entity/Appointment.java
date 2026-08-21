package com.sunrise.dentalclinic.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private Long id;
    private LocalDate appointmentDate;
    private String appointmentNumber;
    private LocalTime appointmentTime;
    private String status;
    private Long dentistId;
    private Long patientId;
    private Long treatmentId;

    public Appointment() {
    }

    public Appointment(
            Long id,
            LocalDate appointmentDate,
            String appointmentNumber,
            LocalTime appointmentTime,
            String status,
            Long dentistId,
            Long patientId,
            Long treatmentId) {

        this.id = id;
        this.appointmentDate = appointmentDate;
        this.appointmentNumber = appointmentNumber;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.dentistId = dentistId;
        this.patientId = patientId;
        this.treatmentId = treatmentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(String appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDentistId() {
        return dentistId;
    }

    public void setDentistId(Long dentistId) {
        this.dentistId = dentistId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }
}