package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.service.BillService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/bills")
public class BillServlet extends HttpServlet {

    private final BillService billService =
            new BillService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        showBills(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            String appointmentIdText =
                    request.getParameter("appointmentId");

            String consultationFeeText =
                    request.getParameter("consultationFee");

            String treatmentCostText =
                    request.getParameter("treatmentCost");

            if (appointmentIdText == null ||
                    appointmentIdText.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Appointment ID is required."
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
                        "Invalid appointment ID."
                );
            }

            if (consultationFeeText == null ||
                    consultationFeeText.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Consultation fee is required."
                );
            }

            if (treatmentCostText == null ||
                    treatmentCostText.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Treatment cost is required."
                );
            }

            BigDecimal consultationFee;
            BigDecimal treatmentCost;

            try {

                consultationFee =
                        new BigDecimal(
                                consultationFeeText.trim()
                        );

                treatmentCost =
                        new BigDecimal(
                                treatmentCostText.trim()
                        );

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "Please enter valid amounts."
                );
            }

            if (consultationFee.compareTo(BigDecimal.ZERO) < 0 ||
                    treatmentCost.compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Amounts cannot be negative."
                );
            }

            Bill bill =
                    new Bill(
                            appointmentId,
                            consultationFee,
                            treatmentCost
                    );

            billService.addBill(bill);

            response.sendRedirect(
                    request.getContextPath()
                            + "/bills"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showBills(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
        }
    }

    private void showBills(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Bill> bills =
                    billService.getAllBills();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            String error =
                    (String) request.getAttribute("error");

            String contextPath =
                    request.getContextPath();

            String dashboardUrl =
                    contextPath + "/dashboard";

            String printUrl =
                    contextPath + "/printBill?id=";

            StringBuilder html =
                    new StringBuilder();

            html.append("""
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Billing | Sunrise Dental Clinic</title>

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
}

.nav a:hover,
.nav a.active {
    background: #159a9c;
    color: white;
}

.nav-icon {
    width: 25px;
    text-align: center;
}

/* MAIN */

.main {
    margin-left: 250px;
    min-height: 100vh;
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
    justify-content: center;
    align-items: center;
    font-weight: bold;
}

/* CONTENT */

.content {
    padding: 32px 35px;
    max-width: 1450px;
    margin: auto;
}

/* HERO */

.hero {
    background: linear-gradient(135deg,#0f3d56,#159a9c);
    color: white;
    padding: 28px;
    border-radius: 15px;
    margin-bottom: 25px;
    box-shadow: 0 8px 25px rgba(15,61,86,.15);
}

.hero h2 {
    margin-bottom: 7px;
}

.hero p {
    color: #d9f4f2;
    font-size: 14px;
}

/* STATS */

.stats {
    display: grid;
    grid-template-columns: repeat(3,1fr);
    gap: 20px;
    margin-bottom: 25px;
}

.stat {
    background: white;
    border-radius: 13px;
    padding: 22px;
    box-shadow: 0 4px 18px rgba(15,61,86,.06);
}

.stat-label {
    color: #6b7280;
    font-size: 13px;
}

.stat-value {
    color: #0f3d56;
    font-size: 25px;
    font-weight: bold;
    margin-top: 8px;
}

/* GRID */

.grid {
    display: grid;
    grid-template-columns: 380px 1fr;
    gap: 25px;
}

/* CARD */

.card {
    background: white;
    border-radius: 14px;
    padding: 25px;
    box-shadow: 0 4px 18px rgba(15,61,86,.07);
}

.card h2 {
    color: #0f3d56;
    font-size: 19px;
    margin-bottom: 20px;
}

/* FORM */

.form-group {
    margin-bottom: 17px;
}

label {
    display: block;
    font-size: 13px;
    font-weight: bold;
    color: #374151;
    margin-bottom: 7px;
}

input {
    width: 100%;
    padding: 13px;
    border: 1px solid #d5dce3;
    border-radius: 8px;
    outline: none;
    font-size: 14px;
}

input:focus {
    border-color: #159a9c;
    box-shadow: 0 0 0 3px rgba(21,154,156,.1);
}

.btn {
    width: 100%;
    border: none;
    background: #159a9c;
    color: white;
    padding: 13px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: bold;
    cursor: pointer;
}

.btn:hover {
    background: #117779;
}

/* TABLE */

.table-wrapper {
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 850px;
}

th {
    background: #f1f7f8;
    color: #0f3d56;
    text-align: left;
    padding: 14px;
    font-size: 12px;
    text-transform: uppercase;
}

td {
    padding: 14px;
    border-bottom: 1px solid #edf0f2;
    font-size: 13px;
}

tr:hover td {
    background: #f8fbfc;
}

.id {
    background: #e5f6f5;
    color: #087477;
    padding: 5px 9px;
    border-radius: 6px;
    font-weight: bold;
}

.total {
    font-weight: bold;
    color: #0f3d56;
}

.print {
    display: inline-block;
    text-decoration: none;
    background: #159a9c;
    color: white;
    padding: 8px 13px;
    border-radius: 7px;
    font-size: 12px;
    font-weight: bold;
}

.print:hover {
    background: #117779;
}

/* ERROR */

.error {
    background: #fff1f2;
    color: #991b1b;
    border-left: 4px solid #dc2626;
    padding: 14px;
    border-radius: 8px;
    margin-bottom: 20px;
}

/* EMPTY */

.empty {
    text-align: center;
    padding: 35px;
    color: #9ca3af;
}

/* MOBILE */

@media(max-width:1000px) {

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

@media(max-width:700px) {

    .sidebar {
        position: relative;
        width: 100%;
    }

    .main {
        margin-left: 0;
    }

    .nav {
        display: flex;
        overflow-x: auto;
    }

    .nav a {
        white-space: nowrap;
    }

    .content {
        padding: 20px 15px;
    }

    .stats {
        grid-template-columns: 1fr;
    }

    .profile {
        display: none;
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

        <a href="${DASHBOARD}">
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

        <a href="${CONTEXT}/treatments">
            <span class="nav-icon">◆</span>
            Treatments
        </a>

        <a href="${CONTEXT}/appointments">
            <span class="nav-icon">▣</span>
            Appointments
        </a>

        <a href="${CONTEXT}/bills" class="active">
            <span class="nav-icon">$</span>
            Billing
        </a>

    </nav>

</aside>

<main class="main">

<header class="topbar">

    <div class="page-title">
        <h1>Billing Management</h1>
        <p>Create, manage and print patient bills</p>
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

<div class="hero">

    <h2>Billing & Payments</h2>

    <p>
        Create patient bills and manage generated invoices
        from one place.
    </p>

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
""")
                        .append(escapeHtml(error))
                        .append("""
</div>
""");
            }

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

            html.append("""
<div class="stats">

    <div class="stat">
        <div class="stat-label">
            Total Bills
        </div>

        <div class="stat-value">
""")
                    .append(bills.size())
                    .append("""
        </div>
    </div>

    <div class="stat">
        <div class="stat-label">
            Total Revenue
        </div>

        <div class="stat-value">
            Rs.
""")
                    .append(totalRevenue)
                    .append("""
        </div>
    </div>

    <div class="stat">
        <div class="stat-label">
            Billing System
        </div>

        <div class="stat-value">
            Active
        </div>
    </div>

</div>

<div class="grid">

    <div class="card">

        <h2>Create New Bill</h2>

        <form method="post"
              action="
""")
                    .append(contextPath)
                    .append("""
/bills">

            <div class="form-group">

                <label>
                    Appointment ID
                </label>

                <input
                    type="number"
                    name="appointmentId"
                    min="1"
                    required
                    placeholder="Enter appointment ID">

            </div>

            <div class="form-group">

                <label>
                    Consultation Fee
                </label>

                <input
                    type="number"
                    id="consultationFee"
                    name="consultationFee"
                    min="0"
                    step="0.01"
                    required
                    placeholder="0.00">

            </div>

            <div class="form-group">

                <label>
                    Treatment Cost
                </label>

                <input
                    type="number"
                    id="treatmentCost"
                    name="treatmentCost"
                    min="0"
                    step="0.01"
                    required
                    placeholder="0.00">

            </div>

            <div class="form-group">

                <label>
                    Estimated Total
                </label>

                <input
                    type="text"
                    id="totalPreview"
                    value="Rs. 0.00"
                    readonly>

            </div>

            <button class="btn"
                    type="submit">

                + Create Bill

            </button>

        </form>

    </div>

    <div class="card">

        <h2>Generated Bills</h2>

        <div class="table-wrapper">

        <table>

            <thead>

                <tr>
                    <th>ID</th>
                    <th>Appointment</th>
                    <th>Date</th>
                    <th>Consultation</th>
                    <th>Treatment</th>
                    <th>Total</th>
                    <th>Action</th>
                </tr>

            </thead>

            <tbody>
""");

            if (bills.isEmpty()) {

                html.append("""
                <tr>
                    <td colspan="7"
                        class="empty">
                        No bills have been generated yet.
                    </td>
                </tr>
                """);

            } else {

                for (Bill bill : bills) {

                    html.append("""
                <tr>

                    <td>
                        <span class="id">
""")
                            .append(bill.getId())
                            .append("""
                        </span>
                    </td>

                    <td>
""")
                            .append(bill.getAppointmentId())
                            .append("""
                    </td>

                    <td>
""")
                            .append(bill.getBillDate())
                            .append("""
                    </td>

                    <td>
                        Rs.
""")
                            .append(bill.getConsultationFee())
                            .append("""
                    </td>

                    <td>
                        Rs.
""")
                            .append(bill.getTreatmentCost())
                            .append("""
                    </td>

                    <td class="total">
                        Rs.
""")
                            .append(bill.getTotalAmount())
                            .append("""
                    </td>

                    <td>
                        <a class="print"
                           href="
""")
                            .append(printUrl)
                            .append(bill.getId())
                            .append("""
"
                           target="_blank">
                            Print
                        </a>
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

<script>

function calculateTotal() {

    const consultation =
        parseFloat(
            document.getElementById(
                "consultationFee"
            ).value
        ) || 0;

    const treatment =
        parseFloat(
            document.getElementById(
                "treatmentCost"
            ).value
        ) || 0;

    const total =
        consultation + treatment;

    document.getElementById(
        "totalPreview"
    ).value =
        "Rs. " + total.toFixed(2);
}

document.getElementById(
    "consultationFee"
).addEventListener(
    "input",
    calculateTotal
);

document.getElementById(
    "treatmentCost"
).addEventListener(
    "input",
    calculateTotal
);

</script>

</body>
</html>
""");

            response.getWriter()
                    .write(html.toString());

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load bills.",
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