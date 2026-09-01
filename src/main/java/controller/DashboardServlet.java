package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.service.AppointmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final AppointmentService appointmentService =
            new AppointmentService();


    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        // =====================================================
        // CHECK LOGIN
        // =====================================================

        HttpSession session =
                request.getSession(false);


        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }


        Object userObject =
                session.getAttribute(
                        "loggedInUser"
                );


        if (!(userObject instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }


        User user =
                (User) userObject;


        // =====================================================
        // CHECK USER STATUS
        // =====================================================

        if (user.getStatus() == null ||
                !"ACTIVE".equalsIgnoreCase(
                        user.getStatus()
                )) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }


        // =====================================================
        // SEND LOGGED-IN USER TO JSP
        // =====================================================

        request.setAttribute(
                "loggedInUser",
                user
        );


        // =====================================================
        // APPOINTMENT STATISTICS
        // =====================================================

        int totalAppointments = 0;

        int pendingAppointments = 0;

        int confirmedAppointments = 0;

        int completedAppointments = 0;

        int cancelledAppointments = 0;

        int todayAppointments = 0;


        try {

            /*
             * Get all appointments from database.
             */

            List<Appointment> appointments =
                    appointmentService
                            .getAllAppointments();


            /*
             * Make sure the list is not null.
             */

            if (appointments != null) {

                totalAppointments =
                        appointments.size();


                LocalDate today =
                        LocalDate.now();


                // =================================================
                // COUNT APPOINTMENTS
                // =================================================

                for (Appointment appointment :
                        appointments) {


                    if (appointment == null) {
                        continue;
                    }


                    // -------------------------------------------------
                    // STATUS
                    // -------------------------------------------------

                    String status =
                            appointment.getStatus();


                    if (status != null) {

                        status =
                                status.trim()
                                        .toUpperCase();


                        switch (status) {

                            case "PENDING":

                                pendingAppointments++;

                                break;


                            case "CONFIRMED":

                                confirmedAppointments++;

                                break;


                            case "COMPLETED":

                                completedAppointments++;

                                break;


                            case "CANCELLED":

                                cancelledAppointments++;

                                break;


                            default:

                                break;
                        }
                    }


                    // -------------------------------------------------
                    // TODAY'S APPOINTMENTS
                    // -------------------------------------------------

                    LocalDate appointmentDate =
                            appointment
                                    .getAppointmentDate();


                    if (appointmentDate != null &&
                            appointmentDate.equals(today)) {

                        todayAppointments++;
                    }
                }
            }


        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load dashboard appointment statistics.",
                    e
            );
        }


        // =====================================================
        // SEND STATISTICS TO JSP
        // =====================================================

        request.setAttribute(
                "totalAppointments",
                totalAppointments
        );


        request.setAttribute(
                "pendingAppointments",
                pendingAppointments
        );


        request.setAttribute(
                "confirmedAppointments",
                confirmedAppointments
        );


        request.setAttribute(
                "completedAppointments",
                completedAppointments
        );


        request.setAttribute(
                "cancelledAppointments",
                cancelledAppointments
        );


        request.setAttribute(
                "todayAppointments",
                todayAppointments
        );


        // =====================================================
        // OPEN DASHBOARD JSP
        // =====================================================

        request.getRequestDispatcher(
                "/WEB-INF/views/dashboard.jsp"
        ).forward(
                request,
                response
        );
    }
}