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
                    (String) request.getAttribute(
                            "error"
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
                        Billing - Sunrise Dental Clinic
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

                    th,
                    td {
                        padding: 12px;
                        border-bottom: 1px solid #ddd;
                        text-align: left;
                        vertical-align: middle;
                    }

                    th {
                        background: #0f3d56;
                        color: white;
                    }

                    .print-button {
                        display: inline-block;
                        padding: 8px 14px;
                        background: #159a9c;
                        color: white;
                        text-decoration: none;
                        border-radius: 6px;
                        font-weight: bold;
                    }

                    .print-button:hover {
                        background: #117779;
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
                        <p>Billing Management System</p>
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

                    <h2>Create Bill</h2>

                    <form method="post"
                          action="bills">

                        <label>
                            Appointment ID
                        </label>

                        <input
                            type="number"
                            name="appointmentId"
                            min="1"
                            required
                            placeholder="Enter appointment ID">

                        <label>
                            Consultation Fee
                        </label>

                        <input
                            type="number"
                            name="consultationFee"
                            step="0.01"
                            min="0"
                            required
                            placeholder="Enter consultation fee">

                        <label>
                            Treatment Cost
                        </label>

                        <input
                            type="number"
                            name="treatmentCost"
                            step="0.01"
                            min="0"
                            required
                            placeholder="Enter treatment cost">

                        <button type="submit">
                            Create Bill
                        </button>

                    </form>

                    </div>

                    <div class="card">

                    <h2>Generated Bills</h2>

                    <table>

                    <tr>
                        <th>ID</th>
                        <th>Appointment ID</th>
                        <th>Bill Date</th>
                        <th>Consultation Fee</th>
                        <th>Treatment Cost</th>
                        <th>Total Amount</th>
                        <th>Action</th>
                    </tr>
                    """);

            for (Bill bill : bills) {

                html.append("<tr>");

                html.append("<td>")
                        .append(bill.getId())
                        .append("</td>");

                html.append("<td>")
                        .append(bill.getAppointmentId())
                        .append("</td>");

                html.append("<td>")
                        .append(bill.getBillDate())
                        .append("</td>");

                html.append("<td>")
                        .append(bill.getConsultationFee())
                        .append("</td>");

                html.append("<td>")
                        .append(bill.getTreatmentCost())
                        .append("</td>");

                html.append("<td>")
                        .append(bill.getTotalAmount())
                        .append("</td>");

                /*
                 * PRINT BILL BUTTON
                 */

                html.append("<td>");

                html.append("""
                        <a
                            class="print-button"
                            href="printBill?id=
                        """);

                html.append(bill.getId());

                html.append("""
                            "
                            target="_blank">
                            Print
                        </a>
                        """);

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
                    "Unable to load bills.",
                    e
            );
        }
    }
}