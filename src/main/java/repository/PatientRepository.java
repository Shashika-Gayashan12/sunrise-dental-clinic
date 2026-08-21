package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {

    public Patient save(Patient patient) throws SQLException {

        String sql = """
                INSERT INTO patients (patient_name, address, contact_number)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, patient.getPatientName());
            statement.setString(2, patient.getAddress());
            statement.setString(3, patient.getContactNumber());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    patient.setId(generatedKeys.getLong(1));
                }
            }
        }

        return patient;
    }

    public List<Patient> findAll() throws SQLException {

        List<Patient> patients = new ArrayList<>();

        String sql = """
                SELECT id, patient_name, address, contact_number
                FROM patients
                ORDER BY id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Patient patient = new Patient(
                        resultSet.getLong("id"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("address"),
                        resultSet.getString("contact_number")
                );

                patients.add(patient);
            }
        }

        return patients;
    }

    public Patient findById(Long id) throws SQLException {

        String sql = """
                SELECT id, patient_name, address, contact_number
                FROM patients
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Patient(
                            resultSet.getLong("id"),
                            resultSet.getString("patient_name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number")
                    );
                }
            }
        }

        return null;
    }
}