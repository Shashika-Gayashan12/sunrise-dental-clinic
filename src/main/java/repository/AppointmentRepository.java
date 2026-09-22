
        package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.AppointmentBillingInfo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {

    // =========================================================
    // SAVE APPOINTMENT
    // =========================================================

    public Appointment save(Appointment appointment)
            throws SQLException {

        String sql = """
            INSERT INTO appointments
            (
                appointment_date,
                appointment_number,
                appointment_time,
                appointment_end_time,
                status,
                dentist_id,
                patient_id,
                treatment_id
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setDate(
                    1,
                    Date.valueOf(
                            appointment.getAppointmentDate()
                    )
            );

            statement.setString(
                    2,
                    appointment.getAppointmentNumber()
            );

            statement.setTime(
                    3,
                    Time.valueOf(
                            appointment.getAppointmentTime()
                    )
            );

            statement.setTime(
                    4,
                    Time.valueOf(
                            appointment.getAppointmentEndTime()
                    )
            );

            statement.setString(
                    5,
                    appointment.getStatus()
            );

            statement.setLong(
                    6,
                    appointment.getDentistId()
            );

            statement.setLong(
                    7,
                    appointment.getPatientId()
            );

            statement.setLong(
                    8,
                    appointment.getTreatmentId()
            );

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {
                    appointment.setId(
                            keys.getLong(1)
                    );
                }
            }
        }

        return appointment;
    }


    // =========================================================
    // GET ALL APPOINTMENTS
    // =========================================================

    public List<Appointment> findAll()
            throws SQLException {

        List<Appointment> appointments =
                new ArrayList<>();

        String sql = """
            SELECT
                id,
                appointment_date,
                appointment_number,
                appointment_time,
                appointment_end_time,
                status,
                dentist_id,
                patient_id,
                treatment_id
            FROM appointments
            ORDER BY appointment_date DESC,
                     appointment_time DESC
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                appointments.add(
                        mapAppointment(resultSet)
                );
            }
        }

        return appointments;
    }


    // =========================================================
    // GET APPOINTMENTS BY DATE
    // =========================================================

    public List<Appointment> findByDate(
            LocalDate appointmentDate)
            throws SQLException {

        List<Appointment> appointments =
                new ArrayList<>();

        String sql = """
            SELECT
                id,
                appointment_date,
                appointment_number,
                appointment_time,
                appointment_end_time,
                status,
                dentist_id,
                patient_id,
                treatment_id
            FROM appointments
            WHERE appointment_date = ?
            ORDER BY appointment_time ASC
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    Date.valueOf(appointmentDate)
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    appointments.add(
                            mapAppointment(resultSet)
                    );
                }
            }
        }

        return appointments;
    }


    // =========================================================
    // GET APPOINTMENTS BY DENTIST
    // =========================================================

    public List<Appointment> findByDentistId(
            Long dentistId)
            throws SQLException {

        List<Appointment> appointments =
                new ArrayList<>();

        String sql = """
            SELECT
                id,
                appointment_date,
                appointment_number,
                appointment_time,
                appointment_end_time,
                status,
                dentist_id,
                patient_id,
                treatment_id
            FROM appointments
            WHERE dentist_id = ?
            ORDER BY appointment_date ASC,
                     appointment_time ASC
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    dentistId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    appointments.add(
                            mapAppointment(resultSet)
                    );
                }
            }
        }

        return appointments;
    }


    // =========================================================
    // BILLING APPOINTMENTS
    // =========================================================

    public List<AppointmentBillingInfo>
    findBillingAppointmentsByDate(
            LocalDate appointmentDate)
            throws SQLException {

        List<AppointmentBillingInfo> appointments =
                new ArrayList<>();

        String sql = """
            SELECT
                a.id,
                a.appointment_date,
                a.appointment_number,
                a.appointment_time,
                a.appointment_end_time,
                a.status,
                a.dentist_id,
                a.patient_id,
                a.treatment_id,
                p.patient_name
            FROM appointments a
            INNER JOIN patients p
                ON a.patient_id = p.id
            WHERE a.appointment_date = ?
            ORDER BY a.appointment_time ASC
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    Date.valueOf(appointmentDate)
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    /*
                     * IMPORTANT:
                     * AppointmentBillingInfo currently has
                     * no end-time parameter in the project.
                     * Therefore we continue using its existing
                     * constructor structure here.
                     */

                    AppointmentBillingInfo appointment =
                            new AppointmentBillingInfo(
                                    resultSet.getLong("id"),

                                    resultSet.getString(
                                            "appointment_number"
                                    ),

                                    resultSet.getDate(
                                            "appointment_date"
                                    ).toLocalDate(),

                                    resultSet.getTime(
                                            "appointment_time"
                                    ).toLocalTime(),

                                    resultSet.getString(
                                            "status"
                                    ),

                                    resultSet.getLong(
                                            "dentist_id"
                                    ),

                                    resultSet.getLong(
                                            "patient_id"
                                    ),

                                    resultSet.getLong(
                                            "treatment_id"
                                    ),

                                    resultSet.getString(
                                            "patient_name"
                                    )
                            );

                    appointments.add(appointment);
                }
            }
        }

        return appointments;
    }


    // =========================================================
    // GET APPOINTMENT BY ID
    // =========================================================

    public Appointment findById(Long id)
            throws SQLException {

        String sql = """
            SELECT
                id,
                appointment_date,
                appointment_number,
                appointment_time,
                appointment_end_time,
                status,
                dentist_id,
                patient_id,
                treatment_id
            FROM appointments
            WHERE id = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    id
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapAppointment(resultSet);
                }
            }
        }

        return null;
    }


    // =========================================================
    // GET APPOINTMENT BY NUMBER
    // =========================================================

    public Appointment findByAppointmentNumber(
            String appointmentNumber)
            throws SQLException {

        String sql = """
            SELECT
                id,
                appointment_date,
                appointment_number,
                appointment_time,
                appointment_end_time,
                status,
                dentist_id,
                patient_id,
                treatment_id
            FROM appointments
            WHERE appointment_number = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    appointmentNumber
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapAppointment(resultSet);
                }
            }
        }

        return null;
    }


    // =========================================================
    // UPDATE APPOINTMENT
    // =========================================================

    public boolean update(Appointment appointment)
            throws SQLException {

        String sql = """
            UPDATE appointments
            SET
                appointment_date = ?,
                appointment_time = ?,
                appointment_end_time = ?,
                status = ?,
                dentist_id = ?,
                patient_id = ?,
                treatment_id = ?
            WHERE id = ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDate(
                    1,
                    Date.valueOf(
                            appointment.getAppointmentDate()
                    )
            );

            statement.setTime(
                    2,
                    Time.valueOf(
                            appointment.getAppointmentTime()
                    )
            );

            statement.setTime(
                    3,
                    Time.valueOf(
                            appointment.getAppointmentEndTime()
                    )
            );

            statement.setString(
                    4,
                    appointment.getStatus()
            );

            statement.setLong(
                    5,
                    appointment.getDentistId()
            );

            statement.setLong(
                    6,
                    appointment.getPatientId()
            );

            statement.setLong(
                    7,
                    appointment.getTreatmentId()
            );

            statement.setLong(
                    8,
                    appointment.getId()
            );

            return statement.executeUpdate() > 0;
        }
    }


    // =========================================================
    // CANCEL APPOINTMENT
    // =========================================================

    public boolean cancel(Long id)
            throws SQLException {

        String sql = """
            UPDATE appointments
            SET status = 'CANCELLED'
            WHERE id = ?
              AND status IN ('PENDING', 'CONFIRMED')
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    id
            );

            return statement.executeUpdate() > 0;
        }
    }


    // =========================================================
    // CHECK OVERLAPPING ACTIVE APPOINTMENT
    // =========================================================
    //
    // Existing appointment:
    //     10:00 - 11:00
    //
    // New appointment:
    //     10:30 - 11:30
    //
    // Result:
    //     TRUE -> overlap -> NOT AVAILABLE
    //
    // Existing:
    //     10:00 - 11:00
    //
    // New:
    //     11:00 - 12:00
    //
    // Result:
    //     FALSE -> no overlap -> AVAILABLE
    //
    // =========================================================

    public boolean existsOverlappingAppointment(
            Long dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentStartTime,
            LocalTime appointmentEndTime)
            throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM appointments
            WHERE dentist_id = ?
              AND appointment_date = ?
              AND status IN ('PENDING', 'CONFIRMED')
              AND appointment_time < ?
              AND appointment_end_time > ?
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    dentistId
            );

            statement.setDate(
                    2,
                    Date.valueOf(appointmentDate)
            );

            /*
             * Existing appointment START < New appointment END
             */
            statement.setTime(
                    3,
                    Time.valueOf(appointmentEndTime)
            );

            /*
             * Existing appointment END > New appointment START
             */
            statement.setTime(
                    4,
                    Time.valueOf(appointmentStartTime)
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }


    // =========================================================
    // OLD EXACT-TIME CHECK
    // =========================================================
    //
    // Kept for compatibility with existing code.
    // The new AppointmentService should use
    // existsOverlappingAppointment() instead.
    //
    // =========================================================

    public boolean existsActiveAppointment(
            Long dentistId,
            LocalDate appointmentDate,
            LocalTime appointmentTime)
            throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM appointments
            WHERE dentist_id = ?
              AND appointment_date = ?
              AND appointment_time = ?
              AND status IN ('PENDING', 'CONFIRMED')
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    dentistId
            );

            statement.setDate(
                    2,
                    Date.valueOf(appointmentDate)
            );

            statement.setTime(
                    3,
                    Time.valueOf(appointmentTime)
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }


    // =========================================================
    // GET LAST APPOINTMENT NUMBER
    // =========================================================

    public int getLastAppointmentNumber()
            throws SQLException {

        String sql = """
            SELECT appointment_number
            FROM appointments
            WHERE appointment_number LIKE 'APT-%'
            ORDER BY id DESC
            LIMIT 1
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {

                String number =
                        resultSet.getString(
                                "appointment_number"
                        );

                try {

                    return Integer.parseInt(
                            number.substring(4)
                    );

                } catch (Exception ignored) {

                    return 0;
                }
            }
        }

        return 0;
    }


    // =========================================================
    // MAP RESULTSET TO APPOINTMENT
    // =========================================================

    private Appointment mapAppointment(
            ResultSet resultSet)
            throws SQLException {

        Appointment appointment =
                new Appointment();

        appointment.setId(
                resultSet.getLong("id")
        );

        appointment.setAppointmentDate(
                resultSet.getDate(
                        "appointment_date"
                ).toLocalDate()
        );

        appointment.setAppointmentNumber(
                resultSet.getString(
                        "appointment_number"
                )
        );

        appointment.setAppointmentTime(
                resultSet.getTime(
                        "appointment_time"
                ).toLocalTime()
        );

        Time endTime =
                resultSet.getTime(
                        "appointment_end_time"
                );

        if (endTime != null) {
            appointment.setAppointmentEndTime(
                    endTime.toLocalTime()
            );
        }

        appointment.setStatus(
                resultSet.getString(
                        "status"
                )
        );

        appointment.setDentistId(
                resultSet.getLong(
                        "dentist_id"
                )
        );

        appointment.setPatientId(
                resultSet.getLong(
                        "patient_id"
                )
        );

        appointment.setTreatmentId(
                resultSet.getLong(
                        "treatment_id"
                )
        );

        return appointment;
    }
}
