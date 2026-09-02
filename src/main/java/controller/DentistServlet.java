package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.DentistAvailability;
import com.sunrise.dentalclinic.service.DentistAvailabilityService;
import com.sunrise.dentalclinic.service.DentistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dentists")
public class DentistServlet extends HttpServlet {

    private final DentistService dentistService =
            new DentistService();

    private final DentistAvailabilityService availabilityService =
            new DentistAvailabilityService();


    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        showDentists(request, response);
    }


    // =========================================================
    // POST
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {

            if ("addDentist".equals(action)) {

                addDentist(request, response);

            } else if ("addAvailability".equals(action)) {

                addAvailability(request, response);

            } else {

                response.sendRedirect(
                        request.getContextPath() + "/dentists"
                );
            }

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showDentists(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Database error while processing dentist request.",
                    e
            );
        }
    }


    // =========================================================
    // ADD DENTIST
    // =========================================================

    private void addDentist(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String dentistName =
                request.getParameter("dentistName");

        String specialization =
                request.getParameter("specialization");

        String contactNumber =
                request.getParameter("contactNumber");


        // Validation
        if (dentistName == null ||
                dentistName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Dentist name is required."
            );
        }


        if (specialization == null ||
                specialization.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Specialization is required."
            );
        }


        if (contactNumber == null ||
                contactNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Contact number is required."
            );
        }


        Dentist dentist = new Dentist(
                dentistName.trim(),
                specialization.trim(),
                contactNumber.trim()
        );


        dentistService.addDentist(dentist);


        response.sendRedirect(
                request.getContextPath() + "/dentists"
        );
    }


    // =========================================================
    // ADD AVAILABILITY
    // =========================================================

    private void addAvailability(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String dentistIdParam =
                request.getParameter("dentistId");

        String dayOfWeek =
                request.getParameter("dayOfWeek");

        String startTimeParam =
                request.getParameter("startTime");

        String endTimeParam =
                request.getParameter("endTime");


        // Dentist ID validation
        if (dentistIdParam == null ||
                dentistIdParam.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Dentist ID is required."
            );
        }


        long dentistId;

        try {

            dentistId =
                    Long.parseLong(
                            dentistIdParam.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid dentist ID."
            );
        }


        // Day validation
        if (dayOfWeek == null ||
                dayOfWeek.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select a day."
            );
        }


        // Time validation
        if (startTimeParam == null ||
                startTimeParam.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Start time is required."
            );
        }


        if (endTimeParam == null ||
                endTimeParam.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "End time is required."
            );
        }


        LocalTime startTime;

        LocalTime endTime;

        try {

            startTime =
                    LocalTime.parse(
                            startTimeParam.trim()
                    );

            endTime =
                    LocalTime.parse(
                            endTimeParam.trim()
                    );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid time format."
            );
        }


        // End time must be after start time
        if (!endTime.isAfter(startTime)) {

            throw new IllegalArgumentException(
                    "End time must be after start time."
            );
        }


        DentistAvailability availability =
                new DentistAvailability(
                        dentistId,
                        dayOfWeek.trim(),
                        null,
                        startTime,
                        endTime
                );


        availabilityService.addAvailability(
                availability
        );


        response.sendRedirect(
                request.getContextPath() + "/dentists"
        );
    }


    // =========================================================
    // SHOW DENTISTS
    // =========================================================

    private void showDentists(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            // -------------------------------------------------
            // Get all dentists
            // -------------------------------------------------

            List<Dentist> dentists =
                    dentistService.getAllDentists();


            // -------------------------------------------------
            // Get availability for each dentist
            // -------------------------------------------------

            Map<Long, List<DentistAvailability>>
                    availabilityMap =
                    new HashMap<>();


            for (Dentist dentist : dentists) {

                List<DentistAvailability> schedules =
                        availabilityService.getByDentistId(
                                dentist.getId()
                        );

                availabilityMap.put(
                        dentist.getId(),
                        schedules
                );
            }


            // -------------------------------------------------
            // Send data to JSP
            // -------------------------------------------------

            request.setAttribute(
                    "dentists",
                    dentists
            );


            request.setAttribute(
                    "availabilityMap",
                    availabilityMap
            );


            // -------------------------------------------------
            // Open JSP
            // -------------------------------------------------

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentists.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load dentists.",
                    e
            );
        }
    }


    // =========================================================
    // LOGIN CHECK
    // =========================================================

    private boolean isLoggedIn(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        return session != null &&
                session.getAttribute("loggedInUser") != null;
    }
}