package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    /*
     * LOGIN
     *
     * Only ACTIVE users can log in.
     */
    public User login(
            String username,
            String password)
            throws SQLException {

        if (username == null ||
                username.isBlank()) {

            throw new IllegalArgumentException(
                    "Username is required."
            );
        }

        if (password == null ||
                password.isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required."
            );
        }

        User user =
                userRepository.findByUsername(
                        username.trim()
                );

        if (user == null) {

            throw new IllegalArgumentException(
                    "Invalid username or password."
            );
        }

        if (!password.equals(
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid username or password."
            );
        }

        /*
         * PENDING users cannot log in.
         */
        if (!"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            if ("PENDING".equalsIgnoreCase(
                    user.getStatus())) {

                throw new IllegalArgumentException(
                        "Your account is waiting for admin approval."
                );
            }

            throw new IllegalArgumentException(
                    "Your account is not active."
            );
        }

        return user;
    }

    /*
     * CREATE USER
     *
     * New users start as PENDING.
     */
    public User createUser(
            String username,
            String password)
            throws SQLException {

        if (username == null ||
                username.isBlank()) {

            throw new IllegalArgumentException(
                    "Username is required."
            );
        }

        if (password == null ||
                password.isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required."
            );
        }

        username = username.trim();

        User existing =
                userRepository.findByUsername(
                        username
                );

        if (existing != null) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        User user =
                new User(
                        null,
                        username,
                        password,
                        "USER",
                        "PENDING"
                );

        return userRepository.save(user);
    }

    /*
     * GET ALL USERS
     */
    public List<User> getAllUsers()
            throws SQLException {

        return userRepository.findAll();
    }

    /*
     * GET USER BY ID
     */
    public User getUserById(Long id)
            throws SQLException {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }

        return userRepository.findById(id);
    }

    /*
     * APPROVE USER
     */
    public void activateUser(Long id)
            throws SQLException {

        validateUserId(id);

        User user =
                userRepository.findById(id);

        if (user == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }

        userRepository.updateStatus(
                id,
                "ACTIVE"
        );
    }

    /*
     * DEACTIVATE USER
     */
    public void deactivateUser(Long id)
            throws SQLException {

        validateUserId(id);

        User user =
                userRepository.findById(id);

        if (user == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }

        /*
         * Prevent deactivating ADMIN.
         */
        if ("ADMIN".equalsIgnoreCase(
                user.getRole())) {

            throw new IllegalArgumentException(
                    "Admin account cannot be deactivated."
            );
        }

        userRepository.updateStatus(
                id,
                "INACTIVE"
        );
    }

    /*
     * DELETE USER
     */
    public void deleteUser(Long id)
            throws SQLException {

        validateUserId(id);

        User user =
                userRepository.findById(id);

        if (user == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }

        /*
         * Prevent deleting ADMIN.
         */
        if ("ADMIN".equalsIgnoreCase(
                user.getRole())) {

            throw new IllegalArgumentException(
                    "Admin account cannot be deleted."
            );
        }

        userRepository.deleteById(id);
    }

    /*
     * Validate ID.
     */
    private void validateUserId(Long id) {

        if (id == null || id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }
    }
}