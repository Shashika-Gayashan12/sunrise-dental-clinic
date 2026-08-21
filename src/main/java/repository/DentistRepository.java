package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.Dentist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DentistRepository {

    public Dentist save(Dentist dentist) throws SQLException {

        String sql = """
                INSERT INTO dentists
                (dentist_name, specialization, contact_number)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, dentist.getDentistName());
            statement.setString(2, dentist.getSpecialization());
            statement.setString(3, dentist.getContactNumber());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dentist.setId(generatedKeys.getLong(1));
                }
            }
        }

        return dentist;
    }

    public List<Dentist> findAll() throws SQLException {

        List<Dentist> dentists = new ArrayList<>();

        String sql = """
                SELECT id,
                       dentist_name,
                       specialization,
                       contact_number
                FROM dentists
                ORDER BY id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Dentist dentist = new Dentist(
                        resultSet.getLong("id"),
                        resultSet.getString("dentist_name"),
                        resultSet.getString("specialization"),
                        resultSet.getString("contact_number")
                );

                dentists.add(dentist);
            }
        }

        return dentists;
    }

    public Dentist findById(Long id) throws SQLException {

        String sql = """
                SELECT id,
                       dentist_name,
                       specialization,
                       contact_number
                FROM dentists
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new Dentist(
                            resultSet.getLong("id"),
                            resultSet.getString("dentist_name"),
                            resultSet.getString("specialization"),
                            resultSet.getString("contact_number")
                    );
                }
            }
        }

        return null;
    }
}