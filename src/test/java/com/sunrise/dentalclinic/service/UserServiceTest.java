package com.sunrise.dentalclinic.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private final UserService userService =
            new UserService();

    @Test
    void loginShouldRejectEmptyUsername() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.login("", "123456")
                );

        assertEquals(
                "Username is required.",
                exception.getMessage()
        );
    }

    @Test
    void loginShouldRejectNullUsername() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.login(null, "123456")
                );

        assertEquals(
                "Username is required.",
                exception.getMessage()
        );
    }

    @Test
    void loginShouldRejectEmptyPassword() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.login("admin", "")
                );

        assertEquals(
                "Password is required.",
                exception.getMessage()
        );
    }

    @Test
    void loginShouldRejectNullPassword() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.login("admin", null)
                );

        assertEquals(
                "Password is required.",
                exception.getMessage()
        );
    }

    @Test
    void createUserShouldRejectEmptyUsername() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createUser("", "123456")
                );

        assertEquals(
                "Username is required.",
                exception.getMessage()
        );
    }

    @Test
    void createUserShouldRejectEmptyPassword() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createUser("testuser", "")
                );

        assertEquals(
                "Password is required.",
                exception.getMessage()
        );
    }

    @Test
    void createAdminShouldRejectEmptyUsername() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createAdmin("", "123456")
                );

        assertEquals(
                "Username is required.",
                exception.getMessage()
        );
    }

    @Test
    void createAdminShouldRejectEmptyPassword() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createAdmin("admin", "")
                );

        assertEquals(
                "Password is required.",
                exception.getMessage()
        );
    }

    @Test
    void createDentistAccountShouldRejectInvalidDentistId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createDentistAccount(
                                "Dr.Test",
                                "123456",
                                null
                        )
                );

        assertEquals(
                "Invalid dentist ID.",
                exception.getMessage()
        );
    }

    @Test
    void createDentistAccountShouldRejectZeroDentistId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.createDentistAccount(
                                "Dr.Test",
                                "123456",
                                0L
                        )
                );

        assertEquals(
                "Invalid dentist ID.",
                exception.getMessage()
        );
    }

    @Test
    void getUserByIdShouldRejectInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.getUserById(0L)
                );

        assertEquals(
                "Invalid user ID.",
                exception.getMessage()
        );
    }

    @Test
    void getUserByIdShouldRejectNullId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.getUserById(null)
                );

        assertEquals(
                "Invalid user ID.",
                exception.getMessage()
        );
    }

    @Test
    void deleteUserShouldRejectInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.deleteUser(0L)
                );

        assertEquals(
                "Invalid user ID.",
                exception.getMessage()
        );
    }

    @Test
    void updateAdminPasswordShouldRejectInvalidId() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.updateAdminPassword(
                                0L,
                                "newpassword"
                        )
                );

        assertEquals(
                "Invalid user ID.",
                exception.getMessage()
        );
    }

    @Test
    void updateAdminPasswordShouldRejectEmptyPassword() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> userService.updateAdminPassword(
                                1L,
                                ""
                        )
                );

        assertEquals(
                "Password is required.",
                exception.getMessage()
        );
    }
}

