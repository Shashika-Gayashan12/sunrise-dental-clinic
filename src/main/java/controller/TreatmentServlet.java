package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Treatment;
import com.sunrise.dentalclinic.service.TreatmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/treatments")
public class TreatmentServlet extends HttpServlet {

    private final TreatmentService treatmentService =
            new TreatmentService();


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

        showTreatments(request, response);
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

        try {

            addTreatment(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showTreatments(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
        }
    }


    // =========================================================
    // ADD TREATMENT
    // =========================================================

    private void addTreatment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String treatmentName =
                request.getParameter("treatmentName");

        String treatmentCostText =
                request.getParameter("treatmentCost");


        // -----------------------------------------------------
        // Treatment name validation
        // -----------------------------------------------------

        if (treatmentName == null ||
                treatmentName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Treatment name is required."
            );
        }


        // -----------------------------------------------------
        // Treatment cost validation
        // -----------------------------------------------------

        if (treatmentCostText == null ||
                treatmentCostText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Treatment cost is required."
            );
        }


        BigDecimal treatmentCost;

        try {

            treatmentCost =
                    new BigDecimal(
                            treatmentCostText.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Please enter a valid treatment cost."
            );
        }


        // -----------------------------------------------------
        // Negative cost validation
        // -----------------------------------------------------

        if (treatmentCost.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            throw new IllegalArgumentException(
                    "Treatment cost cannot be negative."
            );
        }


        // -----------------------------------------------------
        // Create treatment
        // -----------------------------------------------------

        Treatment treatment =
                new Treatment(
                        treatmentName.trim(),
                        treatmentCost
                );


        // -----------------------------------------------------
        // Save treatment
        // -----------------------------------------------------

        treatmentService.addTreatment(
                treatment
        );


        // -----------------------------------------------------
        // Redirect
        // -----------------------------------------------------

        response.sendRedirect(
                request.getContextPath()
                        + "/treatments"
        );
    }


    // =========================================================
    // SHOW TREATMENTS
    // =========================================================

    private void showTreatments(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Treatment> treatments =
                    treatmentService.getAllTreatments();


            request.setAttribute(
                    "treatments",
                    treatments
            );


            request.getRequestDispatcher(
                    "/WEB-INF/views/treatments.jsp"
            ).forward(
                    request,
                    response
            );


        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load treatments.",
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