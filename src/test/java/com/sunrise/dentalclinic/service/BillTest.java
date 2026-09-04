package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Bill;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {

    @Test
    void billShouldCalculateTotalAmountCorrectly() {

        BigDecimal consultationFee =
                new BigDecimal("1000.00");

        BigDecimal treatmentCost =
                new BigDecimal("5000.00");

        Bill bill = new Bill(
                1L,
                consultationFee,
                treatmentCost
        );

        BigDecimal expectedTotal =
                new BigDecimal("6000.00");

        assertEquals(
                expectedTotal,
                bill.getTotalAmount()
        );
    }


    @Test
    void billShouldCalculateTotalAmountWithZeroConsultationFee() {

        BigDecimal consultationFee =
                BigDecimal.ZERO;

        BigDecimal treatmentCost =
                new BigDecimal("5000.00");

        Bill bill = new Bill(
                1L,
                consultationFee,
                treatmentCost
        );

        assertEquals(
                new BigDecimal("5000.00"),
                bill.getTotalAmount()
        );
    }


    @Test
    void billShouldCalculateTotalAmountWithZeroTreatmentCost() {

        BigDecimal consultationFee =
                new BigDecimal("1000.00");

        BigDecimal treatmentCost =
                BigDecimal.ZERO;

        Bill bill = new Bill(
                1L,
                consultationFee,
                treatmentCost
        );

        assertEquals(
                new BigDecimal("1000.00"),
                bill.getTotalAmount()
        );
    }
}