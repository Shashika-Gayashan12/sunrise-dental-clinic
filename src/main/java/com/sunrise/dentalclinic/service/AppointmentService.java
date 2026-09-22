package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.AppointmentBillingInfo;
import com.sunrise.dentalclinic.repository.AppointmentRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentRepository appointmentRepository =
            new AppointmentRepository();

    private final DentistAvailabilityService
            dentistAvailabilityService =
            new DentistAvailabilityService();


// =========================================================
// CREATE APPOINTMENT
// =========================================================

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
                    "Appointment start time is required."
            );
        }

        if (appointment.getAppointmentEndTime() == null) {
            throw new IllegalArgumentException(
                    "Appointment end time is required."
            );
        }

        // End time must be after start time
        if (!appointment.getAppointmentEndTime()
                .isAfter(appointment.getAppointmentTime())) {

            throw new IllegalArgumentException(
                    "Appointment end time must be after start time."
            );
        }

        if (appointment.getDentistId() == null ||
                appointment.getDentistId() <= 0) {

            throw new IllegalArgumentException(
                    "Dentist is required."
            );
        }

        if (appointment.getPatientId() == null ||
                appointment.getPatientId() <= 0) {

            throw new IllegalArgumentException(
                    "Patient is required."
            );
        }

        if (appointment.getTreatmentId() == null ||
                appointment.getTreatmentId() <= 0) {

            throw new IllegalArgumentException(
                    "Treatment is required."
            );
        }

        if (appointment.getAppointmentDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past."
            );
        }

        // Check dentist working/available schedule
        boolean available =
                isDentistAvailable(
                        appointment.getDentistId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        appointment.getAppointmentEndTime()
                );

        if (!available) {
            throw new IllegalArgumentException(
                    "Dentist is not available for the selected date and time range."
            );
        }

        // Check overlapping appointments
        boolean overlapping =
                appointmentRepository
                        .existsOverlappingAppointment(
                                appointment.getDentistId(),
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime(),
                                appointment.getAppointmentEndTime()
                        );

        if (overlapping) {
            throw new IllegalArgumentException(
                    "This dentist already has an appointment during the selected time."
            );
        }

        int lastNumber =
                appointmentRepository
                        .getLastAppointmentNumber();

        String appointmentNumber =
                "APT-" +
                        String.format(
                                "%07d",
                                lastNumber + 1
                        );

        appointment.setAppointmentNumber(
                appointmentNumber
        );

        if (appointment.getStatus() == null ||
                appointment.getStatus().isBlank()) {

            appointment.setStatus(
                    "PENDING"
            );
        }

        return appointmentRepository.save(
                appointment
        );
    }


// =========================================================
// GET ALL APPOINTMENTS
// =========================================================

    public List<Appointment> getAllAppointments()
            throws SQLException {

        return appointmentRepository.findAll();
    }


// =========================================================
// GET APPOINTMENTS BY DENTIST
// =========================================================

    public List<Appointment> getAppointmentsByDentistId(
            Long dentistId)
            throws SQLException {

        if (dentistId == null ||
                dentistId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid dentist ID."
            );
        }

        return appointmentRepository.findByDentistId(
                dentistId
        );
    }


// =========================================================
// GET APPOINTMENTS BY DATE
// =========================================================

    public List<Appointment> getAppointmentsByDate(
            LocalDate appointmentDate)
            throws SQLException {

        if (appointmentDate == null) {
            throw new IllegalArgumentException(
                    "Appointment date is required."
            );
        }

        return appointmentRepository.findByDate(
                appointmentDate
        );
    }


// =========================================================
// GET BILLING APPOINTMENTS BY DATE
// =========================================================

    public List<AppointmentBillingInfo>
    getBillingAppointmentsByDate(
            LocalDate appointmentDate)
            throws SQLException {

        if (appointmentDate == null) {
            throw new IllegalArgumentException(
                    "Appointment date is required."
            );
        }

        return appointmentRepository
                .findBillingAppointmentsByDate(
                        appointmentDate
                );
    }


// =========================================================
// GET APPOINTMENT BY ID
// =========================================================

    public Appointment getAppointmentById(
            Long id)
            throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid appointment ID."
            );
        }

        Appointment appointment =
                appointmentRepository.findById(id);

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }

        return appointment;
    }


// =========================================================
// GET APPOINTMENT BY NUMBER
// =========================================================

    public Appointment getAppointmentByNumber(
            String appointmentNumber)
            throws SQLException {

        if (appointmentNumber == null ||
                appointmentNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Appointment number is required."
            );
        }

        Appointment appointment =
                appointmentRepository
                        .findByAppointmentNumber(
                                appointmentNumber.trim()
                        );

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }

        return appointment;
    }


// =========================================================
// UPDATE APPOINTMENT
// =========================================================

    public void updateAppointment(
            Appointment appointment)
            throws SQLException {

        if (appointment == null ||
                appointment.getId() == null ||
                appointment.getId() <= 0) {

            throw new IllegalArgumentException(
                    "Valid appointment is required."
            );
        }

        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException(
                    "Appointment date is required."
            );
        }

        if (appointment.getAppointmentTime() == null) {
            throw new IllegalArgumentException(
                    "Appointment start time is required."
            );
        }

        if (appointment.getAppointmentEndTime() == null) {
            throw new IllegalArgumentException(
                    "Appointment end time is required."
            );
        }

        // End time must be after start time
        if (!appointment.getAppointmentEndTime()
                .isAfter(appointment.getAppointmentTime())) {

            throw new IllegalArgumentException(
                    "Appointment end time must be after start time."
            );
        }

        if (appointment.getDentistId() == null ||
                appointment.getDentistId() <= 0) {

            throw new IllegalArgumentException(
                    "Dentist is required."
            );
        }

        if (appointment.getPatientId() == null ||
                appointment.getPatientId() <= 0) {

            throw new IllegalArgumentException(
                    "Patient is required."
            );
        }

        if (appointment.getTreatmentId() == null ||
                appointment.getTreatmentId() <= 0) {

            throw new IllegalArgumentException(
                    "Treatment is required."
            );
        }

        if (appointment.getAppointmentDate()
                .isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past."
            );
        }

        // Check dentist working/available schedule
        boolean available =
                isDentistAvailable(
                        appointment.getDentistId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime(),
                        appointment.getAppointmentEndTime()
                );

        if (!available) {
            throw new IllegalArgumentException(
                    "Dentist is not available for the selected date and time range."
            );
        }

        // Get existing appointment first
        Appointment existing =
                appointmentRepository.findById(
                        appointment.getId()
                );

        if (existing == null) {
            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }

        // Check overlapping appointments
        boolean overlapping =
                appointmentRepository
                        .existsOverlappingAppointment(
                                appointment.getDentistId(),
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime(),
                                appointment.getAppointmentEndTime()
                        );

        /*
         * If the overlap found is the same appointment
         * being edited, it should not block the update.
         */
        if (overlapping) {

            List<Appointment> appointments =
                    appointmentRepository.findByDentistId(
                            appointment.getDentistId()
                    );

            for (Appointment other : appointments) {

                if (other.getId() == null ||
                        other.getId().equals(
                                appointment.getId()
                        )) {

                    continue;
                }

                if (other.getAppointmentDate() == null ||
                        other.getAppointmentTime() == null ||
                        other.getAppointmentEndTime() == null) {

                    continue;
                }

                if (!other.getAppointmentDate()
                        .equals(
                                appointment.getAppointmentDate()
                        )) {

                    continue;
                }

                boolean timeOverlap =
                        other.getAppointmentTime()
                                .isBefore(
                                        appointment.getAppointmentEndTime()
                                )
                                &&
                                other.getAppointmentEndTime()
                                        .isAfter(
                                                appointment.getAppointmentTime()
                                        );

                if (timeOverlap) {
                    throw new IllegalArgumentException(
                            "This dentist already has an appointment during the selected time."
                    );
                }
            }
        }

        boolean updated =
                appointmentRepository.update(
                        appointment
                );

        if (!updated) {
            throw new IllegalArgumentException(
                    "Unable to update appointment."
            );
        }
    }


// =========================================================
// CANCEL APPOINTMENT
// =========================================================

    public void cancelAppointment(
            Long id)
            throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid appointment ID."
            );
        }

        Appointment appointment =
                appointmentRepository.findById(id);

        if (appointment == null) {
            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }

        if ("CANCELLED".equalsIgnoreCase(
                appointment.getStatus())) {

            throw new IllegalArgumentException(
                    "Appointment is already cancelled."
            );
        }

        if ("COMPLETED".equalsIgnoreCase(
                appointment.getStatus())) {

            throw new IllegalArgumentException(
                    "Completed appointment cannot be cancelled."
            );
        }

        boolean cancelled =
                appointmentRepository.cancel(id);

        if (!cancelled) {
            throw new IllegalArgumentException(
                    "Unable to cancel appointment."
            );
        }
    }


// =========================================================
// DENTIST AVAILABILITY
// =========================================================

    private boolean isDentistAvailable(
            Long dentistId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime)
            throws SQLException {

        var schedules =
                dentistAvailabilityService
                        .getByDentistId(dentistId);

        String dayName =
                date.getDayOfWeek()
                        .name();

        for (var schedule : schedules) {

            if (schedule.getStartTime() == null ||
                    schedule.getEndTime() == null) {

                continue;
            }

            boolean correctDate =
                    schedule.getAvailableDate() != null &&
                            schedule.getAvailableDate()
                                    .equals(date);

            boolean correctDay =
                    schedule.getAvailableDate() == null &&
                            schedule.getDayOfWeek() != null &&
                            schedule.getDayOfWeek()
                                    .equalsIgnoreCase(
                                            dayName
                                    );

            if (!correctDate && !correctDay) {
                continue;
            }

            /*
             * The complete appointment range must fit
             * inside the dentist's available schedule.
             *
             * Example:
             *
             * Dentist available: 09:00 - 17:00
             *
             * Appointment: 10:00 - 11:00  -> allowed
             * Appointment: 16:30 - 17:30  -> rejected
             */
            boolean startIsValid =
                    !startTime.isBefore(
                            schedule.getStartTime()
                    );

            boolean endIsValid =
                    !endTime.isAfter(
                            schedule.getEndTime()
                    );

            if (startIsValid && endIsValid) {
                return true;
            }
        }

        return false;
    }

}