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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/dentist-availability")
public class DentistAvailabilityServlet
        extends HttpServlet {

    private final DentistAvailabilityService
            availabilityService =
            new DentistAvailabilityService();

    private final DentistService dentistService =
            new DentistService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        showAvailability(
                request,
                response
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            Long dentistId =
                    Long.parseLong(
                            request.getParameter(
                                    "dentistId"
                            ).trim()
                    );

            LocalDate availableDate =
                    LocalDate.parse(
                            request.getParameter(
                                    "availableDate"
                            ).trim()
                    );

            LocalTime startTime =
                    LocalTime.parse(
                            request.getParameter(
                                    "startTime"
                            ).trim()
                    );

            LocalTime endTime =
                    LocalTime.parse(
                            request.getParameter(
                                    "endTime"
                            ).trim()
                    );

            String dayOfWeek =
                    availableDate
                            .getDayOfWeek()
                            .toString();

            dayOfWeek =
                    dayOfWeek.substring(0, 1)
                            + dayOfWeek.substring(1)
                            .toLowerCase();

            DentistAvailability availability =
                    new DentistAvailability(
                            dentistId,
                            dayOfWeek,
                            availableDate,
                            startTime,
                            endTime
                    );

            availabilityService.addAvailability(
                    availability
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/dentist-availability"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showAvailability(
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

    private void showAvailability(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Dentist> dentists =
                    dentistService.getAllDentists();

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
                        Dentist Availability -
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

                    .container {
                        max-width: 1100px;
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
                        margin-top: 0;
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
                        margin-bottom: 20px;
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

                        <h1>
                            Sunrise Dental Clinic
                        </h1>

                        <p>
                            Dentist Availability
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

                    <h2>
                        Add Dentist Availability
                    </h2>

                    <form method="post"
                          action="dentist-availability">

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
                        Availability Date
                    </label>

                    <input
                        type="date"
                        name="availableDate"
                        required>

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
                        Add Availability
                    </button>

                    </form>

                    </div>

                    </div>

                    </body>

                    </html>
                    """);

            response.getWriter()
                    .write(
                            html.toString()
                    );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load dentists.",
                    e
            );
        }
    }
}