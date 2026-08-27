package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.entity.Treatment;

import com.sunrise.dentalclinic.service.AppointmentService;
import com.sunrise.dentalclinic.service.DentistService;
import com.sunrise.dentalclinic.service.PatientService;
import com.sunrise.dentalclinic.service.TreatmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {

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

        String action = request.getParameter("action");

        try {

            if ("view".equalsIgnoreCase(action)) {
                viewAppointment(request, response);
                return;
            }

            if ("edit".equalsIgnoreCase(action)) {
                editAppointmentForm(request, response);
                return;
            }

            showAppointments(request, response);

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

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {

            // -------------------------------------------------
            // CANCEL
            // -------------------------------------------------

            if ("cancel".equalsIgnoreCase(action)) {

                String idParameter =
                        request.getParameter("id");

                if (idParameter == null ||
                        idParameter.trim().isEmpty()) {

                    throw new IllegalArgumentException(
                            "Appointment ID is required."
                    );
                }

                Long id;

                try {

                    id = Long.parseLong(
                            idParameter.trim()
                    );

                } catch (NumberFormatException e) {

                    throw new IllegalArgumentException(
                            "Invalid appointment ID."
                    );
                }

                appointmentService.cancelAppointment(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/appointments?filter=active"
                );

                return;
            }


            // -------------------------------------------------
            // UPDATE
            // -------------------------------------------------

            if ("update".equalsIgnoreCase(action)) {

                updateAppointment(request, response);
                return;
            }


            // -------------------------------------------------
            // CREATE
            // -------------------------------------------------

            createAppointment(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showAppointments(
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
    // CREATE APPOINTMENT
    // =========================================================

    private void createAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String patientIdText =
                request.getParameter("patientId");

        String dentistIdText =
                request.getParameter("dentistId");

        String treatmentIdText =
                request.getParameter("treatmentId");

        String dateText =
                request.getParameter("appointmentDate");

        String timeText =
                request.getParameter("appointmentTime");


        if (patientIdText == null ||
                patientIdText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select a patient."
            );
        }


        if (dentistIdText == null ||
                dentistIdText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select a dentist."
            );
        }


        if (treatmentIdText == null ||
                treatmentIdText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select a treatment."
            );
        }


        if (dateText == null ||
                dateText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Appointment date is required."
            );
        }


        if (timeText == null ||
                timeText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Appointment time is required."
            );
        }


        Long patientId;
        Long dentistId;
        Long treatmentId;


        try {

            patientId =
                    Long.parseLong(
                            patientIdText.trim()
                    );

            dentistId =
                    Long.parseLong(
                            dentistIdText.trim()
                    );

            treatmentId =
                    Long.parseLong(
                            treatmentIdText.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid patient, dentist, or treatment."
            );
        }


        LocalDate date;
        LocalTime time;


        try {

            date =
                    LocalDate.parse(
                            dateText.trim()
                    );

            time =
                    LocalTime.parse(
                            timeText.trim()
                    );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Please enter a valid appointment date and time."
            );
        }


        Appointment appointment =
                new Appointment(
                        null,
                        date,
                        null,
                        time,
                        "PENDING",
                        dentistId,
                        patientId,
                        treatmentId
                );


        appointmentService.createAppointment(
                appointment
        );


        response.sendRedirect(
                request.getContextPath()
                        + "/appointments?filter=active"
        );
    }


    // =========================================================
    // VIEW APPOINTMENT
    // =========================================================

    private void viewAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String idText =
                request.getParameter("id");


        if (idText == null ||
                idText.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Appointment ID is required."
            );

            return;
        }


        Long id;


        try {

            id =
                    Long.parseLong(
                            idText.trim()
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid appointment ID."
            );

            return;
        }


        Appointment appointment =
                appointmentService.getAppointmentById(id);


        if (appointment == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Appointment not found."
            );

            return;
        }


        Patient patient =
                findPatient(
                        patientService.getAllPatients(),
                        appointment.getPatientId()
                );


        Dentist dentist =
                findDentist(
                        dentistService.getAllDentists(),
                        appointment.getDentistId()
                );


        Treatment treatment =
                findTreatment(
                        treatmentService.getAllTreatments(),
                        appointment.getTreatmentId()
                );


        response.setContentType(
                "text/html;charset=UTF-8"
        );


        String contextPath =
                request.getContextPath();


        String appointmentsUrl =
                contextPath +
                        "/appointments?filter=active";


        String dashboardUrl =
                contextPath +
                        "/dashboard";


        String status =
                appointment.getStatus();


        if (status == null) {
            status = "";
        }


        String statusClass =
                status.toLowerCase();


        StringBuilder html =
                new StringBuilder();


        html.append("""
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width,
      initial-scale=1.0">

<title>
Appointment Details - Sunrise Dental Clinic
</title>

<style>

* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

body {
    font-family: Arial, Helvetica, sans-serif;
    background: #f5f8fa;
    color: #1f2937;
}

.page {
    display: flex;
    min-height: 100vh;
}

.sidebar {
    width: 245px;
    min-width: 245px;
    background: #0f3d56;
    color: white;
    padding: 28px 18px;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
}

.logo {
    padding: 0 12px 30px;
    border-bottom: 1px solid rgba(255,255,255,0.15);
    margin-bottom: 25px;
}

.logo h1 {
    font-size: 20px;
    margin-bottom: 5px;
}

.logo p {
    font-size: 12px;
    color: #b8d9df;
}

.nav-title {
    font-size: 11px;
    color: #91b8c4;
    text-transform: uppercase;
    letter-spacing: 1px;
    padding: 0 12px;
    margin-bottom: 10px;
}

.nav-item {
    display: flex;
    align-items: center;
    width: 100%%;
    height: 42px;
    padding: 0 14px;
    margin-bottom: 5px;
    border-radius: 7px;
    color: #dcecef;
    text-decoration: none;
    font-size: 14px;
    font-weight: 600;
    white-space: nowrap;
    border: 1px solid transparent;
}

.nav-item:hover {
    background: rgba(255,255,255,0.08);
}

.nav-item.active {
    background: #159a9c;
    color: white;
    font-weight: 600;
    border-color: #159a9c;
}

.main {
    margin-left: 245px;
    width: calc(100%% - 245px);
    min-width: 0;
}

.topbar {
    height: 75px;
    background: white;
    border-bottom: 1px solid #e5e7eb;

    display: flex;
    justify-content: space-between;
    align-items: center;

    padding: 0 35px;
}

.page-title h2 {
    color: #0f3d56;
    font-size: 22px;
}

.page-title p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 4px;
}

.user-info {
    font-size: 14px;
    font-weight: bold;
    color: #374151;
}

.content {
    padding: 32px;
    max-width: 1300px;
}

.back {
    display: inline-block;
    margin-bottom: 20px;
    color: #159a9c;
    text-decoration: none;
    font-size: 14px;
    font-weight: bold;
}

.back:hover {
    text-decoration: underline;
}

.card {
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    margin-bottom: 25px;
    overflow: hidden;
}

.card-header {
    padding: 22px 25px;
    border-bottom: 1px solid #e5e7eb;
}

.card-header h3 {
    color: #0f3d56;
    font-size: 19px;
}

.card-header p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 5px;
}

.card-body {
    padding: 25px;
}

.appointment-number {
    background: #e9f8f7;
    color: #0f3d56;
    padding: 16px;
    border-radius: 8px;
    font-weight: bold;
    font-size: 17px;
    margin-bottom: 22px;
}

.details-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
}

.detail {
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 17px;
    background: #fafcfd;
}

.label {
    color: #64748b;
    font-size: 11px;
    text-transform: uppercase;
    letter-spacing: .5px;
    font-weight: bold;
    margin-bottom: 7px;
}

.value {
    color: #1f2937;
    font-size: 15px;
    font-weight: bold;
}

.status {
    display: inline-block;
    padding: 6px 11px;
    border-radius: 20px;
    font-size: 11px;
    font-weight: bold;
}

.pending {
    background: #fff7d6;
    color: #956b00;
}

.confirmed {
    background: #dcfce7;
    color: #166534;
}

.completed {
    background: #e0f2fe;
    color: #0369a1;
}

.cancelled {
    background: #fee2e2;
    color: #991b1b;
}

.buttons {
    display: flex;
    gap: 10px;
    margin-top: 25px;
    flex-wrap: wrap;
}

.btn {
    display: inline-block;
    padding: 11px 17px;
    border-radius: 7px;
    text-decoration: none;
    font-size: 13px;
    font-weight: bold;
}

.btn-back {
    background: #f1f5f9;
    color: #475569;
}

.btn-edit {
    background: #159a9c;
    color: white;
}

.btn-edit:hover {
    background: #117779;
}

@media (max-width: 800px) {

    .sidebar {
        position: static;
        width: 100%%;
        min-width: 0;
    }

    .page {
        display: block;
    }

    .main {
        margin-left: 0;
        width: 100%%;
    }

    .details-grid {
        grid-template-columns: 1fr;
    }

    .topbar {
        padding: 0 20px;
    }

    .content {
        padding: 20px;
    }
}

</style>

</head>

<body>

<div class="page">

<aside class="sidebar">

    <div class="logo">

        <h1>
            Sunrise Dental Clinic
        </h1>

        <p>
            Management System
        </p>

    </div>

    <div class="nav-title">
        Main Menu
    </div>

    <a class="nav-item"
       href="%s">
        Dashboard
    </a>

    <a class="nav-item"
       href="%s">
        Patients
    </a>

    <a class="nav-item"
       href="%s">
        Dentists
    </a>

    <a class="nav-item"
       href="%s">
        Treatments
    </a>

    <a class="nav-item active"
       href="%s">
        Appointments
    </a>

</aside>

<main class="main">

<header class="topbar">

    <div class="page-title">

        <h2>
            Appointment Details
        </h2>

        <p>
            View appointment information
        </p>

    </div>

    <div class="user-info">
        Sunrise Dental Clinic
    </div>

</header>

<div class="content">

<a class="back"
   href="%s">
    ← Back to Appointments
</a>

<div class="card">

    <div class="card-header">

        <h3>
            Appointment Details
        </h3>

        <p>
            Complete information about this appointment
        </p>

    </div>

    <div class="card-body">

        <div class="appointment-number">
            Appointment #%s
        </div>

        <div class="details-grid">
""".formatted(
                dashboardUrl,
                contextPath + "/patients",
                contextPath + "/dentists",
                contextPath + "/treatments",
                appointmentsUrl,
                appointmentsUrl,
                escapeHtml(
                        String.valueOf(
                                appointment.getAppointmentNumber()
                        )
                )
        ));


        addDetail(
                html,
                "Patient",
                patient != null
                        ? patient.getPatientName()
                        : "Patient #" +
                        appointment.getPatientId()
        );


        addDetail(
                html,
                "Dentist",
                dentist != null
                        ? dentist.getDentistName()
                        : "Dentist #" +
                        appointment.getDentistId()
        );


        addDetail(
                html,
                "Specialization",
                dentist != null
                        ? dentist.getSpecialization()
                        : "N/A"
        );


        addDetail(
                html,
                "Treatment",
                treatment != null
                        ? treatment.getTreatmentName()
                        : "Treatment #" +
                        appointment.getTreatmentId()
        );


        addDetail(
                html,
                "Appointment Date",
                String.valueOf(
                        appointment.getAppointmentDate()
                )
        );


        addDetail(
                html,
                "Appointment Time",
                formatTime(
                        appointment.getAppointmentTime()
                )
        );


        html.append(
                "<div class=\"detail\">" +
                        "<div class=\"label\">Status</div>" +
                        "<div class=\"value\">" +
                        "<span class=\"status " +
                        escapeHtml(statusClass) +
                        "\">" +
                        escapeHtml(status) +
                        "</span>" +
                        "</div>" +
                        "</div>"
        );


        html.append("""
        </div>

        <div class="buttons">

            <a class="btn btn-back"
               href="
        """);

        html.append(
                escapeHtml(appointmentsUrl)
        );

        html.append("""
               ">
                ← Back to Appointments
            </a>
        """);


        if (!"CANCELLED".equalsIgnoreCase(status)) {

            html.append(
                    "<a class=\"btn btn-edit\" href=\"" +
                            escapeHtml(contextPath) +
                            "/appointments?action=edit&id=" +
                            appointment.getId() +
                            "\">" +
                            "Edit Appointment" +
                            "</a>"
            );
        }


        html.append("""
        </div>

    </div>

</div>

</div>

</main>

</div>

</body>

</html>
""");


        response.getWriter()
                .write(html.toString());
    }


    // =========================================================
    // EDIT APPOINTMENT FORM
    // =========================================================

    private void editAppointmentForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String idText =
                request.getParameter("id");


        if (idText == null ||
                idText.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Appointment ID is required."
            );

            return;
        }


        Long id;


        try {

            id =
                    Long.parseLong(
                            idText.trim()
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid appointment ID."
            );

            return;
        }


        Appointment appointment =
                appointmentService.getAppointmentById(id);


        if (appointment == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Appointment not found."
            );

            return;
        }


        List<Patient> patients =
                patientService.getAllPatients();

        List<Dentist> dentists =
                dentistService.getAllDentists();

        List<Treatment> treatments =
                treatmentService.getAllTreatments();


        response.setContentType(
                "text/html;charset=UTF-8"
        );


        String contextPath =
                request.getContextPath();


        String appointmentsUrl =
                contextPath +
                        "/appointments?filter=active";


        StringBuilder html =
                new StringBuilder();


        html.append("""
<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width,
      initial-scale=1.0">

<title>
Edit Appointment - Sunrise Dental Clinic
</title>

<style>

* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

body {
    font-family: Arial, Helvetica, sans-serif;
    background: #f5f8fa;
    color: #1f2937;
}

.page {
    display: flex;
    min-height: 100vh;
}

.sidebar {
    width: 245px;
    min-width: 245px;
    background: #0f3d56;
    color: white;
    padding: 28px 18px;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
}

.logo {
    padding: 0 12px 30px;
    border-bottom: 1px solid rgba(255,255,255,0.15);
    margin-bottom: 25px;
}

.logo h1 {
    font-size: 20px;
    margin-bottom: 5px;
}

.logo p {
    font-size: 12px;
    color: #b8d9df;
}

.nav-title {
    font-size: 11px;
    color: #91b8c4;
    text-transform: uppercase;
    letter-spacing: 1px;
    padding: 0 12px;
    margin-bottom: 10px;
}

.nav-item {
    display: flex;
    align-items: center;
    width: 100%%;
    height: 42px;
    padding: 0 14px;
    margin-bottom: 5px;
    border-radius: 7px;
    color: #dcecef;
    text-decoration: none;
    font-size: 14px;
    font-weight: 600;
    white-space: nowrap;
    border: 1px solid transparent;
}

.nav-item:hover {
    background: rgba(255,255,255,0.08);
}

.nav-item.active {
    background: #159a9c;
    color: white;
    font-weight: 600;
    border-color: #159a9c;
}

.main {
    margin-left: 245px;
    width: calc(100%% - 245px);
    min-width: 0;
}

.topbar {
    height: 75px;
    background: white;
    border-bottom: 1px solid #e5e7eb;

    display: flex;
    justify-content: space-between;
    align-items: center;

    padding: 0 35px;
}

.page-title h2 {
    color: #0f3d56;
    font-size: 22px;
}

.page-title p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 4px;
}

.user-info {
    font-size: 14px;
    font-weight: bold;
    color: #374151;
}

.content {
    padding: 32px;
    max-width: 1100px;
}

.back {
    display: inline-block;
    margin-bottom: 20px;
    color: #159a9c;
    text-decoration: none;
    font-size: 14px;
    font-weight: bold;
}

.back:hover {
    text-decoration: underline;
}

.card {
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    overflow: hidden;
}

.card-header {
    padding: 22px 25px;
    border-bottom: 1px solid #e5e7eb;
}

.card-header h3 {
    color: #0f3d56;
    font-size: 19px;
}

.card-header p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 5px;
}

.card-body {
    padding: 25px;
}

.appointment-number {
    background: #e9f8f7;
    color: #0f3d56;
    padding: 15px;
    border-radius: 8px;
    font-weight: bold;
    margin-bottom: 25px;
}

.form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
}

.form-group label {
    display: block;
    font-size: 13px;
    font-weight: bold;
    color: #374151;
    margin-bottom: 7px;
}

.form-group input,
.form-group select {
    width: 100%%;
    height: 44px;
    border: 1px solid #d1d5db;
    border-radius: 7px;
    padding: 0 13px;
    font-size: 14px;
    background: white;
}

.form-group input:focus,
.form-group select:focus {
    outline: none;
    border-color: #159a9c;
    box-shadow:
        0 0 0 3px
        rgba(21,154,156,0.10);
}

.buttons {
    display: flex;
    gap: 10px;
    margin-top: 25px;
}

button,
.btn {
    display: inline-block;
    border: none;
    padding: 11px 20px;
    border-radius: 7px;
    font-size: 13px;
    font-weight: bold;
    text-decoration: none;
    cursor: pointer;
}

.update-btn {
    background: #159a9c;
    color: white;
}

.update-btn:hover {
    background: #117779;
}

.cancel-btn {
    background: #f1f5f9;
    color: #475569;
}

@media (max-width: 800px) {

    .sidebar {
        position: static;
        width: 100%%;
        min-width: 0;
    }

    .page {
        display: block;
    }

    .main {
        margin-left: 0;
        width: 100%%;
    }

    .form-grid {
        grid-template-columns: 1fr;
    }

    .topbar {
        padding: 0 20px;
    }

    .content {
        padding: 20px;
    }
}

</style>

</head>

<body>

<div class="page">

<aside class="sidebar">

    <div class="logo">

        <h1>
            Sunrise Dental Clinic
        </h1>

        <p>
            Management System
        </p>

    </div>

    <div class="nav-title">
        Main Menu
    </div>

    <a class="nav-item"
       href="%s">
        Dashboard
    </a>

    <a class="nav-item"
       href="%s">
        Patients
    </a>

    <a class="nav-item"
       href="%s">
        Dentists
    </a>

    <a class="nav-item"
       href="%s">
        Treatments
    </a>

    <a class="nav-item active"
       href="%s">
        Appointments
    </a>

</aside>

<main class="main">

<header class="topbar">

    <div class="page-title">

        <h2>
            Edit Appointment
        </h2>

        <p>
            Update appointment information
        </p>

    </div>

    <div class="user-info">
        Sunrise Dental Clinic
    </div>

</header>

<div class="content">

<a class="back"
   href="%s">
    ← Back to Appointments
</a>

<div class="card">

<div class="card-header">

    <h3>
        Edit Appointment
    </h3>

    <p>
        Modify the appointment details below
    </p>

</div>

<div class="card-body">

<div class="appointment-number">

    Appointment #%s

</div>

<form method="post"
      action="%s">

<input type="hidden"
       name="action"
       value="update">

<input type="hidden"
       name="id"
       value="%s">

<div class="form-grid">

<div class="form-group">

    <label>
        Patient
    </label>

    <select
        name="patientId"
        required>

        <option value="">
            Select patient
        </option>
""".formatted(
                contextPath + "/dashboard",
                contextPath + "/patients",
                contextPath + "/dentists",
                contextPath + "/treatments",
                appointmentsUrl,
                appointmentsUrl,
                escapeHtml(
                        String.valueOf(
                                appointment.getAppointmentNumber()
                        )
                ),
                contextPath + "/appointments",
                appointment.getId()
        ));


        for (Patient patient : patients) {

            html.append(
                    "<option value=\"" +
                            patient.getId() +
                            "\" " +
                            (
                                    patient.getId().equals(
                                            appointment.getPatientId()
                                    )
                                            ? "selected"
                                            : ""
                            ) +
                            ">" +
                            escapeHtml(
                                    patient.getPatientName()
                            ) +
                            "</option>"
            );
        }


        html.append("""
    </select>

</div>

<div class="form-group">

    <label>
        Dentist
    </label>

    <select
        name="dentistId"
        required>

        <option value="">
            Select dentist
        </option>
""");


        for (Dentist dentist : dentists) {

            html.append(
                    "<option value=\"" +
                            dentist.getId() +
                            "\" " +
                            (
                                    dentist.getId().equals(
                                            appointment.getDentistId()
                                    )
                                            ? "selected"
                                            : ""
                            ) +
                            ">" +
                            escapeHtml(
                                    dentist.getDentistName()
                            ) +
                            " - " +
                            escapeHtml(
                                    dentist.getSpecialization()
                            ) +
                            "</option>"
            );
        }


        html.append("""
    </select>

</div>

<div class="form-group">

    <label>
        Treatment
    </label>

    <select
        name="treatmentId"
        required>

        <option value="">
            Select treatment
        </option>
""");


        for (Treatment treatment : treatments) {

            html.append(
                    "<option value=\"" +
                            treatment.getId() +
                            "\" " +
                            (
                                    treatment.getId().equals(
                                            appointment.getTreatmentId()
                                    )
                                            ? "selected"
                                            : ""
                            ) +
                            ">" +
                            escapeHtml(
                                    treatment.getTreatmentName()
                            ) +
                            "</option>"
            );
        }


        html.append("""
    </select>

</div>

<div class="form-group">

    <label>
        Appointment Date
    </label>

    <input
        type="date"
        name="appointmentDate"
        required
""");


        if (appointment.getAppointmentDate() != null) {

            html.append(
                    " value=\"" +
                            escapeHtml(
                                    appointment
                                            .getAppointmentDate()
                                            .toString()
                            ) +
                            "\""
            );
        }


        html.append("""
    >

</div>

<div class="form-group">

    <label>
        Appointment Time
    </label>

    <input
        type="time"
        name="appointmentTime"
        required
""");


        if (appointment.getAppointmentTime() != null) {

            html.append(
                    " value=\"" +
                            escapeHtml(
                                    formatTime(
                                            appointment
                                                    .getAppointmentTime()
                                    )
                            ) +
                            "\""
            );
        }


        html.append("""
    >

</div>

<div class="form-group">

    <label>
        Status
    </label>

    <select
        name="status"
        required>
""");


        String[] statuses = {
                "PENDING",
                "CONFIRMED",
                "COMPLETED",
                "CANCELLED"
        };


        for (String statusValue : statuses) {

            html.append(
                    "<option value=\"" +
                            statusValue +
                            "\" " +
                            (
                                    statusValue.equalsIgnoreCase(
                                            appointment.getStatus()
                                    )
                                            ? "selected"
                                            : ""
                            ) +
                            ">" +
                            statusValue +
                            "</option>"
            );
        }


        html.append("""
    </select>

</div>

</div>

<div class="buttons">

    <button
        type="submit"
        class="update-btn">

        Update Appointment

    </button>

    <a
        class="btn cancel-btn"
        href="
""");


        html.append(
                escapeHtml(appointmentsUrl)
        );


        html.append("""
        ">

        Cancel

    </a>

</div>

</form>

</div>

</div>

</div>

</main>

</div>

</body>

</html>
""");


        response.getWriter()
                .write(html.toString());
    }


    // =========================================================
    // UPDATE APPOINTMENT
    // =========================================================

    private void updateAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String idText =
                request.getParameter("id");


        if (idText == null ||
                idText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Appointment ID is required."
            );
        }


        Long id;


        try {

            id =
                    Long.parseLong(
                            idText.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid appointment ID."
            );
        }


        Appointment existing =
                appointmentService.getAppointmentById(id);


        if (existing == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Appointment not found."
            );

            return;
        }


        Long patientId;
        Long dentistId;
        Long treatmentId;


        try {

            String patientParameter =
                    request.getParameter("patientId");

            String dentistParameter =
                    request.getParameter("dentistId");

            String treatmentParameter =
                    request.getParameter("treatmentId");


            if (patientParameter == null ||
                    dentistParameter == null ||
                    treatmentParameter == null) {

                throw new IllegalArgumentException();
            }


            patientId =
                    Long.parseLong(
                            patientParameter.trim()
                    );

            dentistId =
                    Long.parseLong(
                            dentistParameter.trim()
                    );

            treatmentId =
                    Long.parseLong(
                            treatmentParameter.trim()
                    );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Please select valid patient, dentist, and treatment."
            );
        }


        String dateParameter =
                request.getParameter(
                        "appointmentDate"
                );


        LocalDate date;


        if (dateParameter == null ||
                dateParameter.trim().isEmpty()) {

            date =
                    existing.getAppointmentDate();

        } else {

            try {

                date =
                        LocalDate.parse(
                                dateParameter.trim()
                        );

            } catch (Exception e) {

                throw new IllegalArgumentException(
                        "Invalid appointment date."
                );
            }
        }


        String timeParameter =
                request.getParameter(
                        "appointmentTime"
                );


        LocalTime time;


        if (timeParameter == null ||
                timeParameter.trim().isEmpty()) {

            time =
                    existing.getAppointmentTime();

        } else {

            try {

                time =
                        LocalTime.parse(
                                timeParameter.trim()
                        );

            } catch (Exception e) {

                throw new IllegalArgumentException(
                        "Invalid appointment time."
                );
            }
        }


        String status =
                request.getParameter("status");


        if (status == null ||
                status.trim().isEmpty()) {

            status =
                    existing.getStatus();

        } else {

            status =
                    status.trim().toUpperCase();
        }


        Appointment appointment =
                new Appointment(
                        id,
                        date,
                        existing.getAppointmentNumber(),
                        time,
                        status,
                        dentistId,
                        patientId,
                        treatmentId
                );


        appointmentService.updateAppointment(
                appointment
        );


        response.sendRedirect(
                request.getContextPath()
                        + "/appointments?filter=active"
        );
    }


    // =========================================================
    // SHOW APPOINTMENTS
    // =========================================================

    private void showAppointments(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Patient> patients =
                    patientService.getAllPatients();

            List<Dentist> dentists =
                    dentistService.getAllDentists();

            List<Treatment> treatments =
                    treatmentService.getAllTreatments();

            List<Appointment> appointments =
                    appointmentService.getAllAppointments();


            String filter =
                    request.getParameter("filter");


            if (filter == null ||
                    filter.trim().isEmpty()) {

                filter = "active";
            }


            filter =
                    filter.toLowerCase();


            // -------------------------------------------------
            // FILTER
            // -------------------------------------------------

            if ("active".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !(
                                        "PENDING".equalsIgnoreCase(
                                                appointment.getStatus()
                                        )
                                                ||
                                                "CONFIRMED".equalsIgnoreCase(
                                                        appointment.getStatus()
                                                )
                                )
                );

            } else if ("pending".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !"PENDING".equalsIgnoreCase(
                                        appointment.getStatus()
                                )
                );

            } else if ("confirmed".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !"CONFIRMED".equalsIgnoreCase(
                                        appointment.getStatus()
                                )
                );

            } else if ("cancelled".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !"CANCELLED".equalsIgnoreCase(
                                        appointment.getStatus()
                                )
                );

            } else if ("completed".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !"COMPLETED".equalsIgnoreCase(
                                        appointment.getStatus()
                                )
                );

            } else if ("all".equals(filter)) {

                // Keep all appointments.

            } else {

                filter = "active";

                appointments.removeIf(
                        appointment ->
                                !(
                                        "PENDING".equalsIgnoreCase(
                                                appointment.getStatus()
                                        )
                                                ||
                                                "CONFIRMED".equalsIgnoreCase(
                                                        appointment.getStatus()
                                                )
                                )
                );
            }


            String error =
                    (String) request.getAttribute(
                            "error"
                    );


            response.setContentType(
                    "text/html;charset=UTF-8"
            );


            String contextPath =
                    request.getContextPath();


            String dashboardUrl =
                    contextPath +
                            "/dashboard";


            String appointmentsUrl =
                    contextPath +
                            "/appointments";


            StringBuilder html =
                    new StringBuilder();


            // =================================================
            // HTML
            // =================================================

            html.append("""
<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width,
      initial-scale=1.0">

<title>
Appointments - Sunrise Dental Clinic
</title>

<style>

* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

body {
    font-family: Arial, Helvetica, sans-serif;
    background: #f5f8fa;
    color: #1f2937;
}

.page {
    display: flex;
    min-height: 100vh;
}

.sidebar {
    width: 245px;
    min-width: 245px;
    background: #0f3d56;
    color: white;
    padding: 28px 18px;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
}

.logo {
    padding: 0 12px 30px;
    border-bottom: 1px solid rgba(255,255,255,0.15);
    margin-bottom: 25px;
}

.logo h1 {
    font-size: 20px;
    margin-bottom: 5px;
}

.logo p {
    font-size: 12px;
    color: #b8d9df;
}

.nav-title {
    font-size: 11px;
    color: #91b8c4;
    text-transform: uppercase;
    letter-spacing: 1px;
    padding: 0 12px;
    margin-bottom: 10px;
}

.nav-item {
    display: flex;
    align-items: center;
    width: 100%%;
    height: 42px;
    padding: 0 14px;
    margin-bottom: 5px;
    border-radius: 7px;
    color: #dcecef;
    text-decoration: none;
    font-size: 14px;
    font-weight: 600;
    white-space: nowrap;
    border: 1px solid transparent;
}

.nav-item:hover {
    background: rgba(255,255,255,0.08);
}

.nav-item.active {
    background: #159a9c;
    color: white;
    font-weight: 600;
    border-color: #159a9c;
}

.main {
    margin-left: 245px;
    width: calc(100%% - 245px);
    min-width: 0;
}

.topbar {
    height: 75px;
    background: white;
    border-bottom: 1px solid #e5e7eb;

    display: flex;
    justify-content: space-between;
    align-items: center;

    padding: 0 35px;
}

.page-title h2 {
    color: #0f3d56;
    font-size: 22px;
}

.page-title p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 4px;
}

.user-info {
    font-size: 14px;
    font-weight: bold;
    color: #374151;
}

.content {
    padding: 32px;
    max-width: 1600px;
}

.back {
    display: inline-block;
    margin-bottom: 20px;
    color: #159a9c;
    text-decoration: none;
    font-size: 14px;
    font-weight: bold;
}

.back:hover {
    text-decoration: underline;
}

.card {
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    margin-bottom: 25px;
    overflow: hidden;
}

.card-header {
    padding: 22px 25px;
    border-bottom: 1px solid #e5e7eb;
}

.card-header h3 {
    color: #0f3d56;
    font-size: 19px;
}

.card-header p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 5px;
}

.card-body {
    padding: 25px;
}

.form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
}

.form-group label {
    display: block;
    font-size: 13px;
    font-weight: bold;
    color: #374151;
    margin-bottom: 7px;
}

.form-group input,
.form-group select {
    width: 100%%;
    height: 44px;
    border: 1px solid #d1d5db;
    border-radius: 7px;
    padding: 0 13px;
    font-size: 14px;
    background: white;
}

.form-group input:focus,
.form-group select:focus {
    outline: none;
    border-color: #159a9c;

    box-shadow:
        0 0 0 3px
        rgba(21,154,156,0.10);
}

.form-actions {
    margin-top: 22px;
    display: flex;
    justify-content: flex-end;
}

.primary-btn {
    border: none;
    background: #159a9c;
    color: white;
    padding: 11px 22px;
    border-radius: 7px;
    font-weight: bold;
    cursor: pointer;
    font-size: 13px;
}

.primary-btn:hover {
    background: #117779;
}

.error {
    background: #fff1f2;
    color: #b91c1c;
    border-left: 4px solid #dc2626;
    padding: 13px 15px;
    border-radius: 7px;
    margin-bottom: 20px;
    font-size: 14px;
}

.filters {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 20px;
}

.filter {
    padding: 9px 15px;
    border-radius: 6px;
    background: #f1f5f9;
    color: #475569;
    text-decoration: none;
    font-size: 13px;
    font-weight: bold;
    border: 1px solid #e2e8f0;
}

.filter:hover {
    background: #e2e8f0;
}

.filter.active {
    background: #159a9c;
    color: white;
    border-color: #159a9c;
}

.table-container {
    overflow-x: auto;
}

table {
    width: 100%%;
    border-collapse: collapse;
    min-width: 1000px;
}

thead th {
    background: #f8fafc;
    color: #64748b;
    font-size: 12px;
    text-transform: uppercase;
    letter-spacing: .4px;
    padding: 14px 12px;
    text-align: left;
    border-bottom: 1px solid #e5e7eb;
    white-space: nowrap;
}

tbody td {
    padding: 16px 12px;
    border-bottom: 1px solid #edf0f2;
    font-size: 13px;
    vertical-align: middle;
}

tbody tr:hover {
    background: #fafcfd;
}

.appointment-number {
    font-weight: bold;
    color: #0f3d56;
}

.person-name {
    font-weight: bold;
    color: #1f2937;
}

.secondary {
    display: block;
    color: #8a94a3;
    font-size: 11px;
    margin-top: 3px;
}

.status {
    display: inline-block;
    padding: 6px 10px;
    border-radius: 20px;
    font-size: 11px;
    font-weight: bold;
}

.pending {
    background: #fff7d6;
    color: #956b00;
}

.confirmed {
    background: #dcfce7;
    color: #166534;
}

.completed {
    background: #e0f2fe;
    color: #0369a1;
}

.cancelled {
    background: #fee2e2;
    color: #991b1b;
}

.actions {
    white-space: nowrap;
}

.action-btn {
    display: inline-block;
    padding: 7px 10px;
    margin-right: 4px;
    border-radius: 5px;
    text-decoration: none;
    font-size: 11px;
    font-weight: bold;
}

.view {
    background: #e0f2fe;
    color: #0369a1;
}

.edit {
    background: #dcfce7;
    color: #166534;
}

.cancel {
    background: #fee2e2;
    color: #991b1b;
    border: none;
    cursor: pointer;
    padding: 7px 10px;
    border-radius: 5px;
    font-size: 11px;
    font-weight: bold;
}

.cancel:hover {
    background: #fecaca;
}

.empty {
    text-align: center;
    padding: 45px !important;
    color: #94a3b8;
}

footer {
    text-align: center;
    color: #94a3b8;
    font-size: 12px;
    padding: 25px;
}

@media (max-width: 900px) {

    .sidebar {
        width: 200px;
        min-width: 200px;
    }

    .main {
        margin-left: 200px;
        width: calc(100%% - 200px);
    }

    .form-grid {
        grid-template-columns: 1fr;
    }

    .content {
        padding: 20px;
    }
}

@media (max-width: 650px) {

    .sidebar {
        position: static;
        width: 100%%;
        min-width: 0;
    }

    .page {
        display: block;
    }

    .main {
        margin-left: 0;
        width: 100%%;
    }

    .topbar {
        padding: 0 20px;
    }

    .content {
        padding: 15px;
    }
}

</style>

</head>

<body>

<div class="page">

<aside class="sidebar">

    <div class="logo">

        <h1>
            Sunrise Dental Clinic
        </h1>

        <p>
            Management System
        </p>

    </div>

    <div class="nav-title">
        Main Menu
    </div>

    <a class="nav-item"
       href="%s">
        Dashboard
    </a>

    <a class="nav-item"
       href="%s">
        Patients
    </a>

    <a class="nav-item"
       href="%s">
        Dentists
    </a>

    <a class="nav-item"
       href="%s">
        Treatments
    </a>

    <a class="nav-item active"
       href="%s">
        Appointments
    </a>

</aside>

<main class="main">

<header class="topbar">

    <div class="page-title">

        <h2>
            Appointments
        </h2>

        <p>
            Schedule and manage patient appointments
        </p>

    </div>

    <div class="user-info">
        Sunrise Dental Clinic
    </div>

</header>

<div class="content">

<a class="back"
   href="%s">
    ← Back to Dashboard
</a>
""".formatted(
                    dashboardUrl,
                    contextPath + "/patients",
                    contextPath + "/dentists",
                    contextPath + "/treatments",
                    appointmentsUrl,
                    dashboardUrl
            ));


            // =================================================
            // ERROR MESSAGE
            // =================================================

            if (error != null &&
                    !error.isBlank()) {

                html.append(
                        "<div class=\"error\">" +
                                escapeHtml(error) +
                                "</div>"
                );
            }


            // =================================================
            // BOOK APPOINTMENT
            // =================================================

            html.append("""
<div class="card">

<div class="card-header">

    <h3>
        Book New Appointment
    </h3>

    <p>
        Create a new appointment for a patient
    </p>

</div>

<div class="card-body">

<form method="post"
      action="%s">

<div class="form-grid">

<div class="form-group">

    <label>
        Patient
    </label>

    <select
        name="patientId"
        required>

        <option value="">
            Select patient
        </option>
""".formatted(
                    appointmentsUrl
            ));


            for (Patient patient : patients) {

                html.append(
                        "<option value=\"" +
                                patient.getId() +
                                "\">" +
                                escapeHtml(
                                        patient.getPatientName()
                                ) +
                                "</option>"
                );
            }


            html.append("""
    </select>

</div>

<div class="form-group">

    <label>
        Dentist
    </label>

    <select
        name="dentistId"
        required>

        <option value="">
            Select dentist
        </option>
""");


            for (Dentist dentist : dentists) {

                html.append(
                        "<option value=\"" +
                                dentist.getId() +
                                "\">" +
                                escapeHtml(
                                        dentist.getDentistName()
                                ) +
                                " - " +
                                escapeHtml(
                                        dentist.getSpecialization()
                                ) +
                                "</option>"
                );
            }


            html.append("""
    </select>

</div>

<div class="form-group">

    <label>
        Treatment
    </label>

    <select
        name="treatmentId"
        required>

        <option value="">
            Select treatment
        </option>
""");


            for (Treatment treatment : treatments) {

                html.append(
                        "<option value=\"" +
                                treatment.getId() +
                                "\">" +
                                escapeHtml(
                                        treatment.getTreatmentName()
                                ) +
                                "</option>"
                );
            }


            html.append("""
    </select>

</div>

<div class="form-group">

    <label>
        Appointment Date
    </label>

    <input
        type="date"
        name="appointmentDate"
        required>

</div>

<div class="form-group">

    <label>
        Appointment Time
    </label>

    <input
        type="time"
        name="appointmentTime"
        required>

</div>

</div>

<div class="form-actions">

    <button
        type="submit"
        class="primary-btn">

        Book Appointment

    </button>

</div>

</form>

</div>

</div>
""");


            // =================================================
            // APPOINTMENTS TABLE
            // =================================================

            html.append("""
<div class="card">

<div class="card-header">

    <h3>
        Appointments
    </h3>

    <p>
        View and manage scheduled appointments
    </p>

</div>

<div class="card-body">

<div class="filters">
""");


            addFilterButton(
                    html,
                    request,
                    "active",
                    "Active",
                    filter
            );


            addFilterButton(
                    html,
                    request,
                    "all",
                    "All",
                    filter
            );


            addFilterButton(
                    html,
                    request,
                    "pending",
                    "Pending",
                    filter
            );


            addFilterButton(
                    html,
                    request,
                    "confirmed",
                    "Confirmed",
                    filter
            );


            addFilterButton(
                    html,
                    request,
                    "completed",
                    "Completed",
                    filter
            );


            addFilterButton(
                    html,
                    request,
                    "cancelled",
                    "Cancelled",
                    filter
            );


            html.append("""
</div>

<div class="table-container">

<table>

<thead>

<tr>

<th>
    Appointment
</th>

<th>
    Patient
</th>

<th>
    Dentist
</th>

<th>
    Treatment
</th>

<th>
    Date
</th>

<th>
    Time
</th>

<th>
    Status
</th>

<th>
    Actions
</th>

</tr>

</thead>

<tbody>
""");


            if (appointments == null ||
                    appointments.isEmpty()) {

                html.append("""
<tr>

<td colspan="8"
    class="empty">

    No appointments found.

</td>

</tr>
""");

            } else {

                for (Appointment appointment :
                        appointments) {


                    Patient patient =
                            findPatient(
                                    patients,
                                    appointment.getPatientId()
                            );


                    Dentist dentist =
                            findDentist(
                                    dentists,
                                    appointment.getDentistId()
                            );


                    Treatment treatment =
                            findTreatment(
                                    treatments,
                                    appointment.getTreatmentId()
                            );


                    String status =
                            appointment.getStatus();


                    if (status == null) {
                        status = "";
                    }


                    String statusClass =
                            status.toLowerCase();


                    html.append("<tr>");


                    // -------------------------------------------------
                    // APPOINTMENT NUMBER
                    // -------------------------------------------------

                    html.append(
                            "<td>" +
                                    "<span class=\"appointment-number\">" +
                                    escapeHtml(
                                            String.valueOf(
                                                    appointment
                                                            .getAppointmentNumber()
                                            )
                                    ) +
                                    "</span>" +
                                    "</td>"
                    );


                    // -------------------------------------------------
                    // PATIENT
                    // -------------------------------------------------

                    html.append("<td>");


                    if (patient != null) {

                        html.append(
                                "<span class=\"person-name\">" +
                                        escapeHtml(
                                                patient.getPatientName()
                                        ) +
                                        "</span>"
                        );

                        html.append(
                                "<span class=\"secondary\">" +
                                        "ID: " +
                                        patient.getId() +
                                        "</span>"
                        );

                    } else {

                        html.append(
                                "<span class=\"person-name\">" +
                                        "Patient #" +
                                        appointment.getPatientId() +
                                        "</span>"
                        );
                    }


                    html.append("</td>");


                    // -------------------------------------------------
                    // DENTIST
                    // -------------------------------------------------

                    html.append("<td>");


                    if (dentist != null) {

                        html.append(
                                "<span class=\"person-name\">" +
                                        escapeHtml(
                                                dentist.getDentistName()
                                        ) +
                                        "</span>"
                        );

                        html.append(
                                "<span class=\"secondary\">" +
                                        escapeHtml(
                                                dentist.getSpecialization()
                                        ) +
                                        "</span>"
                        );

                    } else {

                        html.append(
                                "<span class=\"person-name\">" +
                                        "Dentist #" +
                                        appointment.getDentistId() +
                                        "</span>"
                        );
                    }


                    html.append("</td>");


                    // -------------------------------------------------
                    // TREATMENT
                    // -------------------------------------------------

                    html.append("<td>");


                    if (treatment != null) {

                        html.append(
                                "<span class=\"person-name\">" +
                                        escapeHtml(
                                                treatment
                                                        .getTreatmentName()
                                        ) +
                                        "</span>"
                        );

                    } else {

                        html.append(
                                "<span class=\"person-name\">" +
                                        "Treatment #" +
                                        appointment.getTreatmentId() +
                                        "</span>"
                        );
                    }


                    html.append("</td>");


                    // -------------------------------------------------
                    // DATE
                    // -------------------------------------------------

                    html.append(
                            "<td>" +
                                    escapeHtml(
                                            String.valueOf(
                                                    appointment
                                                            .getAppointmentDate()
                                            )
                                    ) +
                                    "</td>"
                    );


                    // -------------------------------------------------
                    // TIME
                    // -------------------------------------------------

                    html.append(
                            "<td>" +
                                    escapeHtml(
                                            formatTime(
                                                    appointment
                                                            .getAppointmentTime()
                                            )
                                    ) +
                                    "</td>"
                    );


                    // -------------------------------------------------
                    // STATUS
                    // -------------------------------------------------

                    html.append(
                            "<td>" +
                                    "<span class=\"status " +
                                    escapeHtml(statusClass) +
                                    "\">" +
                                    escapeHtml(status) +
                                    "</span>" +
                                    "</td>"
                    );


                    // -------------------------------------------------
                    // ACTIONS
                    // -------------------------------------------------

                    html.append(
                            "<td class=\"actions\">"
                    );


                    // VIEW

                    html.append(
                            "<a class=\"action-btn view\" " +
                                    "href=\"" +
                                    escapeHtml(contextPath) +
                                    "/appointments?action=view&id=" +
                                    appointment.getId() +
                                    "\">" +
                                    "View" +
                                    "</a>"
                    );


                    // EDIT

                    if (!"CANCELLED".equalsIgnoreCase(
                            status)) {

                        html.append(
                                "<a class=\"action-btn edit\" " +
                                        "href=\"" +
                                        escapeHtml(contextPath) +
                                        "/appointments?action=edit&id=" +
                                        appointment.getId() +
                                        "\">" +
                                        "Edit" +
                                        "</a>"
                        );
                    }


                    // CANCEL

                    if ("PENDING".equalsIgnoreCase(status)
                            ||
                            "CONFIRMED".equalsIgnoreCase(status)) {

                        html.append(
                                "<form method=\"post\" " +
                                        "action=\"" +
                                        escapeHtml(appointmentsUrl) +
                                        "\" " +
                                        "style=\"display:inline;\" " +
                                        "onsubmit=\"return confirm(" +
                                        "'Are you sure you want to cancel this appointment?'" +
                                        ");\">"
                        );


                        html.append(
                                "<input type=\"hidden\" " +
                                        "name=\"action\" " +
                                        "value=\"cancel\">"
                        );


                        html.append(
                                "<input type=\"hidden\" " +
                                        "name=\"id\" " +
                                        "value=\"" +
                                        appointment.getId() +
                                        "\">"
                        );


                        html.append(
                                "<button type=\"submit\" " +
                                        "class=\"cancel\">" +
                                        "Cancel" +
                                        "</button>"
                        );


                        html.append("</form>");
                    }


                    html.append("</td>");

                    html.append("</tr>");
                }
            }


            html.append("""
</tbody>

</table>

</div>

</div>

</div>

</div>

<footer>

    © 2026 Sunrise Dental Clinic
    Management System

</footer>

</main>

</div>

</body>

</html>
""");


            response.getWriter()
                    .write(html.toString());


        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load appointments.",
                    e
            );
        }
    }


    // =========================================================
    // FILTER BUTTON
    // =========================================================

    private void addFilterButton(
            StringBuilder html,
            HttpServletRequest request,
            String filterValue,
            String label,
            String currentFilter) {

        String activeClass =
                filterValue.equalsIgnoreCase(
                        currentFilter
                )
                        ? " active"
                        : "";


        html.append(
                "<a class=\"filter" +
                        activeClass +
                        "\" href=\"" +
                        escapeHtml(
                                request.getContextPath()
                        ) +
                        "/appointments?filter=" +
                        escapeHtml(filterValue) +
                        "\">" +
                        escapeHtml(label) +
                        "</a>"
        );
    }


    // =========================================================
    // FIND PATIENT
    // =========================================================

    private Patient findPatient(
            List<Patient> patients,
            Long id) {

        if (patients == null ||
                id == null) {

            return null;
        }


        for (Patient patient : patients) {

            if (id.equals(
                    patient.getId()
            )) {

                return patient;
            }
        }


        return null;
    }


    // =========================================================
    // FIND DENTIST
    // =========================================================

    private Dentist findDentist(
            List<Dentist> dentists,
            Long id) {

        if (dentists == null ||
                id == null) {

            return null;
        }


        for (Dentist dentist : dentists) {

            if (id.equals(
                    dentist.getId()
            )) {

                return dentist;
            }
        }


        return null;
    }


    // =========================================================
    // FIND TREATMENT
    // =========================================================

    private Treatment findTreatment(
            List<Treatment> treatments,
            Long id) {

        if (treatments == null ||
                id == null) {

            return null;
        }


        for (Treatment treatment : treatments) {

            if (id.equals(
                    treatment.getId()
            )) {

                return treatment;
            }
        }


        return null;
    }


    // =========================================================
    // DETAIL HELPER
    // =========================================================

    private void addDetail(
            StringBuilder html,
            String label,
            String value) {

        html.append(
                "<div class=\"detail\">" +
                        "<div class=\"label\">" +
                        escapeHtml(label) +
                        "</div>" +
                        "<div class=\"value\">" +
                        escapeHtml(value) +
                        "</div>" +
                        "</div>"
        );
    }


    // =========================================================
    // FORMAT TIME
    // =========================================================

    private String formatTime(
            LocalTime time) {

        if (time == null) {
            return "";
        }


        String value =
                time.toString();


        if (value.length() >= 5) {

            return value.substring(0, 5);
        }


        return value;
    }


    // =========================================================
    // HTML ESCAPE
    // =========================================================

    private String escapeHtml(
            String text) {

        if (text == null) {
            return "";
        }


        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}