package com.sunrise.dentalclinic.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bill {

    private Long id;
    private LocalDateTime billDate;
    private BigDecimal consultationFee;
    private BigDecimal totalAmount;
    private BigDecimal treatmentCost;
    private Long appointmentId;

    public Bill() {
    }

    public Bill(
            Long appointmentId,
            BigDecimal consultationFee,
            BigDecimal treatmentCost) {

        this.appointmentId = appointmentId;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;

        this.totalAmount =
                consultationFee.add(treatmentCost);

        this.billDate = LocalDateTime.now();
    }

    public Bill(
            Long id,
            LocalDateTime billDate,
            BigDecimal consultationFee,
            BigDecimal totalAmount,
            BigDecimal treatmentCost,
            Long appointmentId) {

        this.id = id;
        this.billDate = billDate;
        this.consultationFee = consultationFee;
        this.totalAmount = totalAmount;
        this.treatmentCost = treatmentCost;
        this.appointmentId = appointmentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(
            BigDecimal consultationFee) {

        this.consultationFee = consultationFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(
            BigDecimal totalAmount) {

        this.totalAmount = totalAmount;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(
            BigDecimal treatmentCost) {

        this.treatmentCost = treatmentCost;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(
            Long appointmentId) {

        this.appointmentId = appointmentId;
    }
}