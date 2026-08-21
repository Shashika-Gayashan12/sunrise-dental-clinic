package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.DentistAvailability;
import com.sunrise.dentalclinic.service.DentistService;
import com.sunrise.dentalclinic.service.DentistAvailabilityService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/dentists")
public class DentistServlet extends HttpServlet {

    private final DentistService dentistService =
            new DentistService();

    private final DentistAvailabilityService availabilityService =
            new DentistAvailabilityService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        showDentists(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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

            request.setAttribute("error", e.getMessage());

            showDentists(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
        }
    }

    private void addDentist(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String dentistName =
                request.getParameter("dentistName").trim();

        String specialization =
                request.getParameter("specialization").trim();

        String contactNumber =
                request.getParameter("contactNumber").trim();

        Dentist dentist = new Dentist(
                dentistName,
                specialization,
                contactNumber
        );

        dentistService.addDentist(dentist);

        response.sendRedirect(
                request.getContextPath() + "/dentists"
        );
    }

    private void addAvailability(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String dentistIdText =
                request.getParameter("dentistId");

        if (dentistIdText == null ||
                dentistIdText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Dentist ID is required."
            );
        }

        Long dentistId;

        try {

            dentistId = Long.parseLong(
                    dentistIdText.trim()
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid dentist ID: " +
                            dentistIdText
            );
        }

        String dayOfWeek =
                request.getParameter("dayOfWeek");

        if (dayOfWeek == null ||
                dayOfWeek.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select a day."
            );
        }

        LocalTime startTime;
        LocalTime endTime;

        try {

            startTime = LocalTime.parse(
                    request.getParameter("startTime").trim()
            );

            endTime = LocalTime.parse(
                    request.getParameter("endTime").trim()
            );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Please enter valid start and end times."
            );
        }

        if (!endTime.isAfter(startTime)) {

            throw new IllegalArgumentException(
                    "End time must be later than start time."
            );
        }

        /*
         * Weekly recurring schedule.
         *
         * availableDate is null because this schedule
         * applies to the selected day every week.
         */
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

    private void showDentists(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Dentist> dentists =
                    dentistService.getAllDentists();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            String error =
                    (String) request.getAttribute("error");

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
                        Dentists - Sunrise Dental Clinic
                    </title>

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
                        max-width: 1200px;
                        margin: auto;
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
                        margin-top: 0;
                    }

                    label {
                        display: block;
                        margin-top: 15px;
                        margin-bottom: 5px;
                        font-weight: bold;
                    }

                    input,
                    select {
                        width: 100%;
                        padding: 11px;
                        border: 1px solid #d1d5db;
                        border-radius: 6px;
                        font-size: 15px;
                        background: white;
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

                    th,
                    td {
                        padding: 12px;
                        border-bottom: 1px solid #ddd;
                        text-align: left;
                        vertical-align: top;
                    }

                    th {
                        background: #0f3d56;
                        color: white;
                    }

                    .schedule {
                        margin-top: 15px;
                    }

                    .schedule-item {
                        padding: 9px;
                        background: #f4f7fb;
                        border-radius: 6px;
                        margin-bottom: 6px;
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
                        <p>Dentist Management System</p>
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

                    <h2>Add New Dentist</h2>

                    <form method="post"
                          action="dentists">

                        <input type="hidden"
                               name="action"
                               value="addDentist">

                        <label>
                            Dentist Name
                        </label>

                        <input
                            type="text"
                            name="dentistName"
                            required
                            placeholder="Enter dentist name">

                        <label>
                            Specialization
                        </label>

                        <select
                            name="specialization"
                            required>

                            <option value="">
                                Select specialization
                            </option>

                            <option value="General Dentistry">
                                General Dentistry
                            </option>

                            <option value="Orthodontics">
                                Orthodontics
                            </option>

                            <option value="Endodontics">
                                Endodontics
                            </option>

                            <option value="Periodontics">
                                Periodontics
                            </option>

                            <option value="Prosthodontics">
                                Prosthodontics
                            </option>

                            <option value="Pediatric Dentistry">
                                Pediatric Dentistry
                            </option>

                            <option value="Oral & Maxillofacial Surgery">
                                Oral & Maxillofacial Surgery
                            </option>

                            <option value="Oral Medicine">
                                Oral Medicine
                            </option>

                        </select>

                        <label>
                            Contact Number
                        </label>

                        <input
                            type="text"
                            name="contactNumber"
                            required
                            placeholder="Enter contact number">

                        <button type="submit">
                            Add Dentist
                        </button>

                    </form>

                    </div>
                    """);

            html.append("""
                    <div class="card">

                    <h2>Registered Dentists</h2>

                    <table>

                    <tr>
                        <th>ID</th>
                        <th>Dentist Name</th>
                        <th>Specialization</th>
                        <th>Contact</th>
                        <th>Availability</th>
                    </tr>
                    """);

            for (Dentist dentist : dentists) {

                html.append("<tr>");

                html.append("<td>")
                        .append(dentist.getId())
                        .append("</td>");

                html.append("<td>")
                        .append(dentist.getDentistName())
                        .append("</td>");

                html.append("<td>")
                        .append(dentist.getSpecialization())
                        .append("</td>");

                html.append("<td>")
                        .append(dentist.getContactNumber())
                        .append("</td>");

                html.append("<td>");

                html.append("""
                        <form method="post"
                              action="dentists">

                            <input type="hidden"
                                   name="action"
                                   value="addAvailability">

                            <input type="hidden"
                                   name="dentistId"
                                   value="
                        """);

                html.append(dentist.getId());

                html.append("""
                            ">

                            <label>
                                Day
                            </label>

                            <select
                                name="dayOfWeek"
                                required>

                                <option value="">
                                    Select day
                                </option>

                                <option value="Monday">
                                    Monday
                                </option>

                                <option value="Tuesday">
                                    Tuesday
                                </option>

                                <option value="Wednesday">
                                    Wednesday
                                </option>

                                <option value="Thursday">
                                    Thursday
                                </option>

                                <option value="Friday">
                                    Friday
                                </option>

                                <option value="Saturday">
                                    Saturday
                                </option>

                                <option value="Sunday">
                                    Sunday
                                </option>

                            </select>

                            <label>
                                Start Time
                            </label>

                            <input
                                type="time"
                                name="startTime"
                                required>

                            <label>
                                End Time
                            </label>

                            <input
                                type="time"
                                name="endTime"
                                required>

                            <button type="submit">
                                Add Schedule
                            </button>

                        </form>
                        """);

                List<DentistAvailability> schedules =
                        availabilityService
                                .getByDentistId(
                                        dentist.getId()
                                );

                if (!schedules.isEmpty()) {

                    html.append("""
                            <div class="schedule">

                            <strong>
                                Current Schedule
                            </strong>
                            """);

                    for (DentistAvailability schedule :
                            schedules) {

                        html.append("""
                                <div class="schedule-item">
                                """);

                        html.append(
                                schedule.getDayOfWeek()
                        );

                        html.append(" : ");

                        html.append(
                                schedule.getStartTime()
                        );

                        html.append(" - ");

                        html.append(
                                schedule.getEndTime()
                        );

                        html.append("""
                                </div>
                                """);
                    }

                    html.append("""
                            </div>
                            """);
                }

                html.append("</td>");
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
                    "Unable to load dentists.",
                    e
            );
        }
    }
}