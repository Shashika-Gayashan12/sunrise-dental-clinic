package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Treatment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TreatmentServiceTest {

    private final TreatmentService treatmentService =
            new TreatmentService();


    @Test
    void addTreatmentShouldRejectNullTreatment() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.addTreatment(null)
                );

        assertEquals(
                "Treatment is required.",
                exception.getMessage()
        );
    }


    @Test
    void addTreatmentShouldRejectNullTreatmentName() {

        Treatment treatment = new Treatment();

        treatment.setTreatmentName(null);
        treatment.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.addTreatment(treatment)
                );

        assertEquals(
                "Treatment name is required.",
                exception.getMessage()
        );
    }


    @Test
    void addTreatmentShouldRejectEmptyTreatmentName() {

        Treatment treatment = new Treatment();

        treatment.setTreatmentName("");
        treatment.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.addTreatment(treatment)
                );

        assertEquals(
                "Treatment name is required.",
                exception.getMessage()
        );
    }


    @Test
    void addTreatmentShouldRejectBlankTreatmentName() {

        Treatment treatment = new Treatment();

        treatment.setTreatmentName("   ");
        treatment.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.addTreatment(treatment)
                );

        assertEquals(
                "Treatment name is required.",
                exception.getMessage()
        );
    }


    @Test
    void addTreatmentShouldRejectNullTreatmentCost() {

        Treatment treatment = new Treatment();

        treatment.setTreatmentName("Dental Cleaning");
        treatment.setTreatmentCost(null);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.addTreatment(treatment)
                );

        assertEquals(
                "Treatment cost must be valid.",
                exception.getMessage()
        );
    }


    @Test
    void addTreatmentShouldRejectNegativeTreatmentCost() {

        Treatment treatment = new Treatment();

        treatment.setTreatmentName("Dental Cleaning");
        treatment.setTreatmentCost(
                new BigDecimal("-100")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.addTreatment(treatment)
                );

        assertEquals(
                "Treatment cost must be valid.",
                exception.getMessage()
        );
    }


    @Test
    void getTreatmentByIdShouldRejectNullId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.getTreatmentById(null)
                );

        assertEquals(
                "Invalid treatment ID.",
                exception.getMessage()
        );
    }


    @Test
    void getTreatmentByIdShouldRejectZeroId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.getTreatmentById(0L)
                );

        assertEquals(
                "Invalid treatment ID.",
                exception.getMessage()
        );
    }


    @Test
    void getTreatmentByIdShouldRejectNegativeId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> treatmentService.getTreatmentById(-1L)
                );

        assertEquals(
                "Invalid treatment ID.",
                exception.getMessage()
        );
    }
}