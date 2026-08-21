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

    private final DentistAvailabilityService availabilityService =
            new DentistAvailabilityService();

    public Appointment createAppointment(
            Appointment appointment)
            throws SQLException {

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment is required."
            );
        }

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

        List<DentistAvailability> schedules =
                availabilityService.getByDentistId(
                        appointment.getDentistId()
                );

        String appointmentDay =
                appointmentDate
                        .getDayOfWeek()
                        .toString();

        appointmentDay =
                appointmentDay.substring(0, 1)
                        + appointmentDay.substring(1)
                        .toLowerCase();

        boolean exactDateScheduleExists = false;
        boolean weeklyScheduleExists = false;
        boolean available = false;

        for (DentistAvailability schedule : schedules) {

            if (schedule.getAvailableDate() != null) {

                if (schedule.getAvailableDate()
                        .equals(appointmentDate)) {

                    exactDateScheduleExists = true;

                    LocalTime start =
                            schedule.getStartTime();

                    LocalTime end =
                            schedule.getEndTime();

                    if (!appointmentTime.isBefore(start)
                            && !appointmentTime.isAfter(end)) {

                        available = true;
                        break;
                    }
                }

                continue;
            }

            if (schedule.getDayOfWeek()
                    .equalsIgnoreCase(appointmentDay)) {

                weeklyScheduleExists = true;

                LocalTime start =
                        schedule.getStartTime();

                LocalTime end =
                        schedule.getEndTime();

                if (!appointmentTime.isBefore(start)
                        && !appointmentTime.isAfter(end)) {

                    available = true;
                    break;
                }
            }
        }

        if (exactDateScheduleExists && !available) {

            throw new IllegalArgumentException(
                    "The dentist is not available on "
                            + appointmentDate
                            + " at "
                            + appointmentTime
            );
        }

        if (!exactDateScheduleExists
                && weeklyScheduleExists
                && !available) {

            throw new IllegalArgumentException(
                    "The dentist is not available on "
                            + appointmentDay
                            + " at "
                            + appointmentTime
            );
        }

        boolean alreadyBooked =
                repository.existsActiveAppointment(
                        appointment.getDentistId(),
                        appointmentDate,
                        appointmentTime
                );

        if (alreadyBooked) {

            throw new IllegalArgumentException(
                    "This dentist is already booked at "
                            + appointmentDate
                            + " "
                            + appointmentTime
                            + ". Please choose another time."
            );
        }

        appointment.setAppointmentNumber(
                generateAppointmentNumber()
        );

        appointment.setStatus("PENDING");

        return repository.save(appointment);
    }

    public List<Appointment> getAllAppointments()
            throws SQLException {

        return repository.findAll();
    }

    /*
     * View one appointment.
     */
    public Appointment getAppointmentById(Long id)
            throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Appointment ID is required."
            );
        }

        Appointment appointment =
                repository.findById(id);

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }

        return appointment;
    }

    /*
     * Update appointment.
     */
    public boolean updateAppointment(
            Appointment appointment)
            throws SQLException {

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment is required."
            );
        }

        if (appointment.getId() == null) {
            throw new IllegalArgumentException(
                    "Appointment ID is required."
            );
        }

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

        if (appointment.getAppointmentDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past."
            );
        }

        /*
         * Check whether another appointment already
         * uses the same dentist, date and time.
         *
         * We ignore the current appointment itself.
         */
        List<Appointment> appointments =
                repository.findAll();

        for (Appointment existing : appointments) {

            if (existing.getId().equals(
                    appointment.getId())) {

                continue;
            }

            if (!existing.getDentistId().equals(
                    appointment.getDentistId())) {

                continue;
            }

            if (!existing.getAppointmentDate().equals(
                    appointment.getAppointmentDate())) {

                continue;
            }

            if (!existing.getAppointmentTime().equals(
                    appointment.getAppointmentTime())) {

                continue;
            }

            if ("PENDING".equalsIgnoreCase(
                    existing.getStatus())
                    ||
                    "CONFIRMED".equalsIgnoreCase(
                            existing.getStatus())) {

                throw new IllegalArgumentException(
                        "This dentist is already booked at "
                                + appointment.getAppointmentDate()
                                + " "
                                + appointment.getAppointmentTime()
                                + ". Please choose another time."
                );
            }
        }

        /*
         * Keep the original appointment number.
         */
        Appointment existing =
                repository.findById(
                        appointment.getId()
                );

        if (existing == null) {
            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }

        appointment.setAppointmentNumber(
                existing.getAppointmentNumber()
        );

        return repository.update(appointment);
    }

    /*
     * Delete appointment.
     */
    public boolean deleteAppointment(Long id)
            throws SQLException {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Appointment ID is required."
            );
        }

        Appointment appointment =
                repository.findById(id);

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }

        return repository.delete(id);
    }

    /*
     * Generate unique appointment number.
     */
    private String generateAppointmentNumber() {

        return "APT-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}