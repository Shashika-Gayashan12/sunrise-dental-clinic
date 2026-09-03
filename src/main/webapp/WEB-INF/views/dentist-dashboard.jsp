<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>
<%@ page import="com.sunrise.dentalclinic.entity.Appointment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>

<%
Dentist dentist =
(Dentist) request.getAttribute("dentist");


Integer totalAppointments =
        (Integer) request.getAttribute("totalAppointments");

Integer todayAppointments =
        (Integer) request.getAttribute("todayAppointments");

Integer pendingAppointments =
        (Integer) request.getAttribute("pendingAppointments");

Integer confirmedAppointments =
        (Integer) request.getAttribute("confirmedAppointments");

Integer completedAppointments =
        (Integer) request.getAttribute("completedAppointments");

Integer cancelledAppointments =
        (Integer) request.getAttribute("cancelledAppointments");

List<Appointment> todayList =
        (List<Appointment>) request.getAttribute(
                "todayAppointmentsList"
        );

List<Appointment> upcomingAppointments =
        (List<Appointment>) request.getAttribute(
                "upcomingAppointments"
        );

String error =
        (String) request.getAttribute("error");

LocalDate today =
        (LocalDate) request.getAttribute("today");

if (totalAppointments == null) {
    totalAppointments = 0;
}

if (todayAppointments == null) {
    todayAppointments = 0;
}

if (pendingAppointments == null) {
    pendingAppointments = 0;
}

if (confirmedAppointments == null) {
    confirmedAppointments = 0;
}

if (completedAppointments == null) {
    completedAppointments = 0;
}

if (cancelledAppointments == null) {
    cancelledAppointments = 0;
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
    Dentist Dashboard - Sunrise Dental Clinic
</title>

<style>

    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    body {
        font-family:
                Arial,
                Helvetica,
                sans-serif;

        background: #f5f7fb;

        color: #1f2937;
    }

    .main {

        margin-left: 250px;

        padding: 32px 35px;

        max-width: 1500px;

        min-height: 100vh;
    }

    .welcome {

        margin-bottom: 28px;
    }

    .welcome h1 {

        font-size: 28px;

        color: #111827;

        margin-bottom: 7px;
    }

    .welcome p {

        color: #6b7280;

        font-size: 14px;
    }

    .profile-card {

        background: #ffffff;

        border-radius: 14px;

        padding: 24px;

        margin-bottom: 25px;

        border:
                1px solid #e5e7eb;

        display: flex;

        align-items: center;

        justify-content: space-between;

        gap: 20px;
    }

    .profile-left {

        display: flex;

        align-items: center;

        gap: 18px;
    }

    .doctor-avatar {

        width: 65px;

        height: 65px;

        border-radius: 50%;

        background: #159a9c;

        color: white;

        display: flex;

        align-items: center;

        justify-content: center;

        font-size: 24px;

        font-weight: bold;

        flex-shrink: 0;
    }

    .doctor-info h2 {

        font-size: 20px;

        color: #111827;

        margin-bottom: 6px;
    }

    .doctor-info p {

        color: #6b7280;

        font-size: 14px;

        margin-bottom: 4px;
    }

    .stats {

        display: grid;

        grid-template-columns:
                repeat(6, 1fr);

        gap: 18px;

        margin-bottom: 30px;
    }

    .stat-card {

        background: #ffffff;

        border:
                1px solid #e5e7eb;

        border-radius: 14px;

        padding: 20px;

        min-height: 125px;
    }

    .stat-title {

        color: #6b7280;

        font-size: 13px;

        margin-bottom: 12px;
    }

    .stat-number {

        font-size: 30px;

        font-weight: 700;

        color: #111827;
    }

    .content-grid {

        display: grid;

        grid-template-columns:
                1fr 1fr;

        gap: 25px;
    }

    .section-card {

        background: #ffffff;

        border:
                1px solid #e5e7eb;

        border-radius: 14px;

        overflow: hidden;
    }

    .section-header {

        padding: 20px 22px;

        border-bottom:
                1px solid #e5e7eb;

        display: flex;

        align-items: center;

        justify-content: space-between;
    }

    .section-header h2 {

        font-size: 17px;

        color: #111827;
    }

    .section-header span {

        font-size: 12px;

        color: #6b7280;
    }

    .appointment-list {

        padding: 8px 22px 18px;
    }

    .appointment-item {

        padding: 17px 0;

        border-bottom:
                1px solid #f0f1f3;

        display: flex;

        align-items: center;

        justify-content: space-between;

        gap: 15px;
    }

    .appointment-item:last-child {

        border-bottom: none;
    }

    .appointment-main {

        display: flex;

        flex-direction: column;

        gap: 5px;

        min-width: 0;
    }

    .appointment-number {

        font-size: 14px;

        font-weight: 700;

        color: #111827;
    }

    .appointment-details {

        font-size: 13px;

        color: #6b7280;
    }

    .appointment-date {

        font-size: 12px;

        color: #159a9c;

        font-weight: 600;
    }

    .status {

        padding: 6px 10px;

        border-radius: 20px;

        font-size: 11px;

        font-weight: 700;

        white-space: nowrap;
    }

    .status-pending {

        background: #fff7ed;

        color: #ea580c;
    }

    .status-confirmed {

        background: #eff6ff;

        color: #2563eb;
    }

    .status-completed {

        background: #ecfdf5;

        color: #059669;
    }

    .status-cancelled {

        background: #fef2f2;

        color: #dc2626;
    }

    .empty {

        padding: 35px 20px;

        text-align: center;

        color: #9ca3af;

        font-size: 14px;
    }

    .error {

        background: #fef2f2;

        border:
                1px solid #fecaca;

        color: #b91c1c;

        padding: 15px 18px;

        border-radius: 10px;

        margin-bottom: 25px;

        font-size: 14px;
    }

    @media (max-width: 1200px) {

        .stats {

            grid-template-columns:
                    repeat(3, 1fr);
        }
    }

    @media (max-width: 950px) {

        .main {

            margin-left: 0;

            padding: 28px 25px;
        }
    }

    @media (max-width: 850px) {

        .content-grid {

            grid-template-columns: 1fr;
        }

        .profile-card {

            align-items: flex-start;

            flex-direction: column;
        }
    }

    @media (max-width: 600px) {

        .main {

            padding: 25px 18px;
        }

        .stats {

            grid-template-columns:
                    repeat(2, 1fr);

            gap: 12px;
        }

        .stat-card {

            padding: 16px;

            min-height: 105px;
        }

        .stat-number {

            font-size: 25px;
        }

        .welcome h1 {

            font-size: 23px;
        }

        .appointment-item {

            align-items: flex-start;

            flex-direction: column;
        }
    }

</style>
```

</head>

<body>


<jsp:include page="dentist-sidebar.jsp" />

<main class="main">

    <div class="welcome">

        <h1>

            Welcome,
            <%= dentist != null
                    ? dentist.getDentistName()
                    : "Doctor" %>

        </h1>

        <p>
            Manage your appointments and view your daily schedule.
        </p>

    </div>


    <% if (error != null) { %>

        <div class="error">

            <%= error %>

        </div>

    <% } %>


    <% if (dentist != null) { %>

        <div class="profile-card">

            <div class="profile-left">

                <div class="doctor-avatar">
                    D
                </div>

                <div class="doctor-info">

                    <h2>
                        <%= dentist.getDentistName() %>
                    </h2>

                    <p>

                        <strong>
                            Specialization:
                        </strong>

                        <%= dentist.getSpecialization() %>

                    </p>

                    <p>

                        <strong>
                            Contact:
                        </strong>

                        <%= dentist.getContactNumber() == null ||
                            dentist.getContactNumber().isBlank()
                                ? "Not provided"
                                : dentist.getContactNumber() %>

                    </p>

                </div>

            </div>

        </div>

    <% } %>


    <div class="stats">


        <div class="stat-card">

            <div class="stat-title">
                Total Appointments
            </div>

            <div class="stat-number">
                <%= totalAppointments %>
            </div>

        </div>


        <div class="stat-card">

            <div class="stat-title">
                Today's Appointments
            </div>

            <div class="stat-number">
                <%= todayAppointments %>
            </div>

        </div>


        <div class="stat-card">

            <div class="stat-title">
                Pending
            </div>

            <div class="stat-number">
                <%= pendingAppointments %>
            </div>

        </div>


        <div class="stat-card">

            <div class="stat-title">
                Confirmed
            </div>

            <div class="stat-number">
                <%= confirmedAppointments %>
            </div>

        </div>


        <div class="stat-card">

            <div class="stat-title">
                Completed
            </div>

            <div class="stat-number">
                <%= completedAppointments %>
            </div>

        </div>


        <div class="stat-card">

            <div class="stat-title">
                Cancelled
            </div>

            <div class="stat-number">
                <%= cancelledAppointments %>
            </div>

        </div>

    </div>


    <div class="content-grid">


        <div class="section-card">

            <div class="section-header">

                <h2>
                    Today's Appointments
                </h2>

                <span>
                    <%= today != null ? today : "" %>
                </span>

            </div>


            <div class="appointment-list">

                <% if (todayList == null ||
                       todayList.isEmpty()) { %>

                    <div class="empty">

                        No appointments scheduled for today.

                    </div>

                <% } else { %>


                    <% for (Appointment appointment :
                            todayList) { %>

                        <div class="appointment-item">

                            <div class="appointment-main">

                                <div class="appointment-number">

                                    Appointment #

                                    <%= appointment.getAppointmentNumber() != null
                                            ? appointment.getAppointmentNumber()
                                            : appointment.getId() %>

                                </div>


                                <div class="appointment-details">

                                    Patient ID:

                                    <%= appointment.getPatientId() != null
                                            ? appointment.getPatientId()
                                            : "-" %>

                                    &nbsp; | &nbsp;

                                    Treatment ID:

                                    <%= appointment.getTreatmentId() != null
                                            ? appointment.getTreatmentId()
                                            : "-" %>

                                </div>


                                <div class="appointment-date">

                                    Time:

                                    <%= appointment.getAppointmentTime() != null
                                            ? appointment.getAppointmentTime()
                                            : "-" %>

                                </div>

                            </div>


                            <%
                                String status =
                                        appointment.getStatus();

                                String statusClass =
                                        "status-pending";

                                if ("CONFIRMED".equalsIgnoreCase(status)) {

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

                                <%= status != null
                                        ? status
                                        : "UNKNOWN" %>

                            </span>

                        </div>

                    <% } %>

                <% } %>

            </div>

        </div>


        <div class="section-card">

            <div class="section-header">

                <h2>
                    Upcoming Appointments
                </h2>

                <span>
                    Next appointments
                </span>

            </div>


            <div class="appointment-list">

                <% if (upcomingAppointments == null ||
                       upcomingAppointments.isEmpty()) { %>

                    <div class="empty">

                        No upcoming appointments.

                    </div>

                <% } else { %>


                    <% for (Appointment appointment :
                            upcomingAppointments) { %>

                        <div class="appointment-item">

                            <div class="appointment-main">

                                <div class="appointment-number">

                                    Appointment #

                                    <%= appointment.getAppointmentNumber() != null
                                            ? appointment.getAppointmentNumber()
                                            : appointment.getId() %>

                                </div>


                                <div class="appointment-details">

                                    Patient ID:

                                    <%= appointment.getPatientId() != null
                                            ? appointment.getPatientId()
                                            : "-" %>

                                    &nbsp; | &nbsp;

                                    Treatment ID:

                                    <%= appointment.getTreatmentId() != null
                                            ? appointment.getTreatmentId()
                                            : "-" %>

                                </div>


                                <div class="appointment-date">

                                    <%= appointment.getAppointmentDate() != null
                                            ? appointment.getAppointmentDate()
                                            : "-" %>

                                    &nbsp;

                                    <%= appointment.getAppointmentTime() != null
                                            ? appointment.getAppointmentTime()
                                            : "" %>

                                </div>

                            </div>


                            <%
                                String status =
                                        appointment.getStatus();

                                String statusClass =
                                        "status-pending";

                                if ("CONFIRMED".equalsIgnoreCase(status)) {

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

                                <%= status != null
                                        ? status
                                        : "UNKNOWN" %>

                            </span>

                        </div>

                    <% } %>

                <% } %>

            </div>

        </div>

    </div>

</main>


</body>

</html>
