package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.repository.DentistRepository;

import java.sql.SQLException;
import java.util.List;

public class DentistService {

    private final DentistRepository dentistRepository =
            new DentistRepository();

    public Dentist addDentist(Dentist dentist) throws SQLException {

        if (dentist.getDentistName() == null ||
                dentist.getDentistName().isBlank()) {

            throw new IllegalArgumentException(
                    "Dentist name is required."
            );
        }

        if (dentist.getSpecialization() == null ||
                dentist.getSpecialization().isBlank()) {

            throw new IllegalArgumentException(
                    "Specialization is required."
            );
        }

        if (dentist.getContactNumber() == null ||
                dentist.getContactNumber().isBlank()) {

            throw new IllegalArgumentException(
                    "Contact number is required."
            );
        }

        return dentistRepository.save(dentist);
    }

    public List<Dentist> getAllDentists()
            throws SQLException {

        return dentistRepository.findAll();
    }

    public Dentist getDentistById(Long id)
            throws SQLException {

        return dentistRepository.findById(id);
    }
}