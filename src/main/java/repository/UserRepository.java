package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.config.DatabaseConnection;
import com.sunrise.dentalclinic.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    /*
     * Find user by username.
     */
    public User findByUsername(String username)
            throws SQLException {

        String sql = """
                SELECT id, username, password, role, status
                FROM users
                WHERE username = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return new User(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role"),
                            resultSet.getString("status")
                    );
                }
            }
        }

        return null;
    }

    /*
     * Find user by ID.
     */
    public User findById(Long id)
            throws SQLException {

        String sql = """
                SELECT id, username, password, role, status
                FROM users
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

                    return new User(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role"),
                            resultSet.getString("status")
                    );
                }
            }
        }

        return null;
    }

    /*
     * Get all users.
     */
    public List<User> findAll()
            throws SQLException {

        List<User> users = new ArrayList<>();

        String sql = """
                SELECT id, username, password, role, status
                FROM users
                ORDER BY id DESC
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("status")
                );

                users.add(user);
            }
        }

        return users;
    }

    /*
     * Save a new user.
     */
    public User save(User user)
            throws SQLException {

        String sql = """
                INSERT INTO users
                (username, password, role, status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    user.getUsername()
            );

            statement.setString(
                    2,
                    user.getPassword()
            );

            statement.setString(
                    3,
                    user.getRole()
            );

            statement.setString(
                    4,
                    user.getStatus()
            );

            statement.executeUpdate();

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    user.setId(
                            generatedKeys.getLong(1)
                    );
                }
            }
        }

        return user;
    }

    /*
     * Update user status.
     */
    public void updateStatus(
            Long id,
            String status)
            throws SQLException {

        String sql = """
                UPDATE users
                SET status = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    /*
     * Update username, role and status.
     */
    public void updateUser(
            User user)
            throws SQLException {

        String sql = """
                UPDATE users
                SET username = ?,
                    role = ?,
                    status = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    user.getUsername()
            );

            statement.setString(
                    2,
                    user.getRole()
            );

            statement.setString(
                    3,
                    user.getStatus()
            );

            statement.setLong(
                    4,
                    user.getId()
            );

            statement.executeUpdate();
        }
    }

    /*
     * Delete user.
     */
    public void deleteById(Long id)
            throws SQLException {

        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        }
    }
}