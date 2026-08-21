package com.sunrise.dentalclinic.entity;

import java.math.BigDecimal;

public class Treatment {

    private Long id;
    private String treatmentName;
    private BigDecimal treatmentCost;

    public Treatment() {
    }

    public Treatment(
            String treatmentName,
            BigDecimal treatmentCost) {

        this.treatmentName = treatmentName;
        this.treatmentCost = treatmentCost;
    }

    public Treatment(
            Long id,
            String treatmentName,
            BigDecimal treatmentCost) {

        this.id = id;
        this.treatmentName = treatmentName;
        this.treatmentCost = treatmentCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
    }
}