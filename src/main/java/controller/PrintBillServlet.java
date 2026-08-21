package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Bill;
import com.sunrise.dentalclinic.service.BillService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/printBill")
public class PrintBillServlet extends HttpServlet {

    private final BillService billService =
            new BillService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String billIdText =
                request.getParameter("id");

        if (billIdText == null ||
                billIdText.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Bill ID is required."
            );

            return;
        }

        Long billId;

        try {

            billId =
                    Long.parseLong(
                            billIdText.trim()
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid bill ID."
            );

            return;
        }

        try {

            List<Bill> bills =
                    billService.getAllBills();

            Bill selectedBill = null;

            for (Bill bill : bills) {

                if (bill.getId() != null &&
                        bill.getId().equals(billId)) {

                    selectedBill = bill;
                    break;
                }
            }

            if (selectedBill == null) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Bill not found."
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
                        Print Bill - Sunrise Dental Clinic
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

                    .container {
                        max-width: 800px;
                        margin: 40px auto;
                        background: white;
                        padding: 40px;

                        box-shadow:
                            0 3px 15px
                            rgba(0,0,0,0.10);
                    }

                    .header {
                        text-align: center;
                        border-bottom: 2px solid #0f3d56;
                        padding-bottom: 20px;
                        margin-bottom: 30px;
                    }

                    .header h1 {
                        margin: 0;
                        color: #0f3d56;
                    }

                    .header p {
                        margin: 6px 0;
                        color: #6b7280;
                    }

                    .bill-title {
                        text-align: center;
                        margin-bottom: 30px;
                    }

                    .bill-title h2 {
                        color: #0f3d56;
                        margin-bottom: 5px;
                    }

                    .bill-info {
                        display: grid;
                        grid-template-columns:
                            1fr 1fr;

                        gap: 15px;

                        margin-bottom: 30px;
                    }

                    .info-box {
                        padding: 15px;
                        background: #f4f7fb;
                        border-radius: 6px;
                    }

                    .info-label {
                        font-size: 13px;
                        color: #6b7280;
                        margin-bottom: 5px;
                    }

                    .info-value {
                        font-size: 16px;
                        font-weight: bold;
                        color: #0f3d56;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin-top: 20px;
                    }

                    th {
                        background: #0f3d56;
                        color: white;
                        padding: 14px;
                        text-align: left;
                    }

                    td {
                        padding: 14px;
                        border-bottom: 1px solid #ddd;
                    }

                    .amount {
                        text-align: right;
                    }

                    .total-row {
                        font-size: 18px;
                        font-weight: bold;
                        background: #f4f7fb;
                    }

                    .footer {
                        text-align: center;
                        margin-top: 40px;
                        padding-top: 20px;
                        border-top: 1px solid #ddd;
                        color: #6b7280;
                    }

                    .print-button {
                        display: block;
                        margin: 25px auto 0 auto;
                        padding: 12px 25px;
                        border: none;
                        border-radius: 6px;
                        background: #159a9c;
                        color: white;
                        font-size: 15px;
                        cursor: pointer;
                    }

                    .print-button:hover {
                        background: #117779;
                    }

                    @media print {

                        body {
                            background: white;
                        }

                        .container {
                            margin: 0;
                            max-width: none;
                            box-shadow: none;
                        }

                        .print-button {
                            display: none;
                        }

                    }

                    </style>

                    </head>

                    <body>

                    <div class="container">

                    <div class="header">

                        <h1>
                            Sunrise Dental Clinic
                        </h1>

                        <p>
                            Dental Clinic Management System
                        </p>

                        <p>
                            Professional Dental Care
                        </p>

                    </div>

                    <div class="bill-title">

                        <h2>
                            BILL / INVOICE
                        </h2>

                    </div>

                    <div class="bill-info">

                        <div class="info-box">

                            <div class="info-label">
                                Bill ID
                            </div>

                            <div class="info-value">
                    """);

            html.append(selectedBill.getId());

            html.append("""
                            </div>

                        </div>

                        <div class="info-box">

                            <div class="info-label">
                                Appointment ID
                            </div>

                            <div class="info-value">
                    """);

            html.append(
                    selectedBill.getAppointmentId()
            );

            html.append("""
                            </div>

                        </div>

                        <div class="info-box">

                            <div class="info-label">
                                Bill Date
                            </div>

                            <div class="info-value">
                    """);

            html.append(
                    selectedBill.getBillDate()
            );

            html.append("""
                            </div>

                        </div>

                    </div>

                    <table>

                        <tr>

                            <th>
                                Description
                            </th>

                            <th class="amount">
                                Amount
                            </th>

                        </tr>

                        <tr>

                            <td>
                                Consultation Fee
                            </td>

                            <td class="amount">
                    """);

            html.append(
                    selectedBill.getConsultationFee()
            );

            html.append("""
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Treatment Cost
                            </td>

                            <td class="amount">
                    """);

            html.append(
                    selectedBill.getTreatmentCost()
            );

            html.append("""
                            </td>

                        </tr>

                        <tr class="total-row">

                            <td>
                                TOTAL AMOUNT
                            </td>

                            <td class="amount">
                    """);

            html.append(
                    selectedBill.getTotalAmount()
            );

            html.append("""
                            </td>

                        </tr>

                    </table>

                    <div class="footer">

                        <p>
                            Thank you for choosing
                            Sunrise Dental Clinic.
                        </p>

                        <p>
                            Please keep this bill
                            for your records.
                        </p>

                    </div>

                    <button
                        class="print-button"
                        onclick="window.print()">

                        Print Bill

                    </button>

                    </div>

                    </body>
                    </html>
                    """);

            response.getWriter()
                    .write(html.toString());

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load bill.",
                    e
            );
        }
    }
}