package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Treatment;
import com.sunrise.dentalclinic.repository.TreatmentRepository;

import java.sql.SQLException;
import java.util.List;

public class TreatmentService {

    private final TreatmentRepository repository =
            new TreatmentRepository();


    public Treatment addTreatment(
            Treatment treatment)
            throws SQLException {

        if (treatment == null) {
            throw new IllegalArgumentException(
                    "Treatment is required."
            );
        }

        if (treatment.getTreatmentName() == null ||
                treatment.getTreatmentName()
                        .trim()
                        .isEmpty()) {

            throw new IllegalArgumentException(
                    "Treatment name is required."
            );
        }

        if (treatment.getTreatmentCost() == null ||
                treatment.getTreatmentCost()
                        .signum() < 0) {

            throw new IllegalArgumentException(
                    "Treatment cost must be valid."
            );
        }

        treatment.setTreatmentName(
                treatment.getTreatmentName().trim()
        );

        return repository.save(treatment);
    }


    public List<Treatment> getAllTreatments()
            throws SQLException {

        return repository.findAll();
    }


    public Treatment getTreatmentById(Long id)
            throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid treatment ID."
            );
        }

        Treatment treatment =
                repository.findById(id);

        if (treatment == null) {
            throw new IllegalArgumentException(
                    "Treatment not found."
            );
        }

        return treatment;
    }
}