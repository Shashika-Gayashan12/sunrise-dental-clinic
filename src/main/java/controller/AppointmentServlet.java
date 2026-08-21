package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Appointment;
import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.Patient;

import com.sunrise.dentalclinic.service.AppointmentService;
import com.sunrise.dentalclinic.service.DentistService;
import com.sunrise.dentalclinic.service.PatientService;

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

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        showAppointments(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

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
                            + "/appointments"
            );

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

    private void showAppointments(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Patient> patients =
                    patientService.getAllPatients();

            List<Dentist> dentists =
                    dentistService.getAllDentists();

            List<Appointment> appointments =
                    appointmentService.getAllAppointments();

            String error =
                    (String) request.getAttribute(
                            "error"
                    );

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
                        max-width: 1200px;
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

                    .pending {
                        color: #b45309;
                        font-weight: bold;
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

            html.append("""
                    <div class="card">

                    <h2>Book Appointment</h2>

                    <form method="post"
                          action="appointments">

                    <label>
                        Patient
                    </label>

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

                    <label>
                        Dentist
                    </label>

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

                    <label>
                        Treatment ID
                    </label>

                    <input type="number"
                           name="treatmentId"
                           required
                           min="1"
                           placeholder="Enter treatment ID">

                    <label>
                        Appointment Date
                    </label>

                    <input type="date"
                           name="appointmentDate"
                           required>

                    <label>
                        Appointment Time
                    </label>

                    <input type="time"
                           name="appointmentTime"
                           required>

                    <button type="submit">
                        Book Appointment
                    </button>

                    </form>

                    </div>
                    """);

            html.append("""
                    <div class="card">

                    <h2>Appointments</h2>

                    <table>

                    <tr>
                        <th>Number</th>
                        <th>Patient ID</th>
                        <th>Dentist ID</th>
                        <th>Treatment ID</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Status</th>
                    </tr>
                    """);

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

                html.append("<td class=\"pending\">")
                        .append(
                                appointment.getStatus()
                        )
                        .append("</td>");

                html.append("</tr>");
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
}