package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Bill;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BillServiceTest {

    private final BillService billService =
            new BillService();


    @Test
    void addBillShouldRejectNullBill() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(null)
                );

        assertEquals(
                "Bill is required.",
                exception.getMessage()
        );
    }


    @Test
    void addBillShouldRejectNullAppointmentId() {

        Bill bill = new Bill();

        bill.setAppointmentId(null);
        bill.setConsultationFee(
                new BigDecimal("1000")
        );
        bill.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(bill)
                );

        assertEquals(
                "Valid appointment ID is required.",
                exception.getMessage()
        );
    }


    @Test
    void addBillShouldRejectZeroAppointmentId() {

        Bill bill = new Bill();

        bill.setAppointmentId(0L);
        bill.setConsultationFee(
                new BigDecimal("1000")
        );
        bill.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(bill)
                );

        assertEquals(
                "Valid appointment ID is required.",
                exception.getMessage()
        );
    }


    @Test
    void addBillShouldRejectNegativeAppointmentId() {

        Bill bill = new Bill();

        bill.setAppointmentId(-1L);
        bill.setConsultationFee(
                new BigDecimal("1000")
        );
        bill.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(bill)
                );

        assertEquals(
                "Valid appointment ID is required.",
                exception.getMessage()
        );
    }


    @Test
    void addBillShouldRejectNullConsultationFee() {

        Bill bill = new Bill();

        bill.setAppointmentId(1L);
        bill.setConsultationFee(null);
        bill.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(bill)
                );

        assertEquals(
                "Consultation fee must be valid.",
                exception.getMessage()
        );
    }


    @Test
    void addBillShouldRejectNegativeConsultationFee() {

        Bill bill = new Bill();

        bill.setAppointmentId(1L);
        bill.setConsultationFee(
                new BigDecimal("-100")
        );
        bill.setTreatmentCost(
                new BigDecimal("5000")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(bill)
                );

        assertEquals(
                "Consultation fee must be valid.",
                exception.getMessage()
        );
    }


    @Test
    void addBillShouldRejectNullTreatmentCost() {

        Bill bill = new Bill();

        bill.setAppointmentId(1L);
        bill.setConsultationFee(
                new BigDecimal("1000")
        );
        bill.setTreatmentCost(null);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(bill)
                );

        assertEquals(
                "Treatment cost must be valid.",
                exception.getMessage()
        );
    }


    @Test
    void addBillShouldRejectNegativeTreatmentCost() {

        Bill bill = new Bill();

        bill.setAppointmentId(1L);
        bill.setConsultationFee(
                new BigDecimal("1000")
        );
        bill.setTreatmentCost(
                new BigDecimal("-500")
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.addBill(bill)
                );

        assertEquals(
                "Treatment cost must be valid.",
                exception.getMessage()
        );
    }


    @Test
    void getBillDetailsShouldRejectNullBillId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.getBillDetails(null)
                );

        assertEquals(
                "Valid bill ID is required.",
                exception.getMessage()
        );
    }


    @Test
    void getBillDetailsShouldRejectZeroBillId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.getBillDetails(0L)
                );

        assertEquals(
                "Valid bill ID is required.",
                exception.getMessage()
        );
    }


    @Test
    void getBillDetailsShouldRejectNegativeBillId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> billService.getBillDetails(-1L)
                );

        assertEquals(
                "Valid bill ID is required.",
                exception.getMessage()
        );
    }
}