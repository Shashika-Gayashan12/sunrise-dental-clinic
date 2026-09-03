package com.sunrise.dentalclinic.service;

import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {


    private final UserRepository userRepository;

    public UserService() {

        this.userRepository =
                new UserRepository();
    }


/* =========================
   LOGIN
   ========================= */

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


/* =========================
   CREATE USER
   ========================= */

    public User createUser(
            String username,
            String password)
            throws SQLException {

        validateCredentials(
                username,
                password
        );

        username =
                username.trim();

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


/* =========================
   CREATE ADMIN
   ========================= */

    public User createAdmin(
            String username,
            String password)
            throws SQLException {

        validateCredentials(
                username,
                password
        );

        username =
                username.trim();

        User existing =
                userRepository.findByUsername(
                        username
                );

        if (existing != null) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        User admin =
                new User(
                        null,
                        username,
                        password,
                        "ADMIN",
                        "ACTIVE"
                );

        return userRepository.saveAdmin(
                admin
        );
    }


/* =========================
   CREATE DENTIST ACCOUNT
   ========================= */

    public User createDentistAccount(
            String username,
            String password,
            Long dentistId)
            throws SQLException {

        validateCredentials(
                username,
                password
        );

        if (dentistId == null ||
                dentistId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid dentist ID."
            );
        }

        username =
                username.trim();

        User existing =
                userRepository.findByUsername(
                        username
                );

        if (existing != null) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        User dentist =
                new User(
                        null,
                        username,
                        password,
                        "DENTIST",
                        "ACTIVE",
                        dentistId
                );

        return userRepository.save(
                dentist
        );
    }


/* =========================
   GET ALL USERS
   ========================= */

    public List<User> getAllUsers()
            throws SQLException {

        return userRepository.findAll();
    }


/* =========================
   GET USER BY ID
   ========================= */

    public User getUserById(Long id)
            throws SQLException {

        validateUserId(id);

        return userRepository.findById(id);
    }


/* =========================
   ACTIVATE
   ========================= */

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


/* =========================
   DEACTIVATE
   ========================= */

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


/* =========================
   DELETE
   ========================= */

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

        if ("ADMIN".equalsIgnoreCase(
                user.getRole())) {

            throw new IllegalArgumentException(
                    "Admin account cannot be deleted."
            );
        }

        userRepository.deleteById(id);
    }


/* =========================
   UPDATE ADMIN PASSWORD
   ========================= */

    public void updateAdminPassword(
            Long id,
            String password)
            throws SQLException {

        validateUserId(id);

        if (password == null ||
                password.isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required."
            );
        }

        User user =
                userRepository.findById(id);

        if (user == null) {

            throw new IllegalArgumentException(
                    "User not found."
            );
        }

        if (!"ADMIN".equalsIgnoreCase(
                user.getRole())) {

            throw new IllegalArgumentException(
                    "Password can only be changed for an admin account."
            );
        }

        userRepository.updatePassword(
                id,
                password
        );
    }


/* =========================
   VALIDATION
   ========================= */

    private void validateCredentials(
            String username,
            String password) {

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
    }


    private void validateUserId(
            Long id) {

        if (id == null ||
                id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }
    }


}
