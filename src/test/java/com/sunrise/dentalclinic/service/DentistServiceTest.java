package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Dentist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DentistServiceTest {

    private final DentistService dentistService =
            new DentistService();


    @Test
    void addDentistShouldRejectNullDentistName() {

        Dentist dentist =
                new Dentist(
                        null,
                        "Orthodontics",
                        "0112345678"
                );

        dentist.setDentistName(null);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Dentist name is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectEmptyDentistName() {

        Dentist dentist =
                new Dentist(
                        "",
                        "Orthodontics",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Dentist name is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectBlankDentistName() {

        Dentist dentist =
                new Dentist(
                        "   ",
                        "Orthodontics",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Dentist name is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectNullSpecialization() {

        Dentist dentist =
                new Dentist(
                        "Dr. Test",
                        null,
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Specialization is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectEmptySpecialization() {

        Dentist dentist =
                new Dentist(
                        "Dr. Test",
                        "",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Specialization is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectBlankSpecialization() {

        Dentist dentist =
                new Dentist(
                        "Dr. Test",
                        "   ",
                        "0112345678"
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Specialization is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectNullContactNumber() {

        Dentist dentist =
                new Dentist(
                        "Dr. Test",
                        "Orthodontics",
                        null
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Contact number is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectEmptyContactNumber() {

        Dentist dentist =
                new Dentist(
                        "Dr. Test",
                        "Orthodontics",
                        ""
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Contact number is required.",
                exception.getMessage()
        );
    }


    @Test
    void addDentistShouldRejectBlankContactNumber() {

        Dentist dentist =
                new Dentist(
                        "Dr. Test",
                        "Orthodontics",
                        "   "
                );

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dentistService.addDentist(dentist)
                );

        assertEquals(
                "Contact number is required.",
                exception.getMessage()
        );
    }
}