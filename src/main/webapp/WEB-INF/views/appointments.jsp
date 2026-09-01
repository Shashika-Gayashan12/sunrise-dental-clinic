<%@ page import="java.util.List" %>
<%@ page import="com.sunrise.dentalclinic.entity.Appointment" %>
<%@ page import="com.sunrise.dentalclinic.entity.Patient" %>
<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>
<%@ page import="com.sunrise.dentalclinic.entity.Treatment" %>

<%
    String contextPath = request.getContextPath();

    List<Appointment> appointments =
            (List<Appointment>) request.getAttribute("appointments");

    List<Patient> patients =
            (List<Patient>) request.getAttribute("patients");

    List<Dentist> dentists =
            (List<Dentist>) request.getAttribute("dentists");

    List<Treatment> treatments =
            (List<Treatment>) request.getAttribute("treatments");

    String filter =
            (String) request.getAttribute("filter");

    String error =
            (String) request.getAttribute("error");


    if (filter == null || filter.isBlank()) {
        filter = "active";
    }
%>

<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>
Appointments - Sunrise Dental Clinic
</title>

<style>

* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

body {
    font-family: Arial, Helvetica, sans-serif;
    background: #f5f8fa;
    color: #1f2937;
}

.page {
    display: flex;
    min-height: 100vh;
}

.sidebar {
    width: 245px;
    min-width: 245px;
    background: #0f3d56;
    color: white;
    padding: 28px 18px;
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
}

.logo {
    padding: 0 12px 30px;
    border-bottom: 1px solid rgba(255,255,255,0.15);
    margin-bottom: 25px;
}

.logo h1 {
    font-size: 20px;
    margin-bottom: 5px;
}

.logo p {
    font-size: 12px;
    color: #b8d9df;
}

.nav-title {
    font-size: 11px;
    color: #91b8c4;
    text-transform: uppercase;
    letter-spacing: 1px;
    padding: 0 12px;
    margin-bottom: 10px;
}

.nav-item {
    display: flex;
    align-items: center;
    width: 100%;
    height: 42px;
    padding: 0 14px;
    margin-bottom: 5px;
    border-radius: 7px;
    color: #dcecef;
    text-decoration: none;
    font-size: 14px;
    font-weight: 600;
    white-space: nowrap;
    border: 1px solid transparent;
}

.nav-item:hover {
    background: rgba(255,255,255,0.08);
}

.nav-item.active {
    background: #159a9c;
    color: white;
    border-color: #159a9c;
}

.main {
    margin-left: 245px;
    width: calc(100% - 245px);
    min-width: 0;
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

.page-title h2 {
    color: #0f3d56;
    font-size: 22px;
}

.page-title p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 4px;
}

.user-info {
    font-size: 14px;
    font-weight: bold;
    color: #374151;
}

.content {
    padding: 32px;
    max-width: 1600px;
}

.back {
    display: inline-block;
    margin-bottom: 20px;
    color: #159a9c;
    text-decoration: none;
    font-size: 14px;
    font-weight: bold;
}

.card {
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    margin-bottom: 25px;
    overflow: hidden;
}

.card-header {
    padding: 22px 25px;
    border-bottom: 1px solid #e5e7eb;
}

.card-header h3 {
    color: #0f3d56;
    font-size: 19px;
}

.card-header p {
    color: #6b7280;
    font-size: 13px;
    margin-top: 5px;
}

.card-body {
    padding: 25px;
}

.form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
}

.form-group label {
    display: block;
    font-size: 13px;
    font-weight: bold;
    color: #374151;
    margin-bottom: 7px;
}

.form-group input,
.form-group select {
    width: 100%;
    height: 44px;
    border: 1px solid #d1d5db;
    border-radius: 7px;
    padding: 0 13px;
    font-size: 14px;
    background: white;
}

.form-group input:focus,
.form-group select:focus {
    outline: none;
    border-color: #159a9c;
    box-shadow: 0 0 0 3px rgba(21,154,156,0.10);
}

.form-actions {
    margin-top: 22px;
    display: flex;
    justify-content: flex-end;
}

.primary-btn {
    border: none;
    background: #159a9c;
    color: white;
    padding: 11px 22px;
    border-radius: 7px;
    font-weight: bold;
    cursor: pointer;
    font-size: 13px;
}

.primary-btn:hover {
    background: #117779;
}

.error {
    background: #fff1f2;
    color: #b91c1c;
    border-left: 4px solid #dc2626;
    padding: 13px 15px;
    border-radius: 7px;
    margin-bottom: 20px;
    font-size: 14px;
}

.filters {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 20px;
}

.filter {
    padding: 9px 15px;
    border-radius: 6px;
    background: #f1f5f9;
    color: #475569;
    text-decoration: none;
    font-size: 13px;
    font-weight: bold;
    border: 1px solid #e2e8f0;
}

.filter:hover {
    background: #e2e8f0;
}

.filter.active {
    background: #159a9c;
    color: white;
    border-color: #159a9c;
}

.table-container {
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 1000px;
}

thead th {
    background: #f8fafc;
    color: #64748b;
    font-size: 12px;
    text-transform: uppercase;
    letter-spacing: .4px;
    padding: 14px 12px;
    text-align: left;
    border-bottom: 1px solid #e5e7eb;
    white-space: nowrap;
}

tbody td {
    padding: 16px 12px;
    border-bottom: 1px solid #edf0f2;
    font-size: 13px;
    vertical-align: middle;
}

tbody tr:hover {
    background: #fafcfd;
}

.appointment-number {
    font-weight: bold;
    color: #0f3d56;
}

.person-name {
    font-weight: bold;
    color: #1f2937;
}

.secondary {
    display: block;
    color: #8a94a3;
    font-size: 11px;
    margin-top: 3px;
}

.status {
    display: inline-block;
    padding: 6px 10px;
    border-radius: 20px;
    font-size: 11px;
    font-weight: bold;
}

.pending {
    background: #fff7d6;
    color: #956b00;
}

.confirmed {
    background: #dcfce7;
    color: #166534;
}

.completed {
    background: #e0f2fe;
    color: #0369a1;
}

.cancelled {
    background: #fee2e2;
    color: #991b1b;
}

.actions {
    white-space: nowrap;
}

.action-btn {
    display: inline-block;
    padding: 7px 10px;
    margin-right: 4px;
    border-radius: 5px;
    text-decoration: none;
    font-size: 11px;
    font-weight: bold;
}

.view {
    background: #e0f2fe;
    color: #0369a1;
}

.edit {
    background: #dcfce7;
    color: #166534;
}

.cancel {
    background: #fee2e2;
    color: #991b1b;
    border: none;
    cursor: pointer;
    padding: 7px 10px;
    border-radius: 5px;
    font-size: 11px;
    font-weight: bold;
}

.cancel:hover {
    background: #fecaca;
}

.empty {
    text-align: center;
    padding: 45px !important;
    color: #94a3b8;
}

footer {
    text-align: center;
    color: #94a3b8;
    font-size: 12px;
    padding: 25px;
}

@media (max-width: 900px) {

    .sidebar {
        width: 200px;
        min-width: 200px;
    }

    .main {
        margin-left: 200px;
        width: calc(100% - 200px);
    }

    .form-grid {
        grid-template-columns: 1fr;
    }

    .content {
        padding: 20px;
    }
}

@media (max-width: 650px) {

    .sidebar {
        position: static;
        width: 100%;
        min-width: 0;
    }

    .page {
        display: block;
    }

    .main {
        margin-left: 0;
        width: 100%;
    }

    .topbar {
        padding: 0 20px;
    }

    .content {
        padding: 15px;
    }
}

</style>

</head>

<body>

<div class="page">

<aside class="sidebar">

    <div class="logo">

        <h1>Sunrise Dental Clinic</h1>

        <p>Management System</p>

    </div>

    <div class="nav-title">
        Main Menu
    </div>

    <a class="nav-item"
       href="<%= contextPath %>/dashboard">
        Dashboard
    </a>

    <a class="nav-item"
       href="<%= contextPath %>/patients">
        Patients
    </a>

    <a class="nav-item"
       href="<%= contextPath %>/dentists">
        Dentists
    </a>

    <a class="nav-item"
       href="<%= contextPath %>/treatments">
        Treatments
    </a>

    <a class="nav-item active"
       href="<%= contextPath %>/appointments">
        Appointments
    </a>

</aside>


<main class="main">

<header class="topbar">

    <div class="page-title">

        <h2>Appointments</h2>

        <p>
            Schedule and manage patient appointments
        </p>

    </div>

    <div class="user-info">
        Sunrise Dental Clinic
    </div>

</header>


<div class="content">

<a class="back"
   href="<%= contextPath %>/dashboard">

    ← Back to Dashboard

</a>


<%
    if (error != null && !error.isBlank()) {
%>

<div class="error">
    <%= error %>
</div>

<%
    }
%>


<!-- =================================================
     BOOK APPOINTMENT
     ================================================= -->

<div class="card">

<div class="card-header">

    <h3>
        Book New Appointment
    </h3>

    <p>
        Create a new appointment for a patient
    </p>

</div>

<div class="card-body">

<form method="post"
      action="<%= contextPath %>/appointments">

<div class="form-grid">


<div class="form-group">

<label>
    Patient
</label>

<select name="patientId" required>

<option value="">
    Select patient
</option>

<%
    if (patients != null) {

        for (Patient patient : patients) {
%>

<option value="<%= patient.getId() %>">

    <%= patient.getPatientName() %>

</option>

<%
        }
    }
%>

</select>

</div>


<div class="form-group">

<label>
    Dentist
</label>

<select name="dentistId" required>

<option value="">
    Select dentist
</option>

<%
    if (dentists != null) {

        for (Dentist dentist : dentists) {
%>

<option value="<%= dentist.getId() %>">

    <%= dentist.getDentistName() %>
    -
    <%= dentist.getSpecialization() %>

</option>

<%
        }
    }
%>

</select>

</div>


<div class="form-group">

<label>
    Treatment
</label>

<select name="treatmentId" required>

<option value="">
    Select treatment
</option>

<%
    if (treatments != null) {

        for (Treatment treatment : treatments) {
%>

<option value="<%= treatment.getId() %>">

    <%= treatment.getTreatmentName() %>

</option>

<%
        }
    }
%>

</select>

</div>


<div class="form-group">

<label>
    Appointment Date
</label>

<input
    type="date"
    name="appointmentDate"
    required>

</div>


<div class="form-group">

<label>
    Appointment Time
</label>

<input
    type="time"
    name="appointmentTime"
    required>

</div>


</div>


<div class="form-actions">

<button
    type="submit"
    class="primary-btn">

    Book Appointment

</button>

</div>

</form>

</div>

</div>


<!-- =================================================
     APPOINTMENTS TABLE
     ================================================= -->

<div class="card">

<div class="card-header">

    <h3>
        Appointments
    </h3>

    <p>
        View and manage scheduled appointments
    </p>

</div>

<div class="card-body">


<div class="filters">

<a class="filter <%= "active".equalsIgnoreCase(filter) ? "active" : "" %>"
   href="<%= contextPath %>/appointments?filter=active">
    Active
</a>

<a class="filter <%= "all".equalsIgnoreCase(filter) ? "active" : "" %>"
   href="<%= contextPath %>/appointments?filter=all">
    All
</a>

<a class="filter <%= "pending".equalsIgnoreCase(filter) ? "active" : "" %>"
   href="<%= contextPath %>/appointments?filter=pending">
    Pending
</a>

<a class="filter <%= "confirmed".equalsIgnoreCase(filter) ? "active" : "" %>"
   href="<%= contextPath %>/appointments?filter=confirmed">
    Confirmed
</a>

<a class="filter <%= "completed".equalsIgnoreCase(filter) ? "active" : "" %>"
   href="<%= contextPath %>/appointments?filter=completed">
    Completed
</a>

<a class="filter <%= "cancelled".equalsIgnoreCase(filter) ? "active" : "" %>"
   href="<%= contextPath %>/appointments?filter=cancelled">
    Cancelled
</a>

</div>


<div class="table-container">

<table>

<thead>

<tr>

<th>Appointment</th>

<th>Patient</th>

<th>Dentist</th>

<th>Treatment</th>

<th>Date</th>

<th>Time</th>

<th>Status</th>

<th>Actions</th>

</tr>

</thead>


<tbody>

<%
    if (appointments == null ||
        appointments.isEmpty()) {
%>

<tr>

<td colspan="8"
    class="empty">

    No appointments found.

</td>

</tr>

<%
    } else {

        for (Appointment appointment :
                appointments) {

            Patient patient = null;

            if (patients != null) {

                for (Patient p : patients) {

                    if (appointment.getPatientId() != null &&
                        appointment.getPatientId().equals(p.getId())) {

                        patient = p;
                        break;
                    }
                }
            }


            Dentist dentist = null;

            if (dentists != null) {

                for (Dentist d : dentists) {

                    if (appointment.getDentistId() != null &&
                        appointment.getDentistId().equals(d.getId())) {

                        dentist = d;
                        break;
                    }
                }
            }


            Treatment treatment = null;

            if (treatments != null) {

                for (Treatment t : treatments) {

                    if (appointment.getTreatmentId() != null &&
                        appointment.getTreatmentId().equals(t.getId())) {

                        treatment = t;
                        break;
                    }
                }
            }


            String status =
                    appointment.getStatus();

            if (status == null) {
                status = "";
            }

            String statusClass =
                    status.toLowerCase();
%>


<tr>


<td>

<span class="appointment-number">

<%= appointment.getAppointmentNumber() %>

</span>

</td>


<td>

<%
    if (patient != null) {
%>

<span class="person-name">

<%= patient.getPatientName() %>

</span>

<span class="secondary">

ID: <%= patient.getId() %>

</span>

<%
    } else {
%>

<span class="person-name">

Patient #<%= appointment.getPatientId() %>

</span>

<%
    }
%>

</td>


<td>

<%
    if (dentist != null) {
%>

<span class="person-name">

<%= dentist.getDentistName() %>

</span>

<span class="secondary">

<%= dentist.getSpecialization() %>

</span>

<%
    } else {
%>

<span class="person-name">

Dentist #<%= appointment.getDentistId() %>

</span>

<%
    }
%>

</td>


<td>

<%
    if (treatment != null) {
%>

<span class="person-name">

<%= treatment.getTreatmentName() %>

</span>

<%
    } else {
%>

<span class="person-name">

Treatment #<%= appointment.getTreatmentId() %>

</span>

<%
    }
%>

</td>


<td>

<%= appointment.getAppointmentDate() %>

</td>


<td>

<%
    if (appointment.getAppointmentTime() != null) {
%>

<%= appointment.getAppointmentTime().toString().substring(0, 5) %>

<%
    }
%>

</td>


<td>

<span class="status <%= statusClass %>">

<%= status %>

</span>

</td>


<td class="actions">


<a class="action-btn view"
   href="<%= contextPath %>/appointments?action=view&id=<%= appointment.getId() %>">

    View

</a>


<%
    if (!"CANCELLED".equalsIgnoreCase(status)) {
%>

<a class="action-btn edit"
   href="<%= contextPath %>/appointments?action=edit&id=<%= appointment.getId() %>">

    Edit

</a>

<%
    }


    if ("PENDING".equalsIgnoreCase(status)
        ||
        "CONFIRMED".equalsIgnoreCase(status)) {
%>


<form method="post"
      action="<%= contextPath %>/appointments"
      style="display:inline;"
      onsubmit="return confirm('Are you sure you want to cancel this appointment?');">

<input type="hidden"
       name="action"
       value="cancel">

<input type="hidden"
       name="id"
       value="<%= appointment.getId() %>">

<button type="submit"
        class="cancel">

    Cancel

</button>

</form>


<%
    }
%>

</td>

</tr>


<%
        }
    }
%>

</tbody>

</table>

</div>

</div>

</div>

</div>


<footer>

© 2026 Sunrise Dental Clinic
Management System

</footer>


</main>

</div>

</body>

</html>