<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="com.sunrise.dentalclinic.entity.Appointment" %>

<%
    List<Appointment> appointments =
            (List<Appointment>) request.getAttribute("appointments");

    String error =
            (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>My Bookings - Sunrise Dental Clinic</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f5f7fa;
            color: #1f2937;
        }

        .main {
            margin-left: 250px;
            padding: 35px;
            min-height: 100vh;
        }

        /* PAGE HEADER */

        .page-header {
            margin-bottom: 30px;
        }

        .page-header h1 {
            margin: 0;
            font-size: 30px;
            color: #123b3a;
        }

        .page-header p {
            margin-top: 8px;
            color: #6b7280;
            font-size: 15px;
        }


        /* ERROR */

        .error-box {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #b91c1c;
            padding: 15px 18px;
            border-radius: 10px;
            margin-bottom: 20px;
        }


        /* CARD */

        .booking-card {
            background: white;
            border-radius: 16px;
            padding: 25px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
            overflow-x: auto;
        }


        /* TABLE */

        .table {
            width: 100%;
            border-collapse: collapse;
            min-width: 900px;
        }

        .table th {
            text-align: left;
            padding: 15px;
            background: #f8fafc;
            color: #475569;
            font-size: 12px;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.4px;
            border-bottom: 1px solid #e5e7eb;
        }

        .table td {
            padding: 16px 15px;
            border-bottom: 1px solid #eef0f2;
            font-size: 14px;
            vertical-align: middle;
        }

        .table tr:last-child td {
            border-bottom: none;
        }

        .table tbody tr:hover {
            background: #fafdfc;
        }


        /* APPOINTMENT NUMBER */

        .appointment-number {
            font-weight: 700;
            color: #0f766e;
            white-space: nowrap;
        }


        /* DATE / TIME */

        .date-value {
            font-weight: 600;
            color: #374151;
        }

        .time-value {
            font-weight: 600;
            color: #374151;
        }


        /* IDS */

        .id-value {
            color: #4b5563;
            font-weight: 500;
        }


        /* STATUS */

        .status {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 700;
            white-space: nowrap;
        }

        .status-pending {
            background: #fff7ed;
            color: #c2410c;
        }

        .status-confirmed {
            background: #ecfdf5;
            color: #15803d;
        }

        .status-completed {
            background: #eff6ff;
            color: #1d4ed8;
        }

        .status-cancelled {
            background: #fef2f2;
            color: #b91c1c;
        }

        .status-unknown {
            background: #f3f4f6;
            color: #4b5563;
        }


        /* VIEW DETAILS BUTTON */

        .view-btn {
            display: inline-block;
            padding: 8px 15px;
            background: #0f766e;
            color: white;
            text-decoration: none;
            border-radius: 7px;
            font-size: 13px;
            font-weight: 600;
            white-space: nowrap;
            transition: 0.2s ease;
        }

        .view-btn:hover {
            background: #115e59;
            transform: translateY(-1px);
        }


        /* EMPTY STATE */

        .empty-state {
            text-align: center;
            padding: 65px 20px;
            color: #6b7280;
        }

        .empty-icon {
            width: 65px;
            height: 65px;
            margin: 0 auto 20px;
            border-radius: 50%;
            background: #f0fdfa;
            color: #0f766e;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 27px;
            font-weight: bold;
        }

        .empty-state h3 {
            margin: 0 0 8px;
            color: #374151;
            font-size: 20px;
        }

        .empty-state p {
            margin: 0;
            font-size: 14px;
        }


        /* SUMMARY */

        .summary {
            margin-top: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: #6b7280;
            font-size: 13px;
        }

        .summary strong {
            color: #123b3a;
        }


        /* RESPONSIVE */

        @media (max-width: 900px) {

            .main {
                margin-left: 0;
                padding: 25px;
            }

        }

    </style>

</head>

<body>

    <jsp:include page="dentist-sidebar.jsp" />


    <div class="main">

        <!-- PAGE HEADER -->

        <div class="page-header">

            <h1>My Bookings</h1>

            <p>
                View and manage appointments assigned to you.
            </p>

        </div>


        <!-- ERROR MESSAGE -->

        <% if (error != null) { %>

            <div class="error-box">
                <%= error %>
            </div>

        <% } %>


        <!-- BOOKINGS CARD -->

        <div class="booking-card">

            <% if (appointments == null ||
                    appointments.isEmpty()) { %>


                <!-- EMPTY STATE -->

                <div class="empty-state">

                    <div class="empty-icon">
                        A
                    </div>

                    <h3>
                        No Bookings Found
                    </h3>

                    <p>
                        You currently have no appointments assigned to you.
                    </p>

                </div>


            <% } else { %>


                <!-- BOOKINGS TABLE -->

                <table class="table">

                    <thead>

                    <tr>

                        <th>
                            Appointment
                        </th>

                        <th>
                            Date
                        </th>

                        <th>
                            Time
                        </th>

                        <th>
                            Patient ID
                        </th>

                        <th>
                            Treatment ID
                        </th>

                        <th>
                            Status
                        </th>

                        <th>
                            Action
                        </th>

                    </tr>

                    </thead>


                    <tbody>

                    <% for (Appointment appointment :
                            appointments) { %>

                        <tr>


                            <!-- APPOINTMENT NUMBER -->

                            <td>

                                <div class="appointment-number">

                                    <%
                                        String appointmentNumber =
                                                appointment.getAppointmentNumber();

                                        if (appointmentNumber == null ||
                                                appointmentNumber.trim().isEmpty()) {
                                    %>

                                        #<%= appointment.getId() %>

                                    <%
                                        } else {
                                    %>

                                        <%= appointmentNumber %>

                                    <%
                                        }
                                    %>

                                </div>

                            </td>


                            <!-- DATE -->

                            <td>

                                <span class="date-value">

                                    <%
                                        if (appointment.getAppointmentDate() != null) {
                                    %>

                                        <%= appointment.getAppointmentDate() %>

                                    <%
                                        } else {
                                    %>

                                        -

                                    <%
                                        }
                                    %>

                                </span>

                            </td>


                            <!-- TIME -->

                            <td>

                                <span class="time-value">

                                    <%
                                        if (appointment.getAppointmentTime() != null) {
                                    %>

                                        <%= appointment.getAppointmentTime() %>

                                    <%
                                        } else {
                                    %>

                                        -

                                    <%
                                        }
                                    %>

                                </span>

                            </td>


                            <!-- PATIENT ID -->

                            <td>

                                <span class="id-value">

                                    <%
                                        if (appointment.getPatientId() != null) {
                                    %>

                                        #<%= appointment.getPatientId() %>

                                    <%
                                        } else {
                                    %>

                                        -

                                    <%
                                        }
                                    %>

                                </span>

                            </td>


                            <!-- TREATMENT ID -->

                            <td>

                                <span class="id-value">

                                    <%
                                        if (appointment.getTreatmentId() != null) {
                                    %>

                                        #<%= appointment.getTreatmentId() %>

                                    <%
                                        } else {
                                    %>

                                        -

                                    <%
                                        }
                                    %>

                                </span>

                            </td>


                            <!-- STATUS -->

                            <td>

                                <%
                                    String status =
                                            appointment.getStatus();

                                    if (status == null ||
                                            status.trim().isEmpty()) {

                                        status = "UNKNOWN";
                                    }

                                    String statusClass =
                                            "status-unknown";

                                    if ("PENDING".equalsIgnoreCase(status)) {

                                        statusClass =
                                                "status-pending";

                                    } else if ("CONFIRMED".equalsIgnoreCase(status)) {

                                        statusClass =
                                                "status-confirmed";

                                    } else if ("COMPLETED".equalsIgnoreCase(status)) {

                                        statusClass =
                                                "status-completed";

                                    } else if ("CANCELLED".equalsIgnoreCase(status)) {

                                        statusClass =
                                                "status-cancelled";
                                    }
                                %>


                                <span class="status <%= statusClass %>">

                                    <%= status %>

                                </span>

                            </td>


                            <!-- VIEW DETAILS -->

                            <td>

                                <a
                                        href="<%= request.getContextPath() %>/dentist-appointment-details?id=<%= appointment.getId() %>"
                                        class="view-btn">

                                    View Details

                                </a>

                            </td>


                        </tr>

                    <% } %>

                    </tbody>

                </table>


                <!-- SUMMARY -->

                <div class="summary">

                    <span>
                        Total appointments:
                        <strong>
                            <%= appointments.size() %>
                        </strong>
                    </span>

                    <span>
                        Click <strong>View Details</strong> to see complete appointment information.
                    </span>

                </div>


            <% } %>

        </div>

    </div>

</body>

</html>