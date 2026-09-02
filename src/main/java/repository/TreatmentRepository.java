package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.Treatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentRepository {

    public Treatment save(Treatment treatment)
            throws SQLException {

        String sql = """
                INSERT INTO treatments
                (treatment_name, treatment_cost)
                VALUES (?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    treatment.getTreatmentName()
            );

            statement.setBigDecimal(
                    2,
                    treatment.getTreatmentCost()
            );

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {
                    treatment.setId(
                            keys.getLong(1)
                    );
                }
            }
        }

        return treatment;
    }


    public List<Treatment> findAll()
            throws SQLException {

        List<Treatment> treatments =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    treatment_name,
                    treatment_cost
                FROM treatments
                ORDER BY treatment_name
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Treatment treatment =
                        new Treatment(
                                resultSet.getLong("id"),

                                resultSet.getString(
                                        "treatment_name"
                                ),

                                resultSet.getBigDecimal(
                                        "treatment_cost"
                                )
                        );

                treatments.add(treatment);
            }
        }

        return treatments;
    }


    public Treatment findById(Long id)
            throws SQLException {

        String sql = """
                SELECT
                    id,
                    treatment_name,
                    treatment_cost
                FROM treatments
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

                    return new Treatment(
                            resultSet.getLong("id"),

                            resultSet.getString(
                                    "treatment_name"
                            ),

                            resultSet.getBigDecimal(
                                    "treatment_cost"
                            )
                    );
                }
            }
        }

        return null;
    }
}