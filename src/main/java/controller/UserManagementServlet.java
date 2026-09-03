
        package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/manage-users")
public class UserManagementServlet
        extends HttpServlet {


    private UserService userService;


    @Override
    public void init() {

        userService =
                new UserService();
    }


    /* =========================
       GET
       ========================= */

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }

        loadUsersAndForward(
                request,
                response
        );
    }


    /* =========================
       POST
       ========================= */

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }


        String action =
                request.getParameter("action");


        try {


            /* =========================
               CREATE USER
               ========================= */

            if ("create".equals(action)) {

                String username =
                        request.getParameter(
                                "username"
                        );

                String password =
                        request.getParameter(
                                "password"
                        );

                userService.createUser(
                        username,
                        password
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/manage-users"
                                + "?success=user-created"
                );

                return;
            }


            /* =========================
               CREATE ADMIN
               ========================= */

            if ("create-admin".equals(action)) {

                String username =
                        request.getParameter(
                                "username"
                        );

                String password =
                        request.getParameter(
                                "password"
                        );

                userService.createAdmin(
                        username,
                        password
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/manage-users"
                                + "?success=admin-created"
                );

                return;
            }


            /* =========================
               ACTIVATE
               ========================= */

            if ("activate".equals(action)) {

                Long id =
                        parseId(
                                request.getParameter(
                                        "id"
                                )
                        );

                userService.activateUser(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/manage-users"
                );

                return;
            }


            /* =========================
               DEACTIVATE
               ========================= */

            if ("deactivate".equals(action)) {

                Long id =
                        parseId(
                                request.getParameter(
                                        "id"
                                )
                        );

                userService.deactivateUser(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/manage-users"
                );

                return;
            }


            /* =========================
               DELETE
               ========================= */

            if ("delete".equals(action)) {

                Long id =
                        parseId(
                                request.getParameter(
                                        "id"
                                )
                        );

                userService.deleteUser(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/manage-users"
                );

                return;
            }


            /* =========================
               UPDATE ADMIN PASSWORD
               ========================= */

            if ("update-password".equals(action)) {

                Long id =
                        parseId(
                                request.getParameter(
                                        "id"
                                )
                        );

                String password =
                        request.getParameter(
                                "password"
                        );

                userService.updateAdminPassword(
                        id,
                        password
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/manage-users"
                                + "?success=password-updated"
                );

                return;
            }


            throw new IllegalArgumentException(
                    "Invalid action."
            );


        } catch (Exception e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            loadUsersAndForward(
                    request,
                    response
            );
        }
    }


    /* =========================
       LOAD USERS
       ========================= */

    private void loadUsersAndForward(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<User> users =
                    userService.getAllUsers();


            request.setAttribute(
                    "users",
                    users
            );


            String success =
                    request.getParameter(
                            "success"
                    );


            if ("user-created".equals(success)) {

                request.setAttribute(
                        "success",
                        "User account created successfully."
                );

            } else if ("admin-created".equals(success)) {

                request.setAttribute(
                        "success",
                        "Admin account created successfully."
                );

            } else if ("password-updated".equals(success)) {

                request.setAttribute(
                        "success",
                        "Admin password updated successfully."
                );
            }


            request.getRequestDispatcher(
                    "/WEB-INF/views/manage-users.jsp"
            ).forward(
                    request,
                    response
            );


        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load users.",
                    e
            );
        }
    }


    /* =========================
       PARSE ID
       ========================= */

    private Long parseId(
            String idValue) {

        if (idValue == null ||
                idValue.isBlank()) {

            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

        try {

            return Long.parseLong(
                    idValue
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }
    }


    /* =========================
       ADMIN CHECK
       ========================= */

    private boolean isAdmin(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);


        if (session == null) {

            return false;
        }


        Object loggedInUser =
                session.getAttribute(
                        "loggedInUser"
                );


        if (!(loggedInUser
                instanceof User)) {

            return false;
        }


        User user =
                (User) loggedInUser;


        return
                "ADMIN".equalsIgnoreCase(
                        user.getRole()
                )
                        &&
                        "ACTIVE".equalsIgnoreCase(
                                user.getStatus()
                        );
    }

}

