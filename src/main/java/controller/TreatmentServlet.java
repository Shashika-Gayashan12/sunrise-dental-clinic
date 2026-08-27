package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Treatment;
import com.sunrise.dentalclinic.service.TreatmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/treatments")
public class TreatmentServlet extends HttpServlet {

    private final TreatmentService treatmentService =
            new TreatmentService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        showTreatments(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            String treatmentName =
                    request.getParameter("treatmentName");

            String treatmentCostText =
                    request.getParameter("treatmentCost");

            if (treatmentName == null ||
                    treatmentName.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Treatment name is required."
                );
            }

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

            if (treatmentCost.compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Treatment cost cannot be negative."
                );
            }

            Treatment treatment =
                    new Treatment(
                            treatmentName.trim(),
                            treatmentCost
                    );

            treatmentService.addTreatment(treatment);

            response.sendRedirect(
                    request.getContextPath()
                            + "/treatments"
            );

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

    private void showTreatments(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Treatment> treatments =
                    treatmentService.getAllTreatments();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            String error =
                    (String) request.getAttribute("error");

            String contextPath =
                    request.getContextPath();

            String dashboardUrl =
                    contextPath + "/dashboard";

            StringBuilder html =
                    new StringBuilder();

            html.append("""
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Treatments | Sunrise Dental Clinic</title>

<style>

* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
    font-family: Arial, Helvetica, sans-serif;
}

body {
    background: #f4f7fb;
    color: #1f2937;
}

/* SIDEBAR */

.sidebar {
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    width: 250px;
    background: #0f3d56;
    color: white;
    padding: 25px 15px;
    z-index: 100;
}

.logo {
    padding: 5px 15px 30px;
    border-bottom: 1px solid rgba(255,255,255,.15);
}

.logo h2 {
    font-size: 21px;
    margin-bottom: 6px;
}

.logo p {
    color: #9fd7d5;
    font-size: 12px;
}

.nav {
    margin-top: 25px;
}

.nav a {
    display: flex;
    align-items: center;
    gap: 13px;
    color: #d8e9ed;
    text-decoration: none;
    padding: 13px 15px;
    margin-bottom: 6px;
    border-radius: 9px;
    font-size: 14px;
    transition: .2s;
}

.nav a:hover,
.nav a.active {
    background: #159a9c;
    color: white;
}

.nav-icon {
    width: 25px;
    text-align: center;
    font-size: 17px;
}

/* MAIN */

.main {
    margin-left: 250px;
    min-height: 100vh;
}

/* TOPBAR */

.topbar {
    height: 75px;
    background: white;
    border-bottom: 1px solid #e5e7eb;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 35px;
}

.page-title h1 {
    color: #0f3d56;
    font-size: 24px;
}

.page-title p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 4px;
}

.profile {
    display: flex;
    align-items: center;
    gap: 12px;
}

.profile-circle {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: #159a9c;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
}

/* CONTENT */

.content {
    padding: 32px 35px;
    max-width: 1400px;
    margin: auto;
}

/* WELCOME */

.welcome {
    background: linear-gradient(135deg, #0f3d56, #159a9c);
    color: white;
    padding: 28px;
    border-radius: 15px;
    margin-bottom: 25px;
    box-shadow: 0 8px 25px rgba(15,61,86,.15);
}

.welcome h2 {
    font-size: 24px;
    margin-bottom: 8px;
}

.welcome p {
    color: #d9f4f2;
    font-size: 14px;
}

/* GRID */

.grid {
    display: grid;
    grid-template-columns: 1fr 1.5fr;
    gap: 25px;
}

/* CARD */

.card {
    background: white;
    border-radius: 14px;
    padding: 25px;
    box-shadow: 0 4px 18px rgba(15,61,86,.07);
    margin-bottom: 25px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.card-header h2 {
    color: #0f3d56;
    font-size: 19px;
}

.card-header span {
    color: #6b7280;
    font-size: 12px;
}

/* FORM */

.form-group {
    margin-bottom: 18px;
}

label {
    display: block;
    color: #374151;
    font-size: 13px;
    font-weight: bold;
    margin-bottom: 7px;
}

input {
    width: 100%;
    padding: 13px 14px;
    border: 1px solid #d5dce3;
    border-radius: 8px;
    font-size: 14px;
    outline: none;
    transition: .2s;
}

input:focus {
    border-color: #159a9c;
    box-shadow: 0 0 0 3px rgba(21,154,156,.1);
}

.btn {
    border: none;
    background: #159a9c;
    color: white;
    padding: 13px 20px;
    border-radius: 8px;
    cursor: pointer;
    font-size: 14px;
    font-weight: bold;
    width: 100%;
    transition: .2s;
}

.btn:hover {
    background: #117779;
    transform: translateY(-1px);
}

/* ERROR */

.error {
    background: #fff1f2;
    border-left: 4px solid #dc2626;
    color: #991b1b;
    padding: 14px 16px;
    border-radius: 8px;
    margin-bottom: 20px;
    font-size: 14px;
}

/* TABLE */

.table-card {
    background: white;
    border-radius: 14px;
    padding: 25px;
    box-shadow: 0 4px 18px rgba(15,61,86,.07);
}

.table-wrapper {
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 500px;
}

thead th {
    background: #f1f7f8;
    color: #0f3d56;
    padding: 14px;
    text-align: left;
    font-size: 12px;
    text-transform: uppercase;
    letter-spacing: .4px;
}

tbody td {
    padding: 15px 14px;
    border-bottom: 1px solid #edf0f2;
    font-size: 14px;
}

tbody tr:hover {
    background: #f8fbfc;
}

.id-badge {
    background: #e5f6f5;
    color: #0f7778;
    padding: 5px 9px;
    border-radius: 6px;
    font-weight: bold;
    font-size: 12px;
}

.cost {
    font-weight: bold;
    color: #0f3d56;
}

/* EMPTY */

.empty {
    text-align: center;
    padding: 35px;
    color: #9ca3af;
}

/* MOBILE */

@media (max-width: 950px) {

    .sidebar {
        width: 210px;
    }

    .main {
        margin-left: 210px;
    }

    .grid {
        grid-template-columns: 1fr;
    }
}

@media (max-width: 700px) {

    .sidebar {
        position: relative;
        width: 100%;
        height: auto;
    }

    .main {
        margin-left: 0;
    }

    .nav {
        display: flex;
        overflow-x: auto;
        gap: 5px;
    }

    .nav a {
        white-space: nowrap;
    }

    .content {
        padding: 20px 15px;
    }

    .topbar {
        padding: 0 15px;
    }

    .profile {
        display: none;
    }

    .grid {
        display: block;
    }
}

</style>

</head>

<body>

<aside class="sidebar">

    <div class="logo">
        <h2>Sunrise Dental</h2>
        <p>Clinic Management System</p>
    </div>

    <nav class="nav">

        <a href="${DASHBOARD}" >
            <span class="nav-icon">⌂</span>
            Dashboard
        </a>

        <a href="${CONTEXT}/patients">
            <span class="nav-icon">●</span>
            Patients
        </a>

        <a href="${CONTEXT}/dentists">
            <span class="nav-icon">+</span>
            Dentists
        </a>

        <a href="${CONTEXT}/treatments" class="active">
            <span class="nav-icon">◆</span>
            Treatments
        </a>

        <a href="${CONTEXT}/appointments">
            <span class="nav-icon">▣</span>
            Appointments
        </a>

        <a href="${CONTEXT}/bills">
            <span class="nav-icon">$</span>
            Billing
        </a>

    </nav>

</aside>

<main class="main">

    <header class="topbar">

        <div class="page-title">
            <h1>Treatment Management</h1>
            <p>Manage clinic treatments and pricing</p>
        </div>

        <div class="profile">
            <div class="profile-circle">SD</div>
            <div>
                <strong>Admin</strong><br>
                <small>Clinic Staff</small>
            </div>
        </div>

    </header>

    <section class="content">

        <div class="welcome">
            <h2>Treatment Management</h2>
            <p>Add and manage the treatments offered by Sunrise Dental Clinic.</p>
        </div>
        """);

            String page =
                    html.toString()
                            .replace("${DASHBOARD}", dashboardUrl)
                            .replace("${CONTEXT}", contextPath);

            html = new StringBuilder(page);

            if (error != null) {

                html.append("""
                <div class="error">
                    """);

                html.append(escapeHtml(error));

                html.append("""
                </div>
                """);
            }

            html.append("""
        <div class="grid">

            <div class="card">

                <div class="card-header">
                    <h2>Add Treatment</h2>
                    <span>New treatment</span>
                </div>

                <form method="post"
                      action="
                """)
                    .append(contextPath)
                    .append("""
/treatments">

                    <div class="form-group">

                        <label>Treatment Name</label>

                        <input
                            type="text"
                            name="treatmentName"
                            placeholder="e.g. Dental Cleaning"
                            required>

                    </div>

                    <div class="form-group">

                        <label>Treatment Cost</label>

                        <input
                            type="number"
                            name="treatmentCost"
                            placeholder="Enter cost"
                            step="0.01"
                            min="0"
                            required>

                    </div>

                    <button class="btn"
                            type="submit">
                        + Add Treatment
                    </button>

                </form>

            </div>

            <div class="table-card">

                <div class="card-header">
                    <h2>Available Treatments</h2>
                    <span>
                """)
                    .append(treatments.size())
                    .append("""
                    treatment(s)
                    </span>
                </div>

                <div class="table-wrapper">

                    <table>

                        <thead>

                            <tr>
                                <th>ID</th>
                                <th>Treatment</th>
                                <th>Cost</th>
                            </tr>

                        </thead>

                        <tbody>
                """);

            if (treatments.isEmpty()) {

                html.append("""
                    <tr>
                        <td colspan="3" class="empty">
                            No treatments available.
                        </td>
                    </tr>
                """);

            } else {

                for (Treatment treatment : treatments) {

                    html.append("""
                    <tr>

                        <td>
                            <span class="id-badge">
                    """)
                            .append(treatment.getId())
                            .append("""
                            </span>
                        </td>

                        <td>
                    """)
                            .append(
                                    escapeHtml(
                                            treatment.getTreatmentName()
                                    )
                            )
                            .append("""
                        </td>

                        <td class="cost">
                            Rs. 
                    """)
                            .append(treatment.getTreatmentCost())
                            .append("""
                        </td>

                    </tr>
                    """);
                }
            }

            html.append("""
                        </tbody>

                    </table>

                </div>

            </div>

        </div>

    </section>

</main>

</body>
</html>
""");

            response.getWriter()
                    .write(html.toString());

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load treatments.",
                    e
            );
        }
    }

    private String escapeHtml(String text) {

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