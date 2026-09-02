<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.sunrise.dentalclinic.entity.BillDetails" %>

<%
BillDetails bill =
(BillDetails) request.getAttribute("billDetails");


String contextPath =
        request.getContextPath();

if (bill == null) {
    response.sendRedirect(
            contextPath + "/bills"
    );
    return;
}


%>

<!DOCTYPE html>

<html lang="en">

<head>


<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width,
               initial-scale=1.0">

<title>
    Bill #<%= bill.getBillId() %> -
    Sunrise Dental Clinic
</title>

<style>

    * {
        box-sizing: border-box;
    }

    body {
        margin: 0;
        padding: 40px 20px;
        background: #eef3f6;
        color: #263746;
        font-family:
            Arial,
            Helvetica,
            sans-serif;
    }

    .invoice {
        width: 100%;
        max-width: 850px;
        margin: 0 auto;
        background: #ffffff;
        box-shadow:
            0 8px 30px
            rgba(0, 0, 0, 0.10);
    }

    /* ==============================
       HEADER
       ============================== */

    .invoice-header {
        padding: 35px 45px 28px;
        background:
            linear-gradient(
                135deg,
                #0b3448,
                #0f3d56
            );
        color: white;
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
    }

    .clinic-name {
        font-size: 28px;
        font-weight: 700;
        margin: 0 0 7px;
        letter-spacing: 0.2px;
    }

    .clinic-subtitle {
        margin: 0;
        font-size: 13px;
        color: #c8dbe5;
    }

    .invoice-heading {
        text-align: right;
    }

    .invoice-heading h2 {
        margin: 0;
        font-size: 25px;
        letter-spacing: 1.5px;
    }

    .invoice-heading p {
        margin: 7px 0 0;
        font-size: 13px;
        color: #c8dbe5;
    }

    /* ==============================
       CONTENT
       ============================== */

    .invoice-body {
        padding: 35px 45px 40px;
    }

    .bill-meta {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 15px;
        margin-bottom: 30px;
    }

    .meta-box {
        border: 1px solid #e2e8ed;
        border-radius: 8px;
        padding: 16px 18px;
        background: #f8fafb;
    }

    .meta-label {
        font-size: 11px;
        text-transform: uppercase;
        letter-spacing: 0.7px;
        color: #7b8791;
        margin-bottom: 6px;
    }

    .meta-value {
        font-size: 15px;
        font-weight: 600;
        color: #0f3d56;
    }

    /* ==============================
       SECTION
       ============================== */

    .section-title {
        margin: 30px 0 12px;
        padding-bottom: 8px;
        border-bottom: 2px solid #159a9c;
        color: #0f3d56;
        font-size: 15px;
        font-weight: 700;
    }

    .details-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 12px 30px;
    }

    .detail-row {
        display: flex;
        justify-content: space-between;
        gap: 15px;
        padding: 9px 0;
        border-bottom: 1px solid #edf0f2;
    }

    .detail-label {
        color: #78838c;
        font-size: 13px;
    }

    .detail-value {
        color: #263746;
        font-size: 13px;
        font-weight: 600;
        text-align: right;
    }

    /* ==============================
       CHARGES TABLE
       ============================== */

    .charges {
        margin-top: 32px;
    }

    .charges table {
        width: 100%;
        border-collapse: collapse;
    }

    .charges th {
        padding: 13px 15px;
        background: #0f3d56;
        color: white;
        font-size: 12px;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        text-align: left;
    }

    .charges th:last-child,
    .charges td:last-child {
        text-align: right;
    }

    .charges td {
        padding: 15px;
        border-bottom: 1px solid #e7ecef;
        font-size: 14px;
    }

    .charges td:last-child {
        font-weight: 600;
    }

    /* ==============================
       TOTAL
       ============================== */

    .summary {
        margin-top: 25px;
        margin-left: auto;
        width: 330px;
    }

    .summary-row {
        display: flex;
        justify-content: space-between;
        padding: 9px 0;
        font-size: 14px;
    }

    .summary-label {
        color: #6f7b84;
    }

    .summary-value {
        font-weight: 600;
        color: #263746;
    }

    .grand-total {
        margin-top: 8px;
        padding: 16px 0;
        border-top: 2px solid #159a9c;
        border-bottom: 2px solid #159a9c;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .grand-total .label {
        font-size: 16px;
        font-weight: 700;
        color: #0f3d56;
    }

    .grand-total .value {
        font-size: 21px;
        font-weight: 700;
        color: #159a9c;
    }

    /* ==============================
       FOOTER
       ============================== */

    .invoice-footer {
        margin-top: 40px;
        padding-top: 22px;
        border-top: 1px solid #e1e6e9;
        text-align: center;
    }

    .thank-you {
        margin: 0 0 7px;
        color: #0f3d56;
        font-size: 14px;
        font-weight: 600;
    }

    .footer-note {
        margin: 0;
        color: #8a949b;
        font-size: 12px;
    }

    /* ==============================
       PRINT BUTTONS
       ============================== */

    .actions {
        max-width: 850px;
        margin: 20px auto 0;
        display: flex;
        justify-content: center;
        gap: 12px;
    }

    .btn {
        border: none;
        border-radius: 7px;
        padding: 12px 25px;
        font-size: 14px;
        font-weight: 600;
        cursor: pointer;
    }

    .btn-print {
        background: #159a9c;
        color: white;
    }

    .btn-print:hover {
        background: #117779;
    }

    .btn-back {
        background: #ffffff;
        color: #0f3d56;
        border: 1px solid #d5dde2;
    }

    .btn-back:hover {
        background: #f3f6f8;
    }

    /* ==============================
       RESPONSIVE
       ============================== */

    @media (max-width: 650px) {

        body {
            padding: 15px;
        }

        .invoice-header {
            padding: 25px;
            flex-direction: column;
            gap: 20px;
        }

        .invoice-heading {
            text-align: left;
        }

        .invoice-body {
            padding: 25px;
        }

        .bill-meta,
        .details-grid {
            grid-template-columns: 1fr;
        }

        .summary {
            width: 100%;
        }
    }

    /* ==============================
       PRINT
       ============================== */

    @media print {

        @page {
            size: A4;
            margin: 12mm;
        }

        body {
            padding: 0;
            margin: 0;
            background: white;
        }

        .invoice {
            max-width: none;
            box-shadow: none;
        }

        .actions {
            display: none;
        }

        .invoice-header {
            -webkit-print-color-adjust: exact;
            print-color-adjust: exact;
        }

        .charges th {
            -webkit-print-color-adjust: exact;
            print-color-adjust: exact;
        }

        .section-title {
            break-after: avoid;
        }

        .charges {
            break-inside: avoid;
        }

        .summary {
            break-inside: avoid;
        }

        .invoice-footer {
            break-inside: avoid;
        }
    }

</style>
```

</head>

<body>

<div class="invoice">

```
<!-- ==============================
     HEADER
     ============================== -->

<div class="invoice-header">

    <div>

        <h1 class="clinic-name">
            Sunrise Dental Clinic
        </h1>

        <p class="clinic-subtitle">
            Professional Dental Care
        </p>

        <p class="clinic-subtitle">
            Clinic Management System
        </p>

    </div>

    <div class="invoice-heading">

        <h2>
            INVOICE
        </h2>

        <p>
            Bill #<%= bill.getBillId() %>
        </p>

    </div>

</div>


<!-- ==============================
     BODY
     ============================== -->

<div class="invoice-body">

    <!-- Bill Information -->

    <div class="bill-meta">

        <div class="meta-box">

            <div class="meta-label">
                Bill Number
            </div>

            <div class="meta-value">
                #<%= bill.getBillId() %>
            </div>

        </div>

        <div class="meta-box">

            <div class="meta-label">
                Bill Date
            </div>

            <div class="meta-value">
                <%= bill.getBillDate() %>
            </div>

        </div>

    </div>


    <!-- ==============================
         PATIENT INFORMATION
         ============================== -->

    <div class="section-title">
        Patient Information
    </div>

    <div class="details-grid">

        <div class="detail-row">

            <span class="detail-label">
                Patient Name
            </span>

            <span class="detail-value">
                <%= bill.getPatientName() %>
            </span>

        </div>

        <div class="detail-row">

            <span class="detail-label">
                Contact Number
            </span>

            <span class="detail-value">
                <%= bill.getPatientContact() %>
            </span>

        </div>

    </div>


    <!-- ==============================
         APPOINTMENT INFORMATION
         ============================== -->

    <div class="section-title">
        Appointment Information
    </div>

    <div class="details-grid">

        <div class="detail-row">

            <span class="detail-label">
                Appointment No
            </span>

            <span class="detail-value">
                <%= bill.getAppointmentNumber() %>
            </span>

        </div>

        <div class="detail-row">

            <span class="detail-label">
                Appointment Date
            </span>

            <span class="detail-value">
                <%= bill.getAppointmentDate() %>
            </span>

        </div>

        <div class="detail-row">

            <span class="detail-label">
                Appointment Time
            </span>

            <span class="detail-value">
                <%= bill.getAppointmentTime() %>
            </span>

        </div>

    </div>


    <!-- ==============================
         DENTIST INFORMATION
         ============================== -->

    <div class="section-title">
        Dentist Information
    </div>

    <div class="details-grid">

        <div class="detail-row">

            <span class="detail-label">
                Dentist
            </span>

            <span class="detail-value">
                <%= bill.getDentistName() %>
            </span>

        </div>

        <div class="detail-row">

            <span class="detail-label">
                Specialization
            </span>

            <span class="detail-value">
                <%= bill.getSpecialization() %>
            </span>

        </div>

    </div>


    <!-- ==============================
         TREATMENT & CHARGES
         ============================== -->

    <div class="section-title">
        Treatment & Charges
    </div>

    <div class="charges">

        <table>

            <thead>

            <tr>

                <th>
                    Description
                </th>

                <th>
                    Amount (LKR)
                </th>

            </tr>

            </thead>

            <tbody>

            <tr>

                <td>
                    Consultation Fee
                </td>

                <td>
                    <%= bill.getConsultationFee() %>
                </td>

            </tr>

            <tr>

                <td>
                    <%= bill.getTreatmentName() %>
                </td>

                <td>
                    <%= bill.getTreatmentCost() %>
                </td>

            </tr>

            </tbody>

        </table>

    </div>


    <!-- ==============================
         SUMMARY
         ============================== -->

    <div class="summary">

        <div class="summary-row">

            <span class="summary-label">
                Consultation Fee
            </span>

            <span class="summary-value">
                LKR <%= bill.getConsultationFee() %>
            </span>

        </div>

        <div class="summary-row">

            <span class="summary-label">
                Treatment Cost
            </span>

            <span class="summary-value">
                LKR <%= bill.getTreatmentCost() %>
            </span>

        </div>

        <div class="grand-total">

            <span class="label">
                GRAND TOTAL
            </span>

            <span class="value">
                LKR <%= bill.getTotalAmount() %>
            </span>

        </div>

    </div>


    <!-- ==============================
         FOOTER
         ============================== -->

    <div class="invoice-footer">

        <p class="thank-you">
            Thank you for choosing
            Sunrise Dental Clinic.
        </p>

        <p class="footer-note">
            Please keep this invoice for
            your records.
        </p>

    </div>

</div>


</div>

<!-- ==============================
     ACTION BUTTONS
     ============================== -->

<div class="actions">

<button
        type="button"
        class="btn btn-back"
        onclick="window.history.back()">

    Back

</button>

<button
        type="button"
        class="btn btn-print"
        onclick="window.print()">

    Print Bill

</button>


</div>

</body>

</html>
