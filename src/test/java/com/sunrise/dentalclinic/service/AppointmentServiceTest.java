package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Appointment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentServiceTest {

    private final AppointmentService appointmentService =
            new AppointmentService();


    @Test
    void createAppointmentShouldRejectNullAppointment() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.createAppointment(null)
                );

        assertEquals(
                "Appointment is required.",
                exception.getMessage()
        );
    }


    @Test
    void createAppointmentShouldRejectNullDate() {

        Appointment appointment = new Appointment();

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.createAppointment(appointment)
                );

        assertEquals(
                "Appointment date is required.",
                exception.getMessage()
        );
    }


    @Test
    void createAppointmentShouldRejectNullTime() {

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(
                LocalDate.now().plusDays(1)
        );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.createAppointment(appointment)
                );

        assertEquals(
                "Appointment time is required.",
                exception.getMessage()
        );
    }


    @Test
    void createAppointmentShouldRejectInvalidDentist() {

        Appointment appointment = new Appointment();
        appointment.setAppointmentDate(
                LocalDate.now().plusDays(1)
        );
        appointment.setAppointmentTime(
                java.time.LocalTime.of(10, 0)
        );

        appointment.setDentistId(0L);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.createAppointment(appointment)
                );

        assertEquals(
                "Dentist is required.",
                exception.getMessage()
        );
    }


    @Test
    void createAppointmentShouldRejectInvalidPatient() {

        Appointment appointment = new Appointment();

        appointment.setAppointmentDate(
                LocalDate.now().plusDays(1)
        );
        appointment.setAppointmentTime(
                java.time.LocalTime.of(10, 0)
        );
        appointment.setDentistId(1L);
        appointment.setPatientId(0L);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.createAppointment(appointment)
                );

        assertEquals(
                "Patient is required.",
                exception.getMessage()
        );
    }


    @Test
    void createAppointmentShouldRejectInvalidTreatment() {

        Appointment appointment = new Appointment();

        appointment.setAppointmentDate(
                LocalDate.now().plusDays(1)
        );
        appointment.setAppointmentTime(
                java.time.LocalTime.of(10, 0)
        );
        appointment.setDentistId(1L);
        appointment.setPatientId(1L);
        appointment.setTreatmentId(0L);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.createAppointment(appointment)
                );

        assertEquals(
                "Treatment is required.",
                exception.getMessage()
        );
    }


    @Test
    void createAppointmentShouldRejectPastDate() {

        Appointment appointment = new Appointment();

        appointment.setAppointmentDate(
                LocalDate.now().minusDays(1)
        );
        appointment.setAppointmentTime(
                java.time.LocalTime.of(10, 0)
        );
        appointment.setDentistId(1L);
        appointment.setPatientId(1L);
        appointment.setTreatmentId(1L);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.createAppointment(appointment)
                );

        assertEquals(
                "Appointment date cannot be in the past.",
                exception.getMessage()
        );
    }


    @Test
    void getAppointmentsByDentistIdShouldRejectInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.getAppointmentsByDentistId(0L)
                );

        assertEquals(
                "Invalid dentist ID.",
                exception.getMessage()
        );
    }


    @Test
    void getAppointmentsByDateShouldRejectNullDate() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.getAppointmentsByDate(null)
                );

        assertEquals(
                "Appointment date is required.",
                exception.getMessage()
        );
    }


    @Test
    void getBillingAppointmentsByDateShouldRejectNullDate() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.getBillingAppointmentsByDate(null)
                );

        assertEquals(
                "Appointment date is required.",
                exception.getMessage()
        );
    }


    @Test
    void getAppointmentByIdShouldRejectInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.getAppointmentById(0L)
                );

        assertEquals(
                "Invalid appointment ID.",
                exception.getMessage()
        );
    }


    @Test
    void getAppointmentByNumberShouldRejectEmptyNumber() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.getAppointmentByNumber("")
                );

        assertEquals(
                "Appointment number is required.",
                exception.getMessage()
        );
    }


    @Test
    void getAppointmentByNumberShouldRejectNullNumber() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.getAppointmentByNumber(null)
                );

        assertEquals(
                "Appointment number is required.",
                exception.getMessage()
        );
    }


    @Test
    void cancelAppointmentShouldRejectInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> appointmentService.cancelAppointment(0L)
                );

        assertEquals(
                "Invalid appointment ID.",
                exception.getMessage()
        );
    }
}