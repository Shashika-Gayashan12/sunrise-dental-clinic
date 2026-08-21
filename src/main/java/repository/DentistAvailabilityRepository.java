package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.DentistAvailability;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DentistAvailabilityRepository {

    public DentistAvailability save(
            DentistAvailability availability)
            throws SQLException {

        String sql = """
                INSERT INTO dentist_availability
                (
                    dentist_id,
                    day_of_week,
                    available_date,
                    start_time,
                    end_time
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    availability.getDentistId()
            );

            statement.setString(
                    2,
                    availability.getDayOfWeek()
            );

            if (availability.getAvailableDate() != null) {

                statement.setDate(
                        3,
                        Date.valueOf(
                                availability.getAvailableDate()
                        )
                );

            } else {

                statement.setNull(
                        3,
                        Types.DATE
                );
            }

            statement.setTime(
                    4,
                    Time.valueOf(
                            availability.getStartTime()
                    )
            );

            statement.setTime(
                    5,
                    Time.valueOf(
                            availability.getEndTime()
                    )
            );

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {

                    availability.setId(
                            keys.getLong(1)
                    );
                }
            }
        }

        return availability;
    }

    public List<DentistAvailability> findByDentistId(
            Long dentistId)
            throws SQLException {

        List<DentistAvailability> availabilityList =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    dentist_id,
                    day_of_week,
                    available_date,
                    start_time,
                    end_time
                FROM dentist_availability
                WHERE dentist_id = ?
                ORDER BY available_date, start_time
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

                    Date availableDate =
                            resultSet.getDate(
                                    "available_date"
                            );

                    DentistAvailability availability =
                            new DentistAvailability(
                                    resultSet.getLong("id"),

                                    resultSet.getLong(
                                            "dentist_id"
                                    ),

                                    resultSet.getString(
                                            "day_of_week"
                                    ),

                                    availableDate != null
                                            ? availableDate.toLocalDate()
                                            : null,

                                    resultSet.getTime(
                                            "start_time"
                                    ).toLocalTime(),

                                    resultSet.getTime(
                                            "end_time"
                                    ).toLocalTime()
                            );

                    availabilityList.add(
                            availability
                    );
                }
            }
        }

        return availabilityList;
    }
}