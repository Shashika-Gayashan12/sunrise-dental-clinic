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
import java.util.List;

@WebServlet("/dentist-bookings")
public class DentistBookingsServlet extends HttpServlet {

    private final AppointmentService appointmentService =
            new AppointmentService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        User user =
                (User) userObject;

        if (!"DENTIST".equalsIgnoreCase(
                user.getRole())) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        if (!"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );

            return;
        }

        Long dentistId =
                user.getDentistId();

        if (dentistId == null ||
                dentistId <= 0) {

            request.setAttribute(
                    "error",
                    "Your account is not linked to a dentist profile."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-bookings.jsp"
            ).forward(request, response);

            return;
        }

        try {

            List<Appointment> appointments =
                    appointmentService
                            .getAppointmentsByDentistId(
                                    dentistId
                            );

            request.setAttribute(
                    "appointments",
                    appointments
            );

            request.setAttribute(
                    "loggedInUser",
                    user
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-bookings.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to load your bookings."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-bookings.jsp"
            ).forward(request, response);
        }
    }

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