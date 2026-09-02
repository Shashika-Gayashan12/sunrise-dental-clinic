package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.AppointmentBillingInfo;
import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.Treatment;
import com.sunrise.dentalclinic.service.AppointmentService;
import com.sunrise.dentalclinic.service.BillService;
import com.sunrise.dentalclinic.service.DentistService;
import com.sunrise.dentalclinic.service.PatientService;
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
import java.time.LocalDate;
import java.util.List;

@WebServlet("/bills")
public class BillServlet extends HttpServlet {

    private final BillService billService =
            new BillService();

    private final AppointmentService appointmentService =
            new AppointmentService();

    private final PatientService patientService =
            new PatientService();

    private final DentistService dentistService =
            new DentistService();

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

        String action =
                request.getParameter("action");

        try {

            // -------------------------------------------------
            // Load appointments by selected date
            // -------------------------------------------------

            if ("loadAppointments".equals(action)) {

                loadAppointmentsByDate(
                        request,
                        response
                );

                return;
            }


            // -------------------------------------------------
            // Load selected appointment
            // -------------------------------------------------

            if ("loadAppointment".equals(action)) {

                loadAppointment(
                        request,
                        response
                );

                return;
            }


            // -------------------------------------------------
            // Default billing page
            // -------------------------------------------------

            showBills(
                    request,
                    response
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showBills(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
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

        if (!isLoggedIn(request)) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");

        try {

            // -------------------------------------------------
            // Create Bill
            // -------------------------------------------------

            if ("createBill".equals(action)) {

                createBill(
                        request,
                        response
                );

                return;
            }


            throw new IllegalArgumentException(
                    "Invalid billing action."
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showBills(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
        }
    }


    // =========================================================
    // LOAD APPOINTMENTS BY DATE
    // =========================================================
    //
    // Example result:
    //
    // APT-1425975 - Kasun Perera
    // APT-1425976 - Nimal Silva
    //
    // =========================================================

    private void loadAppointmentsByDate(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException,
            ServletException,
            IOException {

        String dateText =
                request.getParameter(
                        "appointmentDate"
                );

        if (dateText == null ||
                dateText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select an appointment date."
            );
        }

        LocalDate appointmentDate;

        try {

            appointmentDate =
                    LocalDate.parse(
                            dateText.trim()
                    );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid appointment date."
            );
        }


        // -----------------------------------------------------
        // IMPORTANT
        // Only appointments for the selected date are loaded.
        // Patient name comes from the JOIN query.
        // -----------------------------------------------------

        List<AppointmentBillingInfo> appointments =
                appointmentService
                        .getBillingAppointmentsByDate(
                                appointmentDate
                        );


        request.setAttribute(
                "selectedDate",
                appointmentDate
        );

        request.setAttribute(
                "dateAppointments",
                appointments
        );


        showBills(
                request,
                response
        );
    }


    // =========================================================
    // LOAD SELECTED APPOINTMENT
    // =========================================================

    private void loadAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException,
            ServletException,
            IOException {

        String appointmentIdText =
                request.getParameter(
                        "appointmentId"
                );


        if (appointmentIdText == null ||
                appointmentIdText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select an appointment."
            );
        }


        Long appointmentId;

        try {

            appointmentId =
                    Long.parseLong(
                            appointmentIdText.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid appointment."
            );
        }


        if (appointmentId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid appointment."
            );
        }


        // -----------------------------------------------------
        // Get appointment
        // -----------------------------------------------------

        Appointment appointment =
                appointmentService
                        .getAppointmentById(
                                appointmentId
                        );


        if (appointment == null) {

            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }


        // -----------------------------------------------------
        // Get patient
        // -----------------------------------------------------

        Patient patient =
                patientService.getPatientById(
                        appointment.getPatientId()
                );


        // -----------------------------------------------------
        // Get dentist
        // -----------------------------------------------------

        Dentist dentist =
                dentistService.getDentistById(
                        appointment.getDentistId()
                );


        // -----------------------------------------------------
        // Get treatment
        // -----------------------------------------------------

        Treatment treatment =
                treatmentService.getTreatmentById(
                        appointment.getTreatmentId()
                );


        if (patient == null) {

            throw new IllegalArgumentException(
                    "Patient not found."
            );
        }


        if (dentist == null) {

            throw new IllegalArgumentException(
                    "Dentist not found."
            );
        }


        if (treatment == null) {

            throw new IllegalArgumentException(
                    "Treatment not found."
            );
        }


        // -----------------------------------------------------
        // Send selected appointment details to JSP
        // -----------------------------------------------------

        request.setAttribute(
                "loadedAppointment",
                appointment
        );

        request.setAttribute(
                "loadedPatient",
                patient
        );

        request.setAttribute(
                "loadedDentist",
                dentist
        );

        request.setAttribute(
                "loadedTreatment",
                treatment
        );


        // -----------------------------------------------------
        // Keep selected date
        // -----------------------------------------------------

        String dateText =
                request.getParameter(
                        "appointmentDate"
                );


        if (dateText != null &&
                !dateText.trim().isEmpty()) {

            try {

                request.setAttribute(
                        "selectedDate",
                        LocalDate.parse(
                                dateText.trim()
                        )
                );

            } catch (Exception ignored) {

                // Ignore invalid optional date.
            }
        }


        showBills(
                request,
                response
        );
    }


    // =========================================================
    // CREATE BILL
    // =========================================================

    private void createBill(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException,
            ServletException,
            IOException {

        String appointmentIdText =
                request.getParameter(
                        "appointmentId"
                );


        String consultationFeeText =
                request.getParameter(
                        "consultationFee"
                );


        // -----------------------------------------------------
        // Validate appointment
        // -----------------------------------------------------

        if (appointmentIdText == null ||
                appointmentIdText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select an appointment."
            );
        }


        Long appointmentId;

        try {

            appointmentId =
                    Long.parseLong(
                            appointmentIdText.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid appointment."
            );
        }


        if (appointmentId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid appointment."
            );
        }


        // -----------------------------------------------------
        // Validate consultation fee
        // -----------------------------------------------------

        if (consultationFeeText == null ||
                consultationFeeText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Consultation fee is required."
            );
        }


        BigDecimal consultationFee;

        try {

            consultationFee =
                    new BigDecimal(
                            consultationFeeText.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Please enter a valid consultation fee."
            );
        }


        if (consultationFee.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            throw new IllegalArgumentException(
                    "Consultation fee cannot be negative."
            );
        }


        // -----------------------------------------------------
        // Get appointment from database
        // -----------------------------------------------------

        Appointment appointment =
                appointmentService
                        .getAppointmentById(
                                appointmentId
                        );


        if (appointment == null) {

            throw new IllegalArgumentException(
                    "Appointment not found."
            );
        }


        // -----------------------------------------------------
        // Get treatment from database
        // -----------------------------------------------------

        Treatment treatment =
                treatmentService
                        .getTreatmentById(
                                appointment.getTreatmentId()
                        );


        if (treatment == null) {

            throw new IllegalArgumentException(
                    "Treatment not found."
            );
        }


        // -----------------------------------------------------
        // Treatment cost comes from database
        // -----------------------------------------------------

        BigDecimal treatmentCost =
                treatment.getTreatmentCost();


        if (treatmentCost == null) {

            throw new IllegalArgumentException(
                    "Treatment cost is not available."
            );
        }


        // -----------------------------------------------------
        // Create Bill
        // -----------------------------------------------------

        Bill bill =
                new Bill(
                        appointmentId,
                        consultationFee,
                        treatmentCost
                );


        billService.addBill(
                bill
        );


        // -----------------------------------------------------
        // Redirect after successful bill creation
        // -----------------------------------------------------

        response.sendRedirect(
                request.getContextPath() +
                        "/bills"
        );
    }


    // =========================================================
    // SHOW BILLS
    // =========================================================

    private void showBills(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
            IOException {

        try {

            List<Bill> bills =
                    billService.getAllBills();


            BigDecimal totalRevenue =
                    BigDecimal.ZERO;


            for (Bill bill : bills) {

                if (bill.getTotalAmount() != null) {

                    totalRevenue =
                            totalRevenue.add(
                                    bill.getTotalAmount()
                            );
                }
            }


            request.setAttribute(
                    "bills",
                    bills
            );


            request.setAttribute(
                    "totalRevenue",
                    totalRevenue
            );


            request.getRequestDispatcher(
                    "/WEB-INF/views/bills.jsp"
            ).forward(
                    request,
                    response
            );


        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load bills.",
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
                session.getAttribute(
                        "loggedInUser"
                ) != null;
    }
}