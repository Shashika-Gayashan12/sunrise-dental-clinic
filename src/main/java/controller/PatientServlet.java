package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Patient;
import com.sunrise.dentalclinic.service.PatientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {

    private final PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        showPatients(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String patientName = request.getParameter("patientName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");

        Patient patient = new Patient(
                patientName,
                address,
                contactNumber
        );

        try {
            patientService.addPatient(patient);

            response.sendRedirect(
                    request.getContextPath() + "/patients"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());
            showPatients(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to save patient",
                    e
            );
        }
    }

    private void showPatients(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Patient> patients =
                    patientService.getAllPatients();

            response.setContentType("text/html;charset=UTF-8");

            String error = (String) request.getAttribute("error");

            StringBuilder html = new StringBuilder();

            html.append("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>Patients - Sunrise Dental Clinic</title>

                        <style>
                            * {
                                box-sizing: border-box;
                                font-family: Arial, sans-serif;
                            }

                            body {
                                margin: 0;
                                background: #f4f7fb;
                                color: #1f2937;
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
                                padding: 40px;
                                max-width: 1100px;
                                margin: auto;
                            }

                            .card {
                                background: white;
                                padding: 25px;
                                margin-bottom: 25px;
                                border-radius: 10px;
                                box-shadow: 0 3px 12px rgba(0,0,0,0.08);
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
                                font-size: 15px;
                                cursor: pointer;
                            }

                            button:hover {
                                background: #117779;
                            }

                            .error {
                                background: #fee2e2;
                                color: #991b1b;
                                padding: 12px;
                                border-radius: 6px;
                                margin-bottom: 15px;
                            }

                            table {
                                width: 100%;
                                border-collapse: collapse;
                                margin-top: 15px;
                            }

                            th, td {
                                padding: 12px;
                                border-bottom: 1px solid #ddd;
                                text-align: left;
                            }

                            th {
                                background: #0f3d56;
                                color: white;
                            }

                            .back {
                                display: inline-block;
                                margin-bottom: 20px;
                                color: #159a9c;
                                text-decoration: none;
                                font-weight: bold;
                            }
                        </style>
                    </head>

                    <body>

                    <header>
                        <h1>Sunrise Dental Clinic</h1>
                        <p>Patient Management System</p>
                    </header>

                    <div class="container">

                        <a class="back" href="/sunrise-dental-clinic/">
                            ← Back to Dashboard
                        </a>
                    """);

            if (error != null) {
                html.append("<div class='error'>")
                        .append(error)
                        .append("</div>");
            }

            html.append("""
                        <div class="card">

                            <h2>Add New Patient</h2>

                            <form method="post"
                                  action="patients">

                                <label for="patientName">
                                    Patient Name
                                </label>

                                <input
                                    type="text"
                                    id="patientName"
                                    name="patientName"
                                    required
                                    placeholder="Enter patient name">

                                <label for="address">
                                    Address
                                </label>

                                <input
                                    type="text"
                                    id="address"
                                    name="address"
                                    required
                                    placeholder="Enter patient address">

                                <label for="contactNumber">
                                    Contact Number
                                </label>

                                <input
                                    type="text"
                                    id="contactNumber"
                                    name="contactNumber"
                                    required
                                    placeholder="Enter contact number">

                                <button type="submit">
                                    Add Patient
                                </button>

                            </form>

                        </div>

                        <div class="card">

                            <h2>Registered Patients</h2>

                            <table>

                                <tr>
                                    <th>ID</th>
                                    <th>Patient Name</th>
                                    <th>Address</th>
                                    <th>Contact Number</th>
                                </tr>
                    """);

            for (Patient patient : patients) {

                html.append("<tr>");

                html.append("<td>")
                        .append(patient.getId())
                        .append("</td>");

                html.append("<td>")
                        .append(patient.getPatientName())
                        .append("</td>");

                html.append("<td>")
                        .append(patient.getAddress())
                        .append("</td>");

                html.append("<td>")
                        .append(patient.getContactNumber())
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

            response.getWriter().write(html.toString());

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load patients",
                    e
            );
        }
    }
}