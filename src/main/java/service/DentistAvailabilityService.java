package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.DentistAvailability;
import com.sunrise.dentalclinic.repository.DentistAvailabilityRepository;

import java.sql.SQLException;
import java.util.List;

public class DentistAvailabilityService {

    private final DentistAvailabilityRepository repository =
            new DentistAvailabilityRepository();

    public DentistAvailability addAvailability(
            DentistAvailability availability)
            throws SQLException {

        if (availability == null) {
            throw new IllegalArgumentException(
                    "Availability is required."
            );
        }

        if (availability.getDentistId() == null) {
            throw new IllegalArgumentException(
                    "Dentist is required."
            );
        }

        if (availability.getDayOfWeek() == null ||
                availability.getDayOfWeek().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Day is required."
            );
        }

        if (availability.getStartTime() == null) {
            throw new IllegalArgumentException(
                    "Start time is required."
            );
        }

        if (availability.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "End time is required."
            );
        }

        if (!availability.getEndTime()
                .isAfter(availability.getStartTime())) {

            throw new IllegalArgumentException(
                    "End time must be later than start time."
            );
        }

        return repository.save(availability);
    }

    public List<DentistAvailability> getByDentistId(
            Long dentistId)
            throws SQLException {

        return repository.findByDentistId(dentistId);
    }
}