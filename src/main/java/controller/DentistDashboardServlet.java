package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.service.AppointmentService;
import com.sunrise.dentalclinic.service.DentistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dentist-dashboard")
public class DentistDashboardServlet extends HttpServlet {


    private final AppointmentService appointmentService =
            new AppointmentService();

    private final DentistService dentistService =
            new DentistService();


// =========================================================
// GET
// =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // -----------------------------------------------------
        // Check session
        // -----------------------------------------------------

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login?type=dentist"
            );
            return;
        }

        Object loggedInUserObject =
                session.getAttribute("loggedInUser");

        if (!(loggedInUserObject instanceof User)) {

            response.sendRedirect(
                    request.getContextPath() + "/login?type=dentist"
            );

            return;
        }

        User loggedInUser =
                (User) loggedInUserObject;


        // -----------------------------------------------------
        // Check dentist role
        // -----------------------------------------------------

        if (!"DENTIST".equalsIgnoreCase(
                loggedInUser.getRole())) {

            response.sendRedirect(
                    request.getContextPath() + "/login?type=dentist"
            );

            return;
        }


        // -----------------------------------------------------
        // Check account status
        // -----------------------------------------------------

        if (!"ACTIVE".equalsIgnoreCase(
                loggedInUser.getStatus())) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath() + "/login?type=dentist"
            );

            return;
        }


        // -----------------------------------------------------
        // Get dentist ID
        // -----------------------------------------------------

        Long dentistId =
                loggedInUser.getDentistId();

        if (dentistId == null ||
                dentistId <= 0) {

            request.setAttribute(
                    "error",
                    "Your account is not linked to a dentist profile."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-dashboard.jsp"
            ).forward(request, response);

            return;
        }


        try {

            // -------------------------------------------------
            // Get dentist profile
            // -------------------------------------------------

            Dentist dentist =
                    dentistService.getDentistById(
                            dentistId
                    );

            if (dentist == null) {

                request.setAttribute(
                        "error",
                        "Dentist profile not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/dentist-dashboard.jsp"
                ).forward(request, response);

                return;
            }


            // -------------------------------------------------
            // Get ONLY this dentist's appointments
            // -------------------------------------------------

            List<Appointment> allAppointments =
                    appointmentService
                            .getAppointmentsByDentistId(
                                    dentistId
                            );


            // -------------------------------------------------
            // Current date
            // -------------------------------------------------

            LocalDate today =
                    LocalDate.now();


            // -------------------------------------------------
            // Statistics
            // -------------------------------------------------

            int totalAppointments =
                    allAppointments.size();

            int todayAppointments = 0;

            int pendingAppointments = 0;

            int confirmedAppointments = 0;

            int completedAppointments = 0;

            int cancelledAppointments = 0;


            // -------------------------------------------------
            // Today's appointments
            // -------------------------------------------------

            List<Appointment> todayList =
                    new ArrayList<>();


            // -------------------------------------------------
            // Upcoming appointments
            // -------------------------------------------------

            List<Appointment> upcomingAppointments =
                    new ArrayList<>();


            for (Appointment appointment :
                    allAppointments) {

                if (appointment == null) {
                    continue;
                }


                // ---------------------------------------------
                // Status counts
                // ---------------------------------------------

                String status =
                        appointment.getStatus();

                if ("PENDING".equalsIgnoreCase(
                        status)) {

                    pendingAppointments++;

                } else if ("CONFIRMED".equalsIgnoreCase(
                        status)) {

                    confirmedAppointments++;

                } else if ("COMPLETED".equalsIgnoreCase(
                        status)) {

                    completedAppointments++;

                } else if ("CANCELLED".equalsIgnoreCase(
                        status)) {

                    cancelledAppointments++;
                }


                // ---------------------------------------------
                // Today's appointments
                // ---------------------------------------------

                if (appointment.getAppointmentDate() != null &&
                        appointment.getAppointmentDate()
                                .equals(today)) {

                    todayAppointments++;

                    if (!"CANCELLED".equalsIgnoreCase(
                            status) &&
                            !"COMPLETED".equalsIgnoreCase(
                                    status)) {

                        todayList.add(
                                appointment
                        );
                    }
                }


                // ---------------------------------------------
                // Upcoming appointments
                // ---------------------------------------------

                if (appointment.getAppointmentDate() != null &&
                        appointment.getAppointmentDate()
                                .isAfter(today) &&
                        !"CANCELLED".equalsIgnoreCase(
                                status) &&
                        !"COMPLETED".equalsIgnoreCase(
                                status)) {

                    upcomingAppointments.add(
                            appointment
                    );
                }
            }


            // -------------------------------------------------
            // Limit upcoming appointments
            // -------------------------------------------------

            if (upcomingAppointments.size() > 10) {

                upcomingAppointments =
                        new ArrayList<>(
                                upcomingAppointments.subList(
                                        0,
                                        10
                                )
                        );
            }


            // -------------------------------------------------
            // Request attributes
            // -------------------------------------------------

            request.setAttribute(
                    "dentist",
                    dentist
            );

            request.setAttribute(
                    "appointments",
                    allAppointments
            );

            request.setAttribute(
                    "todayAppointmentsList",
                    todayList
            );

            request.setAttribute(
                    "upcomingAppointments",
                    upcomingAppointments
            );

            request.setAttribute(
                    "totalAppointments",
                    totalAppointments
            );

            request.setAttribute(
                    "todayAppointments",
                    todayAppointments
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
                    "today",
                    today
            );


            // -------------------------------------------------
            // Forward to JSP
            // -------------------------------------------------

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-dashboard.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to load dentist dashboard."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-dashboard.jsp"
            ).forward(request, response);
        }
    }


// =========================================================
// POST
// =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(
                request,
                response
        );
    }


}
