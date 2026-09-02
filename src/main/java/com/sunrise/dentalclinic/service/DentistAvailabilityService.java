package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.DentistAvailability;
import com.sunrise.dentalclinic.repository.DentistAvailabilityRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DentistAvailabilityService {

    private final DentistAvailabilityRepository repository =
            new DentistAvailabilityRepository();


    /*
     * Get availability schedules
     * for a specific dentist.
     */
    public List<DentistAvailability> getByDentistId(
            Long dentistId)
            throws SQLException {

        if (dentistId == null || dentistId <= 0) {
            throw new IllegalArgumentException(
                    "Valid dentist ID is required."
            );
        }

        return repository.findByDentistId(dentistId);
    }


    /*
     * Add a new availability schedule.
     */
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


        /*
         * Start time must be before end time.
         */
        if (!availability.getStartTime()
                .isBefore(
                        availability.getEndTime()
                )) {

            throw new IllegalArgumentException(
                    "Start time must be before end time."
            );
        }


        /*
         * Availability can be:
         *
         * 1. Weekly day
         * OR
         * 2. Specific date
         */
        boolean hasDay =
                availability.getDayOfWeek() != null
                        && !availability.getDayOfWeek()
                        .trim()
                        .isEmpty();

        boolean hasDate =
                availability.getAvailableDate() != null;


        if (!hasDay && !hasDate) {

            throw new IllegalArgumentException(
                    "Please select a day or specific date."
            );
        }


        if (hasDay && hasDate) {

            throw new IllegalArgumentException(
                    "Availability cannot have both a weekly day and a specific date."
            );
        }


        /*
         * Validate weekly day.
         */
        if (hasDay) {

            String selectedDay =
                    availability.getDayOfWeek()
                            .trim();

            boolean validDay = false;

            for (java.time.DayOfWeek day :
                    java.time.DayOfWeek.values()) {

                if (day.name()
                        .equalsIgnoreCase(
                                selectedDay
                        )) {

                    validDay = true;
                    break;
                }
            }

            if (!validDay) {

                throw new IllegalArgumentException(
                        "Invalid day of week."
                );
            }


            /*
             * Store the day in normal format.
             *
             * Example:
             * MONDAY → Monday
             */
            selectedDay =
                    selectedDay.substring(0, 1)
                            .toUpperCase()
                            + selectedDay.substring(1)
                            .toLowerCase();

            availability.setDayOfWeek(
                    selectedDay
            );
        }


        /*
         * Specific availability date
         * cannot be in the past.
         */
        if (hasDate &&
                availability.getAvailableDate()
                        .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Availability date cannot be in the past."
            );
        }


        /*
         * Check existing schedules
         * for overlapping times.
         */
        List<DentistAvailability> existingSchedules =
                repository.findByDentistId(
                        availability.getDentistId()
                );

        for (DentistAvailability existing :
                existingSchedules) {

            boolean sameDate =
                    hasDate
                            && existing.getAvailableDate() != null
                            && existing.getAvailableDate()
                            .equals(
                                    availability.getAvailableDate()
                            );

            boolean sameDay =
                    hasDay
                            && existing.getAvailableDate() == null
                            && existing.getDayOfWeek() != null
                            && existing.getDayOfWeek()
                            .equalsIgnoreCase(
                                    availability.getDayOfWeek()
                            );


            if (!sameDate && !sameDay) {
                continue;
            }


            LocalTime existingStart =
                    existing.getStartTime();

            LocalTime existingEnd =
                    existing.getEndTime();

            LocalTime newStart =
                    availability.getStartTime();

            LocalTime newEnd =
                    availability.getEndTime();


            /*
             * Check whether the new schedule
             * overlaps an existing schedule.
             */
            boolean overlaps =
                    newStart.isBefore(existingEnd)
                            && newEnd.isAfter(existingStart);


            if (overlaps) {

                throw new IllegalArgumentException(
                        "This availability time overlaps with an existing schedule."
                );
            }
        }


        return repository.save(
                availability
        );
    }
}