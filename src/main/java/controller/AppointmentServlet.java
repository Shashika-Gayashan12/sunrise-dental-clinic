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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");

        try {

            /*
             * CANCEL APPOINTMENT
             */
            if ("cancel".equalsIgnoreCase(action)) {

                String idParameter =
                        request.getParameter("id");

                if (idParameter == null
                        || idParameter.trim().isEmpty()) {

                    throw new IllegalArgumentException(
                            "Appointment ID is required."
                    );
                }

                Long id =
                        Long.parseLong(
                                idParameter.trim()
                        );

                appointmentService.cancelAppointment(id);

                response.sendRedirect(
                        request.getContextPath()
                                + "/appointments?filter=active"
                );

                return;
            }

            /*
             * UPDATE APPOINTMENT
             */
            if ("update".equalsIgnoreCase(action)) {

                updateAppointment(request, response);

                return;
            }

            /*
             * CREATE NEW APPOINTMENT
             */
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

    /*
     * CREATE APPOINTMENT
     */
    private void createAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        Long patientId =
                Long.parseLong(
                        request.getParameter(
                                "patientId"
                        ).trim()
                );

        Long dentistId =
                Long.parseLong(
                        request.getParameter(
                                "dentistId"
                        ).trim()
                );

        Long treatmentId =
                Long.parseLong(
                        request.getParameter(
                                "treatmentId"
                        ).trim()
                );

        LocalDate date =
                LocalDate.parse(
                        request.getParameter(
                                "appointmentDate"
                        ).trim()
                );

        LocalTime time =
                LocalTime.parse(
                        request.getParameter(
                                "appointmentTime"
                        ).trim()
                );

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

    /*
     * VIEW ONE APPOINTMENT
     */
    private void viewAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        Long id =
                Long.parseLong(
                        request.getParameter("id")
                                .trim()
                );

        Appointment appointment =
                appointmentService.getAppointmentById(id);

        if (appointment == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Appointment not found."
            );

            return;
        }

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        StringBuilder html =
                new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html>
                <head>

                <meta charset="UTF-8">

                <meta name="viewport"
                      content="width=device-width,
                      initial-scale=1.0">

                <title>
                    View Appointment -
                    Sunrise Dental Clinic
                </title>

                <style>

                body {
                    margin: 0;
                    background: #f4f7fb;
                    color: #1f2937;
                    font-family: Arial, sans-serif;
                }

                header {
                    background: #0f3d56;
                    color: white;
                    padding: 20px 40px;
                }

                header h1 {
                    margin: 0 0 5px 0;
                }

                header p {
                    margin: 0;
                    color: #c9e8e5;
                }

                .container {
                    max-width: 800px;
                    margin: auto;
                    padding: 40px;
                }

                .card {
                    background: white;
                    padding: 30px;
                    border-radius: 10px;
                    box-shadow:
                        0 3px 12px
                        rgba(0,0,0,0.08);
                }

                h2 {
                    color: #0f3d56;
                }

                .row {
                    padding: 15px 0;
                    border-bottom:
                        1px solid #e5e7eb;
                }

                .label {
                    font-weight: bold;
                    color: #0f3d56;
                }

                .value {
                    margin-top: 5px;
                }

                .back {
                    display: inline-block;
                    margin-top: 25px;
                    color: #159a9c;
                    font-weight: bold;
                    text-decoration: none;
                }

                </style>

                </head>

                <body>

                <header>
                    <h1>Sunrise Dental Clinic</h1>
                    <p>Appointment Details</p>
                </header>

                <div class="container">

                <div class="card">

                <h2>Appointment Details</h2>
                """);

        html.append("<div class=\"row\">")
                .append("<div class=\"label\">Appointment Number</div>")
                .append("<div class=\"value\">")
                .append(appointment.getAppointmentNumber())
                .append("</div></div>");

        html.append("<div class=\"row\">")
                .append("<div class=\"label\">Patient ID</div>")
                .append("<div class=\"value\">")
                .append(appointment.getPatientId())
                .append("</div></div>");

        html.append("<div class=\"row\">")
                .append("<div class=\"label\">Dentist ID</div>")
                .append("<div class=\"value\">")
                .append(appointment.getDentistId())
                .append("</div></div>");

        html.append("<div class=\"row\">")
                .append("<div class=\"label\">Treatment ID</div>")
                .append("<div class=\"value\">")
                .append(appointment.getTreatmentId())
                .append("</div></div>");

        html.append("<div class=\"row\">")
                .append("<div class=\"label\">Date</div>")
                .append("<div class=\"value\">")
                .append(appointment.getAppointmentDate())
                .append("</div></div>");

        html.append("<div class=\"row\">")
                .append("<div class=\"label\">Time</div>")
                .append("<div class=\"value\">")
                .append(appointment.getAppointmentTime())
                .append("</div></div>");

        html.append("<div class=\"row\">")
                .append("<div class=\"label\">Status</div>")
                .append("<div class=\"value\">")
                .append(appointment.getStatus())
                .append("</div></div>");

        html.append("""
                <a class="back"
                   href="appointments?filter=active">
                   ← Back to Appointments
                </a>

                </div>

                </div>

                </body>
                </html>
                """);

        response.getWriter()
                .write(html.toString());
    }

    /*
     * EDIT APPOINTMENT FORM
     *
     * Existing appointment information is loaded
     * from the database and displayed in the form.
     */
    private void editAppointmentForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        Long id =
                Long.parseLong(
                        request.getParameter("id")
                                .trim()
                );

        /*
         * Get the existing appointment.
         */
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

        StringBuilder html =
                new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html>
                <head>

                <meta charset="UTF-8">

                <meta name="viewport"
                      content="width=device-width,
                      initial-scale=1.0">

                <title>
                    Edit Appointment -
                    Sunrise Dental Clinic
                </title>

                <style>

                body {
                    margin: 0;
                    background: #f4f7fb;
                    color: #1f2937;
                    font-family: Arial, sans-serif;
                }

                header {
                    background: #0f3d56;
                    color: white;
                    padding: 20px 40px;
                }

                header h1 {
                    margin: 0 0 5px 0;
                }

                header p {
                    margin: 0;
                    color: #c9e8e5;
                }

                .container {
                    max-width: 800px;
                    margin: auto;
                    padding: 40px;
                }

                .card {
                    background: white;
                    padding: 30px;
                    border-radius: 10px;
                    box-shadow:
                        0 3px 12px
                        rgba(0,0,0,0.08);
                }

                h2 {
                    color: #0f3d56;
                }

                label {
                    display: block;
                    margin-top: 15px;
                    margin-bottom: 5px;
                    font-weight: bold;
                }

                select,
                input {
                    width: 100%;
                    padding: 11px;
                    border: 1px solid #d1d5db;
                    border-radius: 6px;
                    font-size: 15px;
                    box-sizing: border-box;
                }

                button {
                    margin-top: 20px;
                    padding: 11px 20px;
                    border: none;
                    border-radius: 6px;
                    background: #159a9c;
                    color: white;
                    cursor: pointer;
                    font-size: 15px;
                }

                button:hover {
                    background: #117779;
                }

                .back {
                    display: inline-block;
                    margin-top: 20px;
                    color: #159a9c;
                    font-weight: bold;
                    text-decoration: none;
                }

                </style>

                </head>

                <body>

                <header>
                    <h1>Sunrise Dental Clinic</h1>
                    <p>Edit Appointment</p>
                </header>

                <div class="container">

                <div class="card">

                <h2>Edit Appointment</h2>

                <form method="post"
                      action="appointments">

                <input type="hidden"
                       name="action"
                       value="update">

                <input type="hidden"
                       name="id"
                """);

        html.append(" value=\"")
                .append(appointment.getId())
                .append("\">");

        /*
         * PATIENT
         */
        html.append("""
                <label>Patient</label>

                <select name="patientId" required>

                """);

        for (Patient patient : patients) {

            html.append("<option value=\"")
                    .append(patient.getId());

            if (patient.getId().equals(
                    appointment.getPatientId())) {

                html.append("\" selected>");

            } else {

                html.append("\">");
            }

            html.append(patient.getPatientName())
                    .append("</option>");
        }

        html.append("""
                </select>

                <label>Dentist</label>

                <select name="dentistId" required>

                """);

        /*
         * DENTIST
         */
        for (Dentist dentist : dentists) {

            html.append("<option value=\"")
                    .append(dentist.getId());

            if (dentist.getId().equals(
                    appointment.getDentistId())) {

                html.append("\" selected>");

            } else {

                html.append("\">");
            }

            html.append(dentist.getDentistName())
                    .append(" - ")
                    .append(dentist.getSpecialization())
                    .append("</option>");
        }

        html.append("""
                </select>

                <label>Treatment</label>

                <select name="treatmentId" required>

                """);

        /*
         * TREATMENT
         */
        for (Treatment treatment : treatments) {

            html.append("<option value=\"")
                    .append(treatment.getId());

            if (treatment.getId().equals(
                    appointment.getTreatmentId())) {

                html.append("\" selected>");

            } else {

                html.append("\">");
            }

            html.append(treatment.getId())
                    .append(" - ")
                    .append(treatment.getTreatmentName())
                    .append("</option>");
        }

        html.append("""
                </select>

                <label>Appointment Date</label>

                <input type="date"
                       name="appointmentDate"
                       required
                """);

        /*
         * EXISTING DATE
         *
         * Example:
         * value="2026-08-30"
         */
        if (appointment.getAppointmentDate() != null) {

            html.append(" value=\"")
                    .append(
                            appointment
                                    .getAppointmentDate()
                                    .toString()
                    )
                    .append("\"");
        }

        html.append("""
                >

                <label>Appointment Time</label>

                <input type="time"
                       name="appointmentTime"
                       required
                """);

        /*
         * EXISTING TIME
         *
         * LocalTime can return:
         *
         * 10:30
         *
         * or
         *
         * 10:30:00
         *
         * HTML time input needs HH:mm.
         */
        if (appointment.getAppointmentTime() != null) {

            String timeValue =
                    appointment
                            .getAppointmentTime()
                            .toString();

            if (timeValue.length() >= 5) {

                timeValue =
                        timeValue.substring(0, 5);
            }

            html.append(" value=\"")
                    .append(timeValue)
                    .append("\"");
        }

        html.append("""
                >

                <label>Status</label>

                <select name="status" required>

                """);

        String[] statuses = {
                "PENDING",
                "CONFIRMED",
                "COMPLETED",
                "CANCELLED"
        };

        for (String status : statuses) {

            html.append("<option value=\"")
                    .append(status);

            if (status.equalsIgnoreCase(
                    appointment.getStatus())) {

                html.append("\" selected>");

            } else {

                html.append("\">");
            }

            html.append(status)
                    .append("</option>");
        }

        html.append("""
                </select>

                <button type="submit">
                    Update Appointment
                </button>

                </form>

                <a class="back"
                   href="appointments?filter=active">
                   ← Back to Appointments
                </a>

                </div>

                </div>

                </body>
                </html>
                """);

        response.getWriter()
                .write(html.toString());
    }

    /*
     * UPDATE APPOINTMENT
     *
     * Existing date/time are preserved if the
     * form does not send a new value.
     */
    private void updateAppointment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        Long id =
                Long.parseLong(
                        request.getParameter("id")
                                .trim()
                );

        /*
         * First load the existing appointment.
         *
         * This is important because we want to
         * preserve the existing date/time.
         */
        Appointment existing =
                appointmentService.getAppointmentById(id);

        if (existing == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Appointment not found."
            );

            return;
        }

        /*
         * PATIENT
         */
        Long patientId =
                Long.parseLong(
                        request.getParameter("patientId")
                                .trim()
                );

        /*
         * DENTIST
         */
        Long dentistId =
                Long.parseLong(
                        request.getParameter("dentistId")
                                .trim()
                );

        /*
         * TREATMENT
         */
        Long treatmentId =
                Long.parseLong(
                        request.getParameter("treatmentId")
                                .trim()
                );

        /*
         * DATE
         *
         * Use the new date if supplied.
         *
         * Otherwise keep the existing date.
         */
        String dateParameter =
                request.getParameter(
                        "appointmentDate"
                );

        LocalDate date;

        if (dateParameter == null
                || dateParameter.trim().isEmpty()) {

            date =
                    existing.getAppointmentDate();

        } else {

            date =
                    LocalDate.parse(
                            dateParameter.trim()
                    );
        }

        /*
         * TIME
         *
         * Use the new time if supplied.
         *
         * Otherwise keep the existing time.
         */
        String timeParameter =
                request.getParameter(
                        "appointmentTime"
                );

        LocalTime time;

        if (timeParameter == null
                || timeParameter.trim().isEmpty()) {

            time =
                    existing.getAppointmentTime();

        } else {

            time =
                    LocalTime.parse(
                            timeParameter.trim()
                    );
        }

        /*
         * STATUS
         */
        String status =
                request.getParameter("status");

        if (status == null
                || status.trim().isEmpty()) {

            status =
                    existing.getStatus();

        } else {

            status =
                    status.trim().toUpperCase();
        }

        /*
         * CREATE UPDATED APPOINTMENT
         *
         * Appointment number stays the same.
         */
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

    /*
     * SHOW APPOINTMENTS
     */
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

            if (filter == null
                    || filter.trim().isEmpty()) {

                filter = "active";
            }

            filter = filter.toLowerCase();

            if ("active".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !("PENDING".equalsIgnoreCase(
                                        appointment.getStatus())
                                        ||
                                        "CONFIRMED".equalsIgnoreCase(
                                                appointment.getStatus()))
                );

            } else if ("pending".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !"PENDING".equalsIgnoreCase(
                                        appointment.getStatus())
                );

            } else if ("confirmed".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !"CONFIRMED".equalsIgnoreCase(
                                        appointment.getStatus())
                );

            } else if ("cancelled".equals(filter)) {

                appointments.removeIf(
                        appointment ->
                                !"CANCELLED".equalsIgnoreCase(
                                        appointment.getStatus())
                );

            } else if ("all".equals(filter)) {

                // Keep all appointments.

            } else {

                filter = "active";

                appointments.removeIf(
                        appointment ->
                                !("PENDING".equalsIgnoreCase(
                                        appointment.getStatus())
                                        ||
                                        "CONFIRMED".equalsIgnoreCase(
                                                appointment.getStatus()))
                );
            }

            String error =
                    (String) request.getAttribute("error");

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            StringBuilder html =
                    new StringBuilder();

            html.append("""
                    <!DOCTYPE html>
                    <html>
                    <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width,
                          initial-scale=1.0">

                    <title>
                        Appointments -
                        Sunrise Dental Clinic
                    </title>

                    <style>

                    body {
                        margin: 0;
                        background: #f4f7fb;
                        color: #1f2937;
                        font-family: Arial, sans-serif;
                    }

                    header {
                        background: #0f3d56;
                        color: white;
                        padding: 20px 40px;
                    }

                    header h1 {
                        margin: 0 0 5px 0;
                    }

                    header p {
                        margin: 0;
                        color: #c9e8e5;
                    }

                    .container {
                        max-width: 1400px;
                        margin: auto;
                        padding: 40px;
                    }

                    .card {
                        background: white;
                        padding: 25px;
                        margin-bottom: 25px;
                        border-radius: 10px;
                        box-shadow:
                            0 3px 12px
                            rgba(0,0,0,0.08);
                    }

                    h2 {
                        color: #0f3d56;
                    }

                    label {
                        display: block;
                        margin-top: 15px;
                        margin-bottom: 5px;
                        font-weight: bold;
                    }

                    select,
                    input {
                        width: 100%;
                        padding: 11px;
                        border: 1px solid #d1d5db;
                        border-radius: 6px;
                        font-size: 15px;
                        box-sizing: border-box;
                    }

                    button {
                        margin-top: 20px;
                        padding: 11px 20px;
                        border: none;
                        border-radius: 6px;
                        background: #159a9c;
                        color: white;
                        cursor: pointer;
                        font-size: 15px;
                    }

                    button:hover {
                        background: #117779;
                    }

                    .error {
                        background: #fee2e2;
                        color: #991b1b;
                        padding: 12px;
                        border-radius: 6px;
                        margin-bottom: 20px;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
                    }

                    th,
                    td {
                        padding: 12px;
                        border-bottom: 1px solid #ddd;
                        text-align: left;
                    }

                    th {
                        background: #0f3d56;
                        color: white;
                    }

                    .status {
                        font-weight: bold;
                    }

                    .pending {
                        color: #b45309;
                    }

                    .confirmed {
                        color: #166534;
                    }

                    .cancelled {
                        color: #991b1b;
                    }

                    .completed {
                        color: #0369a1;
                    }

                    .actions a {
                        display: inline-block;
                        padding: 7px 10px;
                        margin-right: 5px;
                        border-radius: 5px;
                        text-decoration: none;
                        font-size: 13px;
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
                        font-size: 13px;
                    }

                    .filters {
                        display: flex;
                        gap: 10px;
                        flex-wrap: wrap;
                        margin-bottom: 20px;
                    }

                    .filter {
                        display: inline-block;
                        padding: 10px 16px;
                        border-radius: 6px;
                        background: #e5e7eb;
                        color: #374151;
                        text-decoration: none;
                        font-weight: bold;
                    }

                    .filter:hover {
                        background: #d1d5db;
                    }

                    .filter.active {
                        background: #159a9c;
                        color: white;
                    }

                    .empty {
                        text-align: center;
                        padding: 30px;
                        color: #6b7280;
                    }

                    .back {
                        display: inline-block;
                        margin-bottom: 20px;
                        color: #159a9c;
                        font-weight: bold;
                        text-decoration: none;
                    }

                    </style>

                    </head>

                    <body>

                    <header>

                        <h1>
                            Sunrise Dental Clinic
                        </h1>

                        <p>
                            Appointment Management
                        </p>

                    </header>

                    <div class="container">

                    <a class="back"
                       href="/sunrise-dental-clinic/">
                       ← Back to Dashboard
                    </a>
                    """);

            if (error != null) {

                html.append("""
                        <div class="error">
                        """);

                html.append(error);

                html.append("""
                        </div>
                        """);
            }

            /*
             * BOOK APPOINTMENT
             */
            html.append("""
                    <div class="card">

                    <h2>Book Appointment</h2>

                    <form method="post"
                          action="appointments">

                    <label>Patient</label>

                    <select name="patientId"
                            required>

                    <option value="">
                        Select patient
                    </option>
                    """);

            for (Patient patient : patients) {

                html.append("<option value=\"")
                        .append(patient.getId())
                        .append("\">")
                        .append(patient.getPatientName())
                        .append("</option>");
            }

            html.append("""
                    </select>

                    <label>Dentist</label>

                    <select name="dentistId"
                            required>

                    <option value="">
                        Select dentist
                    </option>
                    """);

            for (Dentist dentist : dentists) {

                html.append("<option value=\"")
                        .append(dentist.getId())
                        .append("\">")
                        .append(dentist.getDentistName())
                        .append(" - ")
                        .append(dentist.getSpecialization())
                        .append("</option>");
            }

            html.append("""
                    </select>

                    <label>Treatment</label>

                    <select name="treatmentId"
                            required>

                    <option value="">
                        Select treatment
                    </option>
                    """);

            for (Treatment treatment : treatments) {

                html.append("<option value=\"")
                        .append(treatment.getId())
                        .append("\">")
                        .append(treatment.getId())
                        .append(" - ")
                        .append(treatment.getTreatmentName())
                        .append("</option>");
            }

            html.append("""
                    </select>

                    <label>Appointment Date</label>

                    <input type="date"
                           name="appointmentDate"
                           required>

                    <label>Appointment Time</label>

                    <input type="time"
                           name="appointmentTime"
                           required>

                    <button type="submit">
                        Book Appointment
                    </button>

                    </form>

                    </div>

                    <div class="card">

                    <h2>Appointments</h2>

                    <div class="filters">

                    """);

            /*
             * FILTER BUTTONS
             */

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
                    "cancelled",
                    "Cancelled",
                    filter
            );

            html.append("""
                    </div>

                    <table>

                    <tr>
                        <th>Number</th>
                        <th>Patient ID</th>
                        <th>Dentist ID</th>
                        <th>Treatment ID</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    """);

            if (appointments.isEmpty()) {

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

                    html.append("<tr>");

                    html.append("<td>")
                            .append(
                                    appointment.getAppointmentNumber()
                            )
                            .append("</td>");

                    html.append("<td>")
                            .append(
                                    appointment.getPatientId()
                            )
                            .append("</td>");

                    html.append("<td>")
                            .append(
                                    appointment.getDentistId()
                            )
                            .append("</td>");

                    html.append("<td>")
                            .append(
                                    appointment.getTreatmentId()
                            )
                            .append("</td>");

                    html.append("<td>")
                            .append(
                                    appointment.getAppointmentDate()
                            )
                            .append("</td>");

                    html.append("<td>")
                            .append(
                                    appointment.getAppointmentTime()
                            )
                            .append("</td>");

                    String status =
                            appointment.getStatus();

                    String statusClass =
                            status == null
                                    ? ""
                                    : status.toLowerCase();

                    html.append(
                                    "<td class=\"status "
                                            + statusClass
                                            + "\">"
                            )
                            .append(status)
                            .append("</td>");

                    html.append("""
                            <td class="actions">
                            """);

                    /*
                     * VIEW
                     */
                    html.append(
                                    "<a class=\"view\" href=\""
                            )
                            .append(
                                    request.getContextPath()
                            )
                            .append(
                                    "/appointments?action=view&id="
                            )
                            .append(
                                    appointment.getId()
                            )
                            .append("\">View</a>");

                    /*
                     * EDIT
                     *
                     * Don't show Edit for cancelled
                     * appointments.
                     */
                    if (!"CANCELLED".equalsIgnoreCase(
                            status)) {

                        html.append(
                                        "<a class=\"edit\" href=\""
                                )
                                .append(
                                        request.getContextPath()
                                )
                                .append(
                                        "/appointments?action=edit&id="
                                )
                                .append(
                                        appointment.getId()
                                )
                                .append(
                                        "\">Edit</a>"
                                );
                    }

                    /*
                     * CANCEL
                     *
                     * Only PENDING and CONFIRMED
                     * appointments can be cancelled.
                     */
                    if ("PENDING".equalsIgnoreCase(status)
                            ||
                            "CONFIRMED".equalsIgnoreCase(status)) {

                        html.append("""
                                <form method="post"
                                      action="appointments"
                                      style="display:inline;"
                                      onsubmit="return confirm(
                                      'Are you sure you want to cancel this appointment?'
                                      );">

                                <input type="hidden"
                                       name="action"
                                       value="cancel">

                                <input type="hidden"
                                       name="id"
                                """);

                        html.append(" value=\"")
                                .append(
                                        appointment.getId()
                                )
                                .append("\">");

                        html.append("""
                                <button type="submit"
                                        class="cancel">
                                    Cancel
                                </button>

                                </form>
                                """);
                    }

                    html.append("""
                            </td>
                            </tr>
                            """);
                }
            }

            html.append("""
                    </table>

                    </div>

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

    /*
     * ADD APPOINTMENT FILTER BUTTON
     */
    private void addFilterButton(
            StringBuilder html,
            HttpServletRequest request,
            String filterValue,
            String label,
            String currentFilter) {

        String activeClass =
                filterValue.equalsIgnoreCase(currentFilter)
                        ? " active"
                        : "";

        html.append(
                        "<a class=\"filter"
                                + activeClass
                                + "\" href=\""
                )
                .append(
                        request.getContextPath()
                )
                .append(
                        "/appointments?filter="
                )
                .append(filterValue)
                .append("\">")
                .append(label)
                .append("</a>");
    }
}