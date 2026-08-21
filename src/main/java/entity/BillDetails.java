package com.sunrise.dentalclinic.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BillDetails {

    private Long billId;
    private LocalDateTime billDate;

    private Long appointmentId;
    private String appointmentNumber;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private String patientName;
    private String patientContact;

    private String dentistName;
    private String specialization;

    private String treatmentName;

    private BigDecimal consultationFee;
    private BigDecimal treatmentCost;
    private BigDecimal totalAmount;

    public BillDetails(
            Long billId,
            LocalDateTime billDate,
            Long appointmentId,
            String appointmentNumber,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String patientName,
            String patientContact,
            String dentistName,
            String specialization,
            String treatmentName,
            BigDecimal consultationFee,
            BigDecimal treatmentCost,
            BigDecimal totalAmount) {

        this.billId = billId;
        this.billDate = billDate;
        this.appointmentId = appointmentId;
        this.appointmentNumber = appointmentNumber;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientName = patientName;
        this.patientContact = patientContact;
        this.dentistName = dentistName;
        this.specialization = specialization;
        this.treatmentName = treatmentName;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
        this.totalAmount = totalAmount;
    }

    public Long getBillId() {
        return billId;
    }

    public LocalDateTime getBillDate() {
        return billDate;
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

    public String getPatientName() {
        return patientName;
    }

    public String getPatientContact() {
        return patientContact;
    }

    public String getDentistName() {
        return dentistName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}