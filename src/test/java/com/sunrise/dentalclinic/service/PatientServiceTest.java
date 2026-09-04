package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientServiceTest {

    private final PatientService patientService =
            new PatientService();

    @Test
    void addPatientShouldRejectNullPatient() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(null)
                );

        assertEquals(
                "Patient cannot be null",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectNullPatientName() {

        Patient patient =
                new Patient(
                        null,
                        "Colombo",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Patient name is required",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectEmptyPatientName() {

        Patient patient =
                new Patient(
                        "",
                        "Colombo",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Patient name is required",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectBlankPatientName() {

        Patient patient =
                new Patient(
                        "   ",
                        "Colombo",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Patient name is required",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectNullAddress() {

        Patient patient =
                new Patient(
                        "John Silva",
                        null,
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Address is required",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectEmptyAddress() {

        Patient patient =
                new Patient(
                        "John Silva",
                        "",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Address is required",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectBlankAddress() {

        Patient patient =
                new Patient(
                        "John Silva",
                        "   ",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Address is required",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectNullContactNumber() {

        Patient patient =
                new Patient(
                        "John Silva",
                        "Colombo",
                        null
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Contact number is required",
                exception.getMessage()
        );
    }

    @Test
    void addPatientShouldRejectEmptyContactNumber() {

        Patient patient =
                new Patient(
                        "John Silva",
                        "Colombo",
                        ""
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.addPatient(patient)
                );

        assertEquals(
                "Contact number is required",
                exception.getMessage()
        );
    }

    @Test
    void getPatientByIdShouldRejectInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> patientService.getPatientById(0L)
                );

        assertEquals(
                "Invalid patient ID",
                exception.getMessage()
        );
    }
}