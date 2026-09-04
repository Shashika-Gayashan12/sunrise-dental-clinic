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
                    "Appointment time is required."
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

        boolean available =
                isDentistAvailable(
                        appointment.getDentistId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime()
                );

        if (!available) {
            throw new IllegalArgumentException(
                    "Dentist is not available at the selected date and time."
            );
        }

        boolean alreadyExists =
                appointmentRepository
                        .existsActiveAppointment(
                                appointment.getDentistId(),
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime()
                        );

        if (alreadyExists) {
            throw new IllegalArgumentException(
                    "This dentist already has an appointment at the selected date and time."
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
//
// Used by Dentist Dashboard.
//
// Only appointments belonging to the logged-in
// dentist's dentistId should be passed here.
//
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
                    "Appointment time is required."
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

        boolean duplicate =
                appointmentRepository
                        .existsActiveAppointment(
                                appointment.getDentistId(),
                                appointment.getAppointmentDate(),
                                appointment.getAppointmentTime()
                        );

        Appointment existing =
                appointmentRepository.findById(
                        appointment.getId()
                );

        if (duplicate &&
                (existing == null ||
                        !appointment.getId()
                                .equals(existing.getId()))) {

            throw new IllegalArgumentException(
                    "This dentist already has an appointment at the selected date and time."
            );
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
            LocalTime time)
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

            if (!time.isBefore(
                    schedule.getStartTime()
            ) &&
                    time.isBefore(
                            schedule.getEndTime()
                    )) {

                return true;
            }
        }

        return false;
    }


}
