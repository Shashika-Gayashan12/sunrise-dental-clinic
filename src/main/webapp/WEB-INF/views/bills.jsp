```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.sunrise.dentalclinic.entity.User" %>
<%@ page import="com.sunrise.dentalclinic.entity.Bill" %>
<%@ page import="com.sunrise.dentalclinic.entity.AppointmentBillingInfo" %>
<%@ page import="com.sunrise.dentalclinic.entity.Appointment" %>
<%@ page import="com.sunrise.dentalclinic.entity.Patient" %>
<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>
<%@ page import="com.sunrise.dentalclinic.entity.Treatment" %>

<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.LocalDate" %>

<%
    User loggedInUser =
            (User) session.getAttribute("loggedInUser");

    List<Bill> bills =
            (List<Bill>) request.getAttribute("bills");

    List<AppointmentBillingInfo> dateAppointments =
            (List<AppointmentBillingInfo>) request.getAttribute(
                    "dateAppointments"
            );

    LocalDate selectedDate =
            (LocalDate) request.getAttribute(
                    "selectedDate"
            );

    Appointment loadedAppointment =
            (Appointment) request.getAttribute(
                    "loadedAppointment"
            );

    Patient loadedPatient =
            (Patient) request.getAttribute(
                    "loadedPatient"
            );

    Dentist loadedDentist =
            (Dentist) request.getAttribute(
                    "loadedDentist"
            );

    Treatment loadedTreatment =
            (Treatment) request.getAttribute(
                    "loadedTreatment"
            );

    BigDecimal totalRevenue =
            (BigDecimal) request.getAttribute(
                    "totalRevenue"
            );

    String error =
            (String) request.getAttribute("error");

    String contextPath =
            request.getContextPath();
%>

<!DOCTYPE html>

<html lang="en">

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
            margin: 0;
            padding: 0;
        }

        body {
            font-family:
                    Arial,
                    Helvetica,
                    sans-serif;

            background: #f4f7f9;

            color: #263238;
        }


        /* =====================================================
           SIDEBAR
           ===================================================== */

        .sidebar {
            position: fixed;

            left: 0;
            top: 0;

            width: 250px;
            height: 100vh;

            background:
                    linear-gradient(
                            180deg,
                            #0b3448,
                            #0f3d56,
                            #0a3145
                    );

            color: white;

            padding: 25px 15px;

            display: flex;
            flex-direction: column;

            z-index: 1000;
        }

        .brand {
            text-align: center;

            padding: 5px 0 28px;
        }

        .brand h2 {
            font-size: 22px;

            font-weight: 700;

            margin-bottom: 5px;
        }

        .brand p {
            font-size: 12px;

            color: #a9c7d4;
        }

        .nav {
            flex: 1;
        }

        .nav-item {
            height: 43px;

            display: flex;

            align-items: center;

            gap: 13px;

            padding: 0 13px;

            margin-bottom: 5px;

            border-radius: 9px;

            white-space: nowrap;

            color: #d6e7ee;

            text-decoration: none;

            font-size: 14px;

            transition: 0.2s;
        }

        .nav-item:hover {
            background: rgba(255,255,255,0.09);

            color: white;
        }

        .nav-item.active {
            background: #159a9c;

            color: white;

            font-weight: 600;
        }

        .nav-icon {
            width: 25px;

            min-width: 25px;

            height: 25px;

            display: flex;

            align-items: center;

            justify-content: center;

            flex-shrink: 0;
        }

        .nav-icon svg {
            width: 18px;

            height: 18px;

            display: block;

            stroke: currentColor;

            fill: none;

            stroke-width: 1.8;

            stroke-linecap: round;

            stroke-linejoin: round;
        }

        .sidebar-bottom {
            border-top: 1px solid
                        rgba(255,255,255,0.1);

            padding-top: 15px;
        }

        .user-box {
            padding: 10px 12px;

            margin-bottom: 10px;
        }

        .user-name {
            font-size: 14px;

            font-weight: 600;

            color: white;

            margin-bottom: 3px;
        }

        .user-role {
            font-size: 11px;

            color: #9ebdca;
        }

        .logout {
            display: flex;

            align-items: center;

            gap: 10px;

            height: 40px;

            padding: 0 13px;

            border-radius: 8px;

            color: #d6e7ee;

            text-decoration: none;

            font-size: 14px;
        }

        .logout:hover {
            background: rgba(255,255,255,0.08);

            color: white;
        }


        /* =====================================================
           MAIN
           ===================================================== */

        .main {
            margin-left: 250px;

            min-height: 100vh;

            padding: 28px 32px;
        }

        .topbar {
            display: flex;

            align-items: center;

            justify-content: space-between;

            margin-bottom: 25px;
        }

        .topbar h1 {
            font-size: 27px;

            color: #173c4e;

            margin-bottom: 5px;
        }

        .topbar p {
            font-size: 13px;

            color: #78909c;
        }

        .top-action {
            background: #159a9c;

            color: white;

            padding: 10px 17px;

            border-radius: 8px;

            font-size: 13px;

            font-weight: 600;
        }


        /* =====================================================
           ALERT
           ===================================================== */

        .alert {
            background: #fff1f1;

            border: 1px solid #f1b8b8;

            color: #b42318;

            border-radius: 9px;

            padding: 13px 16px;

            margin-bottom: 20px;

            font-size: 13px;
        }


        /* =====================================================
           STATS
           ===================================================== */

        .stats {
            display: grid;

            grid-template-columns:
                    repeat(3, 1fr);

            gap: 18px;

            margin-bottom: 24px;
        }

        .stat-card {
            background: white;

            border-radius: 12px;

            padding: 20px;

            border: 1px solid #e5ecef;

            box-shadow:
                    0 3px 12px
                    rgba(16,42,55,0.05);
        }

        .stat-label {
            font-size: 12px;

            color: #78909c;

            margin-bottom: 8px;
        }

        .stat-value {
            font-size: 24px;

            font-weight: 700;

            color: #123c50;
        }

        .stat-sub {
            font-size: 11px;

            color: #159a9c;

            margin-top: 5px;
        }


        /* =====================================================
           CARD
           ===================================================== */

        .card {
            background: white;

            border-radius: 13px;

            border: 1px solid #e4ebee;

            box-shadow:
                    0 3px 14px
                    rgba(16,42,55,0.05);

            margin-bottom: 24px;

            overflow: hidden;
        }

        .card-header {
            padding: 19px 22px;

            border-bottom: 1px solid #edf1f3;

            display: flex;

            align-items: center;

            justify-content: space-between;
        }

        .card-header h2 {
            font-size: 17px;

            color: #183f51;
        }

        .card-header p {
            font-size: 12px;

            color: #81939c;

            margin-top: 4px;
        }

        .card-body {
            padding: 22px;
        }


        /* =====================================================
           FORM
           ===================================================== */

        .form-grid {
            display: grid;

            grid-template-columns:
                    repeat(2, 1fr);

            gap: 17px 20px;
        }

        .form-group {
            display: flex;

            flex-direction: column;

            gap: 7px;
        }

        .form-group.full {
            grid-column: 1 / -1;
        }

        .form-group label {
            font-size: 12px;

            font-weight: 600;

            color: #506570;
        }

        input,
        select {
            width: 100%;

            height: 43px;

            border: 1px solid #d6e1e5;

            border-radius: 8px;

            padding: 0 13px;

            font-size: 13px;

            color: #263238;

            background: white;

            outline: none;
        }

        input:focus,
        select:focus {
            border-color: #159a9c;

            box-shadow:
                    0 0 0 3px
                    rgba(21,154,156,0.10);
        }

        input[readonly] {
            background: #f5f8f9;

            color: #536872;
        }

        .btn {
            height: 43px;

            border: none;

            border-radius: 8px;

            padding: 0 20px;

            font-size: 13px;

            font-weight: 600;

            cursor: pointer;

            transition: 0.2s;
        }

        .btn-primary {
            background: #159a9c;

            color: white;
        }

        .btn-primary:hover {
            background: #118789;
        }

        .btn-secondary {
            background: #edf4f6;

            color: #315363;
        }

        .btn-secondary:hover {
            background: #e1ecef;
        }

        .button-row {
            display: flex;

            gap: 10px;

            margin-top: 20px;
        }


        /* =====================================================
           APPOINTMENT SELECT AREA
           ===================================================== */

        .selection-box {
            background: #f6fafb;

            border: 1px solid #e0eaed;

            border-radius: 10px;

            padding: 17px;

            margin-bottom: 20px;
        }

        .selection-title {
            font-size: 13px;

            font-weight: 700;

            color: #174256;

            margin-bottom: 13px;
        }

        .selection-grid {
            display: grid;

            grid-template-columns:
                    1fr 1.5fr auto;

            gap: 12px;

            align-items: end;
        }

        .selection-grid .form-group {
            min-width: 0;
        }

        .selection-grid .btn {
            white-space: nowrap;
        }

        .empty-message {
            padding: 15px;

            background: #fffaf0;

            border: 1px solid #f1dfb1;

            color: #866a25;

            border-radius: 8px;

            font-size: 12px;

            margin-top: 12px;
        }


        /* =====================================================
           APPOINTMENT DETAILS
           ===================================================== */

        .details-title {
            font-size: 14px;

            font-weight: 700;

            color: #183f51;

            margin-bottom: 15px;
        }

        .details-grid {
            display: grid;

            grid-template-columns:
                    repeat(3, 1fr);

            gap: 14px;
        }

        .detail-box {
            background: #f7fafb;

            border: 1px solid #e2eaed;

            border-radius: 9px;

            padding: 13px 14px;

            min-height: 66px;
        }

        .detail-label {
            display: block;

            font-size: 10px;

            color: #80939d;

            margin-bottom: 6px;

            text-transform: uppercase;

            letter-spacing: 0.4px;
        }

        .detail-value {
            display: block;

            font-size: 13px;

            font-weight: 600;

            color: #254b5c;

            word-break: break-word;
        }


        /* =====================================================
           BILLING AREA
           ===================================================== */

        .billing-box {
            margin-top: 22px;

            border-top: 1px solid #edf1f3;

            padding-top: 20px;
        }

        .billing-grid {
            display: grid;

            grid-template-columns:
                    repeat(3, 1fr);

            gap: 15px;
        }

        .total-box {
            background: #eaf8f8;

            border: 1px solid #c7e9e9;

            border-radius: 9px;

            padding: 12px 14px;
        }

        .total-box .detail-label {
            color: #478487;
        }

        .total-value {
            font-size: 18px;

            font-weight: 700;

            color: #087879;
        }


        /* =====================================================
           TABLE
           ===================================================== */

        .table-wrap {
            overflow-x: auto;
        }

        table {
            width: 100%;

            border-collapse: collapse;
        }

        th {
            text-align: left;

            background: #f6f9fa;

            color: #617680;

            font-size: 11px;

            text-transform: uppercase;

            letter-spacing: 0.4px;

            padding: 13px 14px;

            border-bottom: 1px solid #e4ecef;
        }

        td {
            padding: 14px;

            border-bottom: 1px solid #edf1f3;

            font-size: 12px;

            color: #405761;

            vertical-align: middle;
        }

        tbody tr:hover {
            background: #fafcfc;
        }

        .bill-id {
            font-weight: 700;

            color: #173f52;
        }

        .appointment-number {
            font-weight: 600;

            color: #159a9c;
        }

        .amount {
            font-weight: 600;

            color: #304f5d;
        }

        .total-amount {
            font-weight: 700;

            color: #0c7779;
        }

        .print-btn {
            display: inline-flex;

            align-items: center;

            justify-content: center;

            height: 32px;

            padding: 0 12px;

            background: #edf5f6;

            color: #17666c;

            border-radius: 7px;

            text-decoration: none;

            font-size: 11px;

            font-weight: 600;
        }

        .print-btn:hover {
            background: #dceced;
        }

        .no-data {
            text-align: center;

            padding: 35px 20px;

            color: #8b9aa1;

            font-size: 13px;
        }


        /* =====================================================
           RESPONSIVE
           ===================================================== */

        @media (max-width: 1050px) {

            .stats {
                grid-template-columns:
                        1fr 1fr;
            }

            .stats .stat-card:last-child {
                grid-column: 1 / -1;
            }

            .details-grid {
                grid-template-columns:
                        repeat(2, 1fr);
            }

            .selection-grid {
                grid-template-columns:
                        1fr 1fr;
            }

            .selection-grid .btn {
                width: 100%;
            }
        }


        @media (max-width: 800px) {

            .sidebar {
                width: 210px;
            }

            .main {
                margin-left: 210px;

                padding: 22px 18px;
            }

            .form-grid,
            .billing-grid {
                grid-template-columns: 1fr;
            }

            .form-group.full {
                grid-column: auto;
            }
        }


        @media (max-width: 650px) {

            .sidebar {
                position: relative;

                width: 100%;

                height: auto;
            }

            .main {
                margin-left: 0;
            }

            .stats {
                grid-template-columns: 1fr;
            }

            .stats .stat-card:last-child {
                grid-column: auto;
            }

            .details-grid {
                grid-template-columns: 1fr;
            }

            .selection-grid {
                grid-template-columns: 1fr;
            }

            .topbar {
                flex-direction: column;

                align-items: flex-start;

                gap: 10px;
            }
        }

    </style>

</head>


<body>


<!-- =========================================================
     SIDEBAR
     ========================================================= -->

<aside class="sidebar">

    <div class="brand">

        <h2>Sunrise Dental</h2>

        <p>Clinic Management System</p>

    </div>


    <nav class="nav">


        <!-- Dashboard -->

        <a href="<%= contextPath %>/dashboard"
           class="nav-item">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <rect x="3"
                          y="3"
                          width="7"
                          height="7"/>

                    <rect x="14"
                          y="3"
                          width="7"
                          height="7"/>

                    <rect x="3"
                          y="14"
                          width="7"
                          height="7"/>

                    <rect x="14"
                          y="14"
                          width="7"
                          height="7"/>

                </svg>

            </span>

            Dashboard

        </a>


        <!-- Patients -->

        <a href="<%= contextPath %>/patients"
           class="nav-item">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <circle cx="9"
                            cy="8"
                            r="3"/>

                    <path d="M3 20c0-3.2
                             2.5-5 6-5
                             s6 1.8 6 5"/>

                    <path d="M16 11c2.2
                             0 4 1.4
                             4 4"/>

                </svg>

            </span>

            Patients

        </a>


        <!-- Appointments -->

        <a href="<%= contextPath %>/appointments"
           class="nav-item">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <rect x="3"
                          y="4"
                          width="18"
                          height="17"
                          rx="2"/>

                    <line x1="8"
                          y1="2"
                          x2="8"
                          y2="6"/>

                    <line x1="16"
                          y1="2"
                          x2="16"
                          y2="6"/>

                    <line x1="3"
                          y1="10"
                          x2="21"
                          y2="10"/>

                </svg>

            </span>

            Appointments

        </a>


        <!-- Dentists -->

        <a href="<%= contextPath %>/dentists"
           class="nav-item">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <circle cx="12"
                            cy="7"
                            r="3"/>

                    <path d="M5 21
                             c0-4
                             2.8-6
                             7-6
                             s7 2
                             7 6"/>

                </svg>

            </span>

            Dentists

        </a>


        <!-- Treatments -->

        <a href="<%= contextPath %>/treatments"
           class="nav-item">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <path d="M12 3
                             v18"/>

                    <path d="M3 12
                             h18"/>

                    <rect x="5"
                          y="5"
                          width="14"
                          height="14"
                          rx="3"/>

                </svg>

            </span>

            Treatments

        </a>


        <!-- Billing -->

        <a href="<%= contextPath %>/bills"
           class="nav-item active">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <rect x="4"
                          y="3"
                          width="16"
                          height="18"
                          rx="2"/>

                    <line x1="8"
                          y1="8"
                          x2="16"
                          y2="8"/>

                    <line x1="8"
                          y1="12"
                          x2="16"
                          y2="12"/>

                    <line x1="8"
                          y1="16"
                          x2="13"
                          y2="16"/>

                </svg>

            </span>

            Billing

        </a>


        <!-- Manage Users -->

        <%
            if (loggedInUser != null &&
                    "ADMIN".equalsIgnoreCase(
                            loggedInUser.getRole()
                    )) {
        %>

        <a href="<%= contextPath %>/manage-users"
           class="nav-item">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <circle cx="9"
                            cy="8"
                            r="3"/>

                    <path d="M3 20
                             c0-3
                             2.5-5
                             6-5"/>

                    <circle cx="17"
                            cy="9"
                            r="2"/>

                    <path d="M14 20
                             c0-2.5
                             1.5-4
                             4-4
                             2 0
                             3 1
                             3 3"/>

                </svg>

            </span>

            Manage Users

        </a>

        <%
            }
        %>

    </nav>


    <div class="sidebar-bottom">

        <div class="user-box">

            <div class="user-name">

                <%
                    if (loggedInUser != null) {
                        out.print(
                                loggedInUser.getUsername()
                        );
                    }
                %>

            </div>

            <div class="user-role">

                <%
                    if (loggedInUser != null) {
                        out.print(
                                loggedInUser.getRole()
                        );
                    }
                %>

            </div>

        </div>


        <a href="<%= contextPath %>/logout"
           class="logout">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <path d="M10 17l5-5-5-5"/>

                    <path d="M15 12H3"/>

                    <path d="M21 3v18"/>

                </svg>

            </span>

            Logout

        </a>

    </div>

</aside>



<!-- =========================================================
     MAIN
     ========================================================= -->

<main class="main">


    <div class="topbar">

        <div>

            <h1>Billing</h1>

            <p>
                Create and manage patient bills
            </p>

        </div>


        <div class="top-action">

            Billing Management

        </div>

    </div>



    <!-- =====================================================
         ERROR
         ===================================================== -->

    <%
        if (error != null &&
                !error.isBlank()) {
    %>

    <div class="alert">

        <%= error %>

    </div>

    <%
        }
    %>



    <!-- =====================================================
         STATISTICS
         ===================================================== -->

    <div class="stats">


        <div class="stat-card">

            <div class="stat-label">
                Total Bills
            </div>

            <div class="stat-value">

                <%= bills != null
                        ? bills.size()
                        : 0 %>

            </div>

            <div class="stat-sub">
                Generated bills
            </div>

        </div>


        <div class="stat-card">

            <div class="stat-label">
                Total Revenue
            </div>

            <div class="stat-value">

                Rs.
                <%= totalRevenue != null
                        ? totalRevenue
                        : "0.00" %>

            </div>

            <div class="stat-sub">
                All generated bills
            </div>

        </div>


        <div class="stat-card">

            <div class="stat-label">
                Billing System
            </div>

            <div class="stat-value">
                Active
            </div>

            <div class="stat-sub">
                Ready for billing
            </div>

        </div>


    </div>



    <!-- =====================================================
         CREATE BILL
         ===================================================== -->

    <div class="card">


        <div class="card-header">

            <div>

                <h2>Create New Bill</h2>

                <p>
                    Select an appointment and create its bill
                </p>

            </div>

        </div>


        <div class="card-body">


            <!-- =================================================
                 STEP 1
                 ================================================= -->

            <div class="selection-box">

                <div class="selection-title">

                    Step 1 — Select Appointment Date

                </div>


                <form method="get"
                      action="<%= contextPath %>/bills">

                    <input type="hidden"
                           name="action"
                           value="loadAppointments">


                    <div class="selection-grid">


                        <div class="form-group">

                            <label for="appointmentDate">

                                Appointment Date

                            </label>


                            <input type="date"
                                   id="appointmentDate"
                                   name="appointmentDate"
                                   value="<%= selectedDate != null
                                           ? selectedDate
                                           : "" %>"
                                   required>

                        </div>


                        <div></div>


                        <button type="submit"
                                class="btn btn-primary">

                            Load Appointments

                        </button>


                    </div>

                </form>


                <%
                    if (selectedDate != null &&
                            dateAppointments != null) {
                %>


                <%
                    if (dateAppointments.isEmpty()) {
                %>

                <div class="empty-message">

                    No appointments found for

                    <strong>
                        <%= selectedDate %>
                    </strong>.

                </div>

                <%
                    }
                %>


                <%
                    }
                %>

            </div>



            <!-- =================================================
                 STEP 2
                 ================================================= -->

            <%
                if (dateAppointments != null &&
                        !dateAppointments.isEmpty()) {
            %>

            <div class="selection-box">

                <div class="selection-title">

                    Step 2 — Select Appointment

                </div>


                <form method="get"
                      action="<%= contextPath %>/bills">


                    <input type="hidden"
                           name="action"
                           value="loadAppointment">


                    <input type="hidden"
                           name="appointmentDate"
                           value="<%= selectedDate %>">


                    <div class="selection-grid">


                        <div class="form-group"
                             style="grid-column: 1 / 3;">

                            <label for="appointmentId">

                                Appointment

                            </label>


                            <select id="appointmentId"
                                    name="appointmentId"
                                    required>

                                <option value="">

                                    Select an appointment

                                </option>


                                <%
                                    for (
                                        AppointmentBillingInfo appointment :
                                            dateAppointments
                                    ) {
                                %>


                                <option
                                        value="<%= appointment.getAppointmentId() %>"
                                        <%= loadedAppointment != null &&
                                            loadedAppointment.getId()
                                                .equals(
                                                    appointment.getAppointmentId()
                                                )
                                                ? "selected"
                                                : "" %>>

                                    <%= appointment.getAppointmentNumber() %>
                                    -
                                    <%= appointment.getPatientName() %>

                                </option>


                                <%
                                    }
                                %>


                            </select>

                        </div>


                        <button type="submit"
                                class="btn btn-primary">

                            Load Appointment

                        </button>


                    </div>

                </form>

            </div>

            <%
                }
            %>



            <!-- =================================================
                 STEP 3
                 ================================================= -->

            <%
                if (loadedAppointment != null &&
                        loadedPatient != null &&
                        loadedDentist != null &&
                        loadedTreatment != null) {
            %>


            <div class="details-title">

                Appointment Details

            </div>


            <div class="details-grid">


                <div class="detail-box">

                    <span class="detail-label">
                        Appointment Number
                    </span>

                    <span class="detail-value">

                        <%= loadedAppointment
                                .getAppointmentNumber() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Appointment Date
                    </span>

                    <span class="detail-value">

                        <%= loadedAppointment
                                .getAppointmentDate() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Appointment Time
                    </span>

                    <span class="detail-value">

                        <%= loadedAppointment
                                .getAppointmentTime() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Patient Name
                    </span>

                    <span class="detail-value">

                        <%= loadedPatient
                                .getPatientName() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Patient Contact
                    </span>

                    <span class="detail-value">

                        <%= loadedPatient
                                .getContactNumber() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Dentist
                    </span>

                    <span class="detail-value">

                        <%= loadedDentist
                                .getDentistName() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Specialization
                    </span>

                    <span class="detail-value">

                        <%= loadedDentist
                                .getSpecialization() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Treatment
                    </span>

                    <span class="detail-value">

                        <%= loadedTreatment
                                .getTreatmentName() %>

                    </span>

                </div>


                <div class="detail-box">

                    <span class="detail-label">
                        Treatment Cost
                    </span>

                    <span class="detail-value">

                        Rs.
                        <%= loadedTreatment
                                .getTreatmentCost() %>

                    </span>

                </div>


            </div>



            <!-- =================================================
                 BILLING
                 ================================================= -->

            <div class="billing-box">


                <div class="details-title">

                    Billing Information

                </div>


                <form method="post"
                      action="<%= contextPath %>/bills"
                      id="billForm">


                    <input type="hidden"
                           name="action"
                           value="createBill">


                    <input type="hidden"
                           name="appointmentId"
                           value="<%= loadedAppointment.getId() %>">


                    <div class="billing-grid">


                        <div class="form-group">

                            <label for="consultationFee">

                                Consultation Fee

                            </label>


                            <input type="number"
                                   id="consultationFee"
                                   name="consultationFee"
                                   min="0"
                                   step="0.01"
                                   placeholder="Enter consultation fee"
                                   required
                                   oninput="calculateTotal()">

                        </div>


                        <div class="detail-box">

                            <span class="detail-label">

                                Treatment Cost

                            </span>


                            <span class="detail-value"
                                  id="treatmentCostDisplay">

                                Rs.
                                <%= loadedTreatment
                                        .getTreatmentCost() %>

                            </span>

                        </div>


                        <div class="total-box">

                            <span class="detail-label">

                                Estimated Total

                            </span>


                            <span class="total-value"
                                  id="totalDisplay">

                                Rs. 0.00

                            </span>

                        </div>


                    </div>


                    <div class="button-row">


                        <button type="submit"
                                class="btn btn-primary">

                            Create Bill

                        </button>


                        <a href="<%= contextPath %>/bills"
                           class="btn btn-secondary"
                           style="
                               display:flex;
                               align-items:center;
                               text-decoration:none;
                           ">

                            Clear

                        </a>


                    </div>


                </form>

            </div>


            <%
                }
            %>


        </div>

    </div>



    <!-- =====================================================
         GENERATED BILLS
         ===================================================== -->

    <div class="card">


        <div class="card-header">

            <div>

                <h2>Generated Bills</h2>

                <p>
                    View and print generated patient bills
                </p>

            </div>

        </div>


        <div class="table-wrap">


            <table>


                <thead>

                <tr>

                    <th>
                        Bill ID
                    </th>

                    <th>
                        Appointment
                    </th>

                    <th>
                        Bill Date
                    </th>

                    <th>
                        Consultation
                    </th>

                    <th>
                        Treatment
                    </th>

                    <th>
                        Total
                    </th>

                    <th>
                        Action
                    </th>

                </tr>

                </thead>


                <tbody>


                <%
                    if (bills != null &&
                            !bills.isEmpty()) {

                        for (Bill bill : bills) {
                %>


                <tr>


                    <td>

                        <span class="bill-id">

                            #<%= bill.getId() %>

                        </span>

                    </td>


                    <td>

                        <span class="appointment-number">

                            #<%= bill.getAppointmentId() %>

                        </span>

                    </td>


                    <td>

                        <%= bill.getBillDate() %>

                    </td>


                    <td>

                        <span class="amount">

                            Rs.
                            <%= bill.getConsultationFee() %>

                        </span>

                    </td>


                    <td>

                        <span class="amount">

                            Rs.
                            <%= bill.getTreatmentCost() %>

                        </span>

                    </td>


                    <td>

                        <span class="total-amount">

                            Rs.
                            <%= bill.getTotalAmount() %>

                        </span>

                    </td>


                    <td>

                        <a class="print-btn"
                           href="<%= contextPath %>/printBill?id=<%= bill.getId() %>"
                           target="_blank">

                            Print Bill

                        </a>

                    </td>


                </tr>


                <%
                        }

                    } else {
                %>


                <tr>

                    <td colspan="7"
                        class="no-data">

                        No bills have been generated yet.

                    </td>

                </tr>


                <%
                    }
                %>


                </tbody>


            </table>

        </div>

    </div>


</main>



<script>

    function calculateTotal() {

        const consultationInput =
                document.getElementById(
                        "consultationFee"
                );

        const totalDisplay =
                document.getElementById(
                        "totalDisplay"
                );


        if (!consultationInput ||
                !totalDisplay) {

            return;
        }


        const consultation =
                parseFloat(
                        consultationInput.value
                ) || 0;


        const treatment =
                <%= loadedTreatment != null
                        ? loadedTreatment.getTreatmentCost()
                        : "0" %>;


        const total =
                consultation + treatment;


        totalDisplay.textContent =
                "Rs. " +
                total.toFixed(2);
    }

</script>


</body>

</html>
```
