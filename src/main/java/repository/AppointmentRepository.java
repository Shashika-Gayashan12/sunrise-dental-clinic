package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository {

    public Appointment save(Appointment appointment)
            throws SQLException {

        String sql = """
                INSERT INTO appointments
                (
                    appointment_date,
                    appointment_number,
                    appointment_time,
                    status,
                    dentist_id,
                    patient_id,
                    treatment_id
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
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

                Appointment appointment =
                        new Appointment(
                                resultSet.getLong("id"),

                                resultSet.getDate(
                                        "appointment_date"
                                ).toLocalDate(),

                                resultSet.getString(
                                        "appointment_number"
                                ),

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
                                )
                        );

                appointments.add(appointment);
            }
        }

        return appointments;
    }

    public Appointment findById(Long id)
            throws SQLException {

        String sql = """
                SELECT
                    id,
                    appointment_date,
                    appointment_number,
                    appointment_time,
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

            statement.setLong(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Appointment(
                            resultSet.getLong("id"),

                            resultSet.getDate(
                                    "appointment_date"
                            ).toLocalDate(),

                            resultSet.getString(
                                    "appointment_number"
                            ),

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
                            )
                    );
                }
            }
        }

        return null;
    }

    public boolean update(Appointment appointment)
            throws SQLException {

        String sql = """
                UPDATE appointments
                SET
                    appointment_date = ?,
                    appointment_time = ?,
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

            statement.setString(
                    3,
                    appointment.getStatus()
            );

            statement.setLong(
                    4,
                    appointment.getDentistId()
            );

            statement.setLong(
                    5,
                    appointment.getPatientId()
            );

            statement.setLong(
                    6,
                    appointment.getTreatmentId()
            );

            statement.setLong(
                    7,
                    appointment.getId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(Long id)
            throws SQLException {

        String sql = """
                DELETE FROM appointments
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean existsActiveAppointment(
            Long dentistId,
            java.time.LocalDate appointmentDate,
            java.time.LocalTime appointmentTime)
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
}