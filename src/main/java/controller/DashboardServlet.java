package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // ============================
        // CHECK LOGIN SESSION
        // ============================

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        // ============================
        // GET LOGGED-IN USER
        // ============================

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        User user =
                (User) userObject;

        // ============================
        // CHECK USER STATUS
        // ============================

        if (!"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        // ============================
        // UPDATE SESSION ROLE
        // ============================

        session.setAttribute(
                "role",
                user.getRole()
        );

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        // IMPORTANT:
        // trim() removes any accidental whitespace.
        String contextPath =
                request.getContextPath().trim();

        // ============================
        // START HTML
        // ============================

        StringBuilder html =
                new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html lang="en">

                <head>

                <meta charset="UTF-8">

                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0">

                <title>Dashboard - Sunrise Dental Clinic</title>

                <style>

                * {
                    box-sizing: border-box;
                    margin: 0;
                    padding: 0;
                    font-family: Arial, sans-serif;
                }

                body {
                    background: #f4f7fb;
                    color: #1f2937;
                    min-height: 100vh;
                }

                /* =========================
                   HEADER
                   ========================= */

                header {
                    background: #0f3d56;
                    color: white;
                    padding: 20px 40px;

                    display: flex;
                    justify-content: space-between;
                    align-items: center;

                    gap: 20px;
                }

                .header-left h1 {
                    margin-bottom: 5px;
                    font-size: 26px;
                }

                .header-left p {
                    color: #c9e8e5;
                    font-size: 14px;
                }

                .header-right {
                    display: flex;
                    align-items: center;
                    gap: 15px;
                }

                .role-badge {
                    background: #159a9c;
                    color: white;

                    padding: 8px 14px;

                    border-radius: 20px;

                    font-size: 13px;

                    font-weight: bold;
                }

                .logout {
                    color: white;

                    text-decoration: none;

                    background: #dc2626;

                    padding: 10px 16px;

                    border-radius: 6px;

                    font-weight: bold;

                    transition: 0.2s;
                }

                .logout:hover {
                    background: #b91c1c;
                }

                /* =========================
                   NAVIGATION
                   ========================= */

                nav {
                    background: white;

                    padding: 15px 40px;

                    box-shadow:
                        0 2px 8px
                        rgba(0,0,0,0.08);

                    display: flex;

                    flex-wrap: wrap;

                    gap: 22px;
                }

                nav a {
                    text-decoration: none;

                    color: #0f3d56;

                    font-weight: bold;

                    font-size: 14px;
                }

                nav a:hover {
                    color: #159a9c;
                }

                .admin-nav {
                    color: #159a9c;
                }

                /* =========================
                   MAIN CONTAINER
                   ========================= */

                .container {
                    max-width: 1200px;

                    margin: auto;

                    padding: 40px;
                }

                /* =========================
                   WELCOME
                   ========================= */

                .welcome {
                    background: white;

                    padding: 30px;

                    border-radius: 10px;

                    box-shadow:
                        0 3px 12px
                        rgba(0,0,0,0.08);

                    margin-bottom: 30px;
                }

                .welcome h2 {
                    color: #0f3d56;

                    margin-bottom: 10px;
                }

                .welcome p {
                    color: #6b7280;

                    line-height: 1.6;
                }

                .welcome strong {
                    color: #159a9c;
                }

                /* =========================
                   SECTION TITLE
                   ========================= */

                .section-title {
                    color: #0f3d56;

                    margin-bottom: 20px;
                }

                /* =========================
                   CARDS
                   ========================= */

                .cards {
                    display: grid;

                    grid-template-columns:
                        repeat(
                            auto-fit,
                            minmax(220px, 1fr)
                        );

                    gap: 20px;
                }

                .card {
                    display: block;

                    background: white;

                    padding: 25px;

                    border-radius: 10px;

                    box-shadow:
                        0 3px 12px
                        rgba(0,0,0,0.08);

                    text-decoration: none;

                    color: #1f2937;

                    transition: 0.2s;

                    border-left:
                        5px solid #159a9c;
                }

                .card:hover {
                    transform:
                        translateY(-4px);

                    box-shadow:
                        0 7px 20px
                        rgba(0,0,0,0.12);
                }

                .card h3 {
                    color: #0f3d56;

                    margin-bottom: 10px;
                }

                .card p {
                    color: #6b7280;

                    line-height: 1.5;

                    margin-bottom: 15px;
                }

                .card-button {
                    display: inline-block;

                    padding: 9px 15px;

                    background: #159a9c;

                    color: white;

                    border-radius: 5px;

                    font-size: 13px;

                    font-weight: bold;
                }

                /* =========================
                   ADMIN CARD
                   ========================= */

                .admin-card {
                    border-left:
                        5px solid #0f3d56;

                    background:
                        linear-gradient(
                            135deg,
                            #ffffff,
                            #f0f8fa
                        );
                }

                .admin-card .card-button {
                    background: #0f3d56;
                }

                /* =========================
                   FOOTER
                   ========================= */

                footer {
                    margin-top: 40px;

                    padding: 25px;

                    text-align: center;

                    color: #6b7280;

                    font-size: 13px;
                }

                /* =========================
                   MOBILE
                   ========================= */

                @media (max-width: 700px) {

                    header {
                        padding: 20px;

                        flex-direction: column;

                        align-items: flex-start;
                    }

                    .header-right {
                        width: 100%;

                        justify-content:
                            space-between;
                    }

                    nav {
                        padding: 15px 20px;

                        gap: 15px;
                    }

                    .container {
                        padding: 25px 20px;
                    }

                    .welcome {
                        padding: 22px;
                    }

                }

                </style>

                </head>

                <body>
                """);

        // ============================
        // HEADER
        // ============================

        html.append("""
                <header>

                    <div class="header-left">

                        <h1>
                            Sunrise Dental Clinic
                        </h1>

                        <p>
                            Dental Clinic Management System
                        </p>

                    </div>

                    <div class="header-right">

                        <span class="role-badge">
                """);

        html.append(
                escapeHtml(user.getRole())
        );

        html.append("""
                        </span>
                """);

        // IMPORTANT:
        // Build logout URL as ONE complete string.
        String logoutUrl =
                contextPath + "/logout";

        html.append(
                "<a class=\"logout\" href=\"" +
                        logoutUrl +
                        "\">Logout</a>"
        );

        html.append("""
                    </div>

                </header>
                """);

        // ============================
        // NAVIGATION
        // ============================

        html.append("<nav>");

        // Dashboard
        String dashboardUrl =
                contextPath + "/dashboard";

        html.append(
                "<a href=\"" +
                        dashboardUrl +
                        "\">Dashboard</a>"
        );

        // Patients
        String patientsUrl =
                contextPath + "/patients";

        html.append(
                "<a href=\"" +
                        patientsUrl +
                        "\">Patients</a>"
        );

        // Appointments
        String appointmentsUrl =
                contextPath + "/appointments";

        html.append(
                "<a href=\"" +
                        appointmentsUrl +
                        "\">Appointments</a>"
        );

        // Dentists
        String dentistsUrl =
                contextPath + "/dentists";

        html.append(
                "<a href=\"" +
                        dentistsUrl +
                        "\">Dentists</a>"
        );

        // Treatments
        String treatmentsUrl =
                contextPath + "/treatments";

        html.append(
                "<a href=\"" +
                        treatmentsUrl +
                        "\">Treatments</a>"
        );

        // Billing
        String billsUrl =
                contextPath + "/bills";

        html.append(
                "<a href=\"" +
                        billsUrl +
                        "\">Billing</a>"
        );

        // Manage Users - ADMIN ONLY
        if ("ADMIN".equalsIgnoreCase(
                user.getRole())) {

            String manageUsersUrl =
                    contextPath + "/manage-users";

            html.append(
                    "<a class=\"admin-nav\" href=\"" +
                            manageUsersUrl +
                            "\">Manage Users</a>"
            );
        }

        html.append("</nav>");

        // ============================
        // MAIN CONTENT
        // ============================

        html.append("""
                <div class="container">

                    <div class="welcome">

                        <h2>
                            Welcome,
                """);

        html.append(
                escapeHtml(user.getUsername())
        );

        html.append("""
                            👋
                        </h2>

                        <p>
                            You are logged in as
                            <strong>
                """);

        html.append(
                escapeHtml(user.getRole())
        );

        html.append("""
                            </strong>.
                            Select an option below
                            to manage the clinic.
                        </p>

                    </div>

                    <h2 class="section-title">
                        Clinic Management
                    </h2>

                    <div class="cards">
                """);

        // ============================
        // PATIENTS CARD
        // ============================

        html.append(
                "<a class=\"card\" href=\"" +
                        patientsUrl +
                        "\">"
        );

        html.append("""
                            <h3>
                                Patients
                            </h3>

                            <p>
                                Register and manage
                                patient information.
                            </p>

                            <span class="card-button">
                                Manage Patients
                            </span>

                        </a>
                """);

        // ============================
        // APPOINTMENTS CARD
        // ============================

        html.append(
                "<a class=\"card\" href=\"" +
                        appointmentsUrl +
                        "\">"
        );

        html.append("""
                            <h3>
                                Appointments
                            </h3>

                            <p>
                                Schedule and manage
                                dental appointments.
                            </p>

                            <span class="card-button">
                                Manage Appointments
                            </span>

                        </a>
                """);

        // ============================
        // DENTISTS CARD
        // ============================

        html.append(
                "<a class=\"card\" href=\"" +
                        dentistsUrl +
                        "\">"
        );

        html.append("""
                            <h3>
                                Dentists
                            </h3>

                            <p>
                                Manage dentists,
                                specializations and
                                availability.
                            </p>

                            <span class="card-button">
                                Manage Dentists
                            </span>

                        </a>
                """);

        // ============================
        // TREATMENTS CARD
        // ============================

        html.append(
                "<a class=\"card\" href=\"" +
                        treatmentsUrl +
                        "\">"
        );

        html.append("""
                            <h3>
                                Treatments
                            </h3>

                            <p>
                                Manage dental treatments
                                and treatment information.
                            </p>

                            <span class="card-button">
                                Manage Treatments
                            </span>

                        </a>
                """);

        // ============================
        // BILLING CARD
        // ============================

        html.append(
                "<a class=\"card\" href=\"" +
                        billsUrl +
                        "\">"
        );

        html.append("""
                            <h3>
                                Billing
                            </h3>

                            <p>
                                Create and manage
                                patient bills.
                            </p>

                            <span class="card-button">
                                Manage Billing
                            </span>

                        </a>
                """);

        // ============================
        // ADMIN ONLY CARD
        // ============================

        if ("ADMIN".equalsIgnoreCase(
                user.getRole())) {

            String manageUsersUrl =
                    contextPath + "/manage-users";

            html.append(
                    "<a class=\"card admin-card\" href=\"" +
                            manageUsersUrl +
                            "\">"
            );

            html.append("""
                            <h3>
                                Manage Users
                            </h3>

                            <p>
                                Approve new users,
                                activate or deactivate
                                accounts, and manage
                                system users.
                            </p>

                            <span class="card-button">
                                Manage Users
                            </span>

                        </a>
                    """);
        }

        // ============================
        // END MAIN CONTENT
        // ============================

        html.append("""
                    </div>

                </div>

                <footer>

                    <p>
                        © 2026 Sunrise Dental Clinic
                        Management System
                    </p>

                </footer>

                </body>

                </html>
                """);

        // ============================
        // SEND HTML
        // ============================

        response.getWriter()
                .write(html.toString());
    }

    // ============================
    // HTML ESCAPE
    // ============================

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