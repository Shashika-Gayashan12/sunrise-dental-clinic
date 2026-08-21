package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.DentistAvailability;
import com.sunrise.dentalclinic.repository.AppointmentRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class AppointmentService {

    private final AppointmentRepository repository =
            new AppointmentRepository();

    private final DentistAvailabilityService
            availabilityService =
            new DentistAvailabilityService();

    public Appointment createAppointment(
            Appointment appointment)
            throws SQLException {

        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException(
                    "Appointment date is required."
            );
        }

        if (appointment.getAppointmentTime() == null) {
            throw new IllegalArgumentException(
                    "Appointment time is required."
            );
        }

        if (appointment.getDentistId() == null) {
            throw new IllegalArgumentException(
                    "Please select a dentist."
            );
        }

        if (appointment.getPatientId() == null) {
            throw new IllegalArgumentException(
                    "Please select a patient."
            );
        }

        if (appointment.getTreatmentId() == null) {
            throw new IllegalArgumentException(
                    "Please select a treatment."
            );
        }

        /*
         * Appointment date cannot be in the past.
         */
        if (appointment.getAppointmentDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past."
            );
        }

        LocalDate appointmentDate =
                appointment.getAppointmentDate();

        LocalTime appointmentTime =
                appointment.getAppointmentTime();

        /*
         * Get all availability schedules for this dentist.
         */
        List<DentistAvailability> schedules =
                availabilityService.getByDentistId(
                        appointment.getDentistId()
                );

        /*
         * Convert appointment date to day name.
         *
         * Example:
         * 2026-08-24 -> Monday
         */
        String appointmentDay =
                appointmentDate
                        .getDayOfWeek()
                        .toString();

        appointmentDay =
                appointmentDay.substring(0, 1)
                        + appointmentDay.substring(1)
                        .toLowerCase();

        /*
         * These variables tell us whether there is
         * a schedule specifically controlling this
         * appointment.
         */
        boolean exactDateScheduleExists = false;
        boolean weeklyScheduleExists = false;
        boolean available = false;

        /*
         * Check dentist availability schedules.
         */
        for (DentistAvailability schedule : schedules) {

            /*
             * ---------------------------------------------
             * 1. EXACT DATE SCHEDULE
             * ---------------------------------------------
             *
             * Example:
             *
             * Dentist is unavailable/available only on
             * 2026-08-25.
             */
            if (schedule.getAvailableDate() != null) {

                if (schedule.getAvailableDate()
                        .equals(appointmentDate)) {

                    exactDateScheduleExists = true;

                    LocalTime start =
                            schedule.getStartTime();

                    LocalTime end =
                            schedule.getEndTime();

                    if (!appointmentTime.isBefore(start)
                            &&
                            !appointmentTime.isAfter(end)) {

                        available = true;
                        break;
                    }
                }

                continue;
            }

            /*
             * ---------------------------------------------
             * 2. WEEKLY RECURRING SCHEDULE
             * ---------------------------------------------
             *
             * Example:
             *
             * Monday 09:00 - 17:00
             *
             * This applies every Monday.
             */
            if (schedule.getDayOfWeek()
                    .equalsIgnoreCase(appointmentDay)) {

                weeklyScheduleExists = true;

                LocalTime start =
                        schedule.getStartTime();

                LocalTime end =
                        schedule.getEndTime();

                if (!appointmentTime.isBefore(start)
                        &&
                        !appointmentTime.isAfter(end)) {

                    available = true;
                    break;
                }
            }
        }

        /*
         * ---------------------------------------------
         * AVAILABILITY RULE
         * ---------------------------------------------
         *
         * If an exact-date schedule exists,
         * the appointment must be inside that schedule.
         *
         * Otherwise, if a weekly schedule exists,
         * the appointment must be inside that schedule.
         *
         * If NO schedule exists at all for this date/day,
         * the dentist is considered available.
         */
        if (exactDateScheduleExists && !available) {

            throw new IllegalArgumentException(
                    "The dentist is not available on " +
                            appointmentDate +
                            " at " +
                            appointmentTime
            );
        }

        if (!exactDateScheduleExists
                &&
                weeklyScheduleExists
                &&
                !available) {

            throw new IllegalArgumentException(
                    "The dentist is not available on " +
                            appointmentDay +
                            " at " +
                            appointmentTime
            );
        }

        /*
         * ---------------------------------------------
         * CHECK EXISTING APPOINTMENT
         * ---------------------------------------------
         *
         * This is the MOST IMPORTANT check.
         *
         * Even if the dentist has no availability
         * schedule, we still check whether the dentist
         * already has a booking at this exact date
         * and time.
         */
        boolean alreadyBooked =
                repository.existsActiveAppointment(
                        appointment.getDentistId(),
                        appointmentDate,
                        appointmentTime
                );

        if (alreadyBooked) {

            throw new IllegalArgumentException(
                    "This dentist is already booked at " +
                            appointmentDate +
                            " " +
                            appointmentTime +
                            ". Please choose another time."
            );
        }

        /*
         * Generate unique appointment number.
         */
        appointment.setAppointmentNumber(
                generateAppointmentNumber()
        );

        /*
         * New appointments start as PENDING.
         */
        appointment.setStatus("PENDING");

        return repository.save(appointment);
    }

    public List<Appointment> getAllAppointments()
            throws SQLException {

        return repository.findAll();
    }

    private String generateAppointmentNumber() {

        return "APT-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}