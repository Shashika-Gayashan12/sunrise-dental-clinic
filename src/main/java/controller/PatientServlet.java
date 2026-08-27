package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.service.PatientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {

    private final PatientService patientService =
            new PatientService();

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

        showPatients(request, response);
    }

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

        String patientName =
                request.getParameter("patientName");

        String address =
                request.getParameter("address");

        String contactNumber =
                request.getParameter("contactNumber");

        try {

            Patient patient =
                    new Patient(
                            patientName,
                            address,
                            contactNumber
                    );

            patientService.addPatient(patient);

            response.sendRedirect(
                    request.getContextPath() + "/patients"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showPatients(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to save patient.",
                    e
            );
        }
    }

    private boolean isLoggedIn(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {
            return false;
        }

        User user =
                (User) userObject;

        return "ACTIVE".equalsIgnoreCase(
                user.getStatus()
        );
    }

    private void showPatients(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Patient> patients =
                    patientService.getAllPatients();

            request.setAttribute(
                    "patients",
                    patients
            );

            request.setAttribute(
                    "patientCount",
                    patients == null ? 0 : patients.size()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/patients.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load patients.",
                    e
            );
        }
    }
}