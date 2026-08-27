<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sunrise.dentalclinic.entity.Patient" %>
<%@ page import="com.sunrise.dentalclinic.entity.User" %>

<%
    List<Patient> patients =
            (List<Patient>) request.getAttribute("patients");

    String error =
            (String) request.getAttribute("error");

    User user =
            (User) session.getAttribute("loggedInUser");

    int patientCount =
            patients == null ? 0 : patients.size();

    String contextPath =
            request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Patients - Sunrise Dental Clinic</title>

    <style>

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: Arial, Helvetica, sans-serif;
        }

        body {
            background: #f5f8fb;
            color: #1f2937;
            min-height: 100vh;
        }

        .sidebar {
            position: fixed;
            left: 0;
            top: 0;
            width: 250px;
            height: 100vh;
            background: linear-gradient(180deg, #0b354d, #0f3d56);
            color: white;
            padding: 25px 18px;
            z-index: 100;
            box-shadow: 4px 0 15px rgba(0,0,0,0.08);
        }

        .brand {
            padding: 5px 10px 25px;
            border-bottom: 1px solid rgba(255,255,255,0.12);
            margin-bottom: 22px;
        }

        .brand-title {
            font-size: 21px;
            font-weight: 700;
        }

        .brand-subtitle {
            margin-top: 6px;
            color: #a9d8d5;
            font-size: 12px;
        }

        .nav-title {
            font-size: 10px;
            color: #83b9bb;
            text-transform: uppercase;
            letter-spacing: 1.2px;
            padding: 0 12px 10px;
        }

        .nav-link {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 12px 13px;
            margin-bottom: 5px;
            color: #d7e8ea;
            text-decoration: none;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 600;
        }

        .nav-link:hover {
            background: rgba(255,255,255,0.09);
            color: white;
        }

        .nav-link.active {
            background: #159a9c;
            color: white;
        }

        .nav-icon {
            width: 25px;
            text-align: center;
            font-size: 16px;
        }

        .main {
            margin-left: 250px;
            min-height: 100vh;
        }

        .topbar {
            height: 78px;
            background: white;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 35px;
            border-bottom: 1px solid #e5e7eb;
        }

        .page-heading h1 {
            font-size: 23px;
            color: #0f3d56;
            margin-bottom: 4px;
        }

        .page-heading p {
            color: #6b7280;
            font-size: 13px;
        }

        .user-area {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .user-avatar {
            width: 38px;
            height: 38px;
            border-radius: 50%;
            background: #e2f4f3;
            color: #0f7779;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
        }

        .user-name {
            color: #1f2937;
            font-size: 13px;
            font-weight: bold;
        }

        .user-role {
            color: #6b7280;
            font-size: 11px;
            text-transform: uppercase;
        }

        .content {
            padding: 32px 35px;
            max-width: 1450px;
            margin: auto;
        }

        .breadcrumb {
            display: flex;
            gap: 8px;
            color: #6b7280;
            font-size: 13px;
            margin-bottom: 25px;
        }

        .breadcrumb a {
            color: #159a9c;
            text-decoration: none;
            font-weight: 600;
        }

        .stats {
            margin-bottom: 25px;
        }

        .stat-card {
            background: white;
            border-radius: 12px;
            padding: 20px;
            border: 1px solid #e7edf1;
            box-shadow: 0 3px 12px rgba(15,61,86,0.05);
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .stat-icon {
            width: 48px;
            height: 48px;
            border-radius: 10px;
            background: #e3f5f4;
            color: #159a9c;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 21px;
        }

        .stat-number {
            font-size: 24px;
            font-weight: 700;
            color: #0f3d56;
        }

        .stat-label {
            color: #6b7280;
            font-size: 12px;
        }

        .error {
            background: #fff1f2;
            border: 1px solid #fecdd3;
            color: #be123c;
            padding: 13px 16px;
            border-radius: 9px;
            margin-bottom: 20px;
            font-size: 14px;
            font-weight: 600;
        }

        .card {
            background: white;
            border-radius: 12px;
            border: 1px solid #e7edf1;
            box-shadow: 0 3px 12px rgba(15,61,86,0.05);
            padding: 25px;
            margin-bottom: 24px;
        }

        .card-header {
            margin-bottom: 22px;
            padding-bottom: 15px;
            border-bottom: 1px solid #edf1f3;
        }

        .card-title {
            color: #0f3d56;
            font-size: 18px;
            font-weight: 700;
        }

        .card-description {
            color: #6b7280;
            font-size: 12px;
            margin-top: 4px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 18px 22px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group.full {
            grid-column: 1 / -1;
        }

        label {
            font-size: 12px;
            font-weight: 700;
            color: #374151;
            margin-bottom: 7px;
        }

        .required {
            color: #dc2626;
        }

        input {
            width: 100%;
            padding: 12px 13px;
            border: 1px solid #d7dee5;
            border-radius: 8px;
            font-size: 14px;
        }

        input:focus {
            outline: none;
            border-color: #159a9c;
            box-shadow: 0 0 0 3px rgba(21,154,156,0.10);
        }

        .form-footer {
            margin-top: 22px;
            display: flex;
            justify-content: flex-end;
        }

        .primary-button {
            border: none;
            padding: 12px 22px;
            border-radius: 8px;
            background: #159a9c;
            color: white;
            font-size: 13px;
            font-weight: 700;
            cursor: pointer;
        }

        .primary-button:hover {
            background: #117779;
        }

        .table-toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            margin-bottom: 18px;
        }

        .table-info {
            color: #6b7280;
            font-size: 13px;
        }

        .search-box {
            position: relative;
            width: 260px;
        }

        .search-box input {
            padding-left: 38px;
        }

        .search-icon {
            position: absolute;
            left: 13px;
            top: 50%;
            transform: translateY(-50%);
            color: #9ca3af;
        }

        .table-wrapper {
            overflow-x: auto;
            border: 1px solid #e5e7eb;
            border-radius: 9px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 700px;
        }

        th {
            background: #f5f8fa;
            color: #52616d;
            padding: 13px 15px;
            text-align: left;
            font-size: 11px;
            text-transform: uppercase;
        }

        td {
            padding: 15px;
            border-bottom: 1px solid #edf1f3;
            color: #374151;
            font-size: 13px;
        }

        tbody tr:hover {
            background: #f8fbfc;
        }

        .patient-id {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 34px;
            height: 28px;
            padding: 0 8px;
            border-radius: 6px;
            background: #eaf5f5;
            color: #0f7779;
            font-weight: 700;
        }

        .patient-name {
            font-weight: 700;
            color: #0f3d56;
        }

        .contact {
            color: #159a9c;
            font-weight: 600;
        }

        .empty-state {
            text-align: center;
            padding: 50px 20px;
            color: #6b7280;
        }

        footer {
            text-align: center;
            color: #9ca3af;
            font-size: 11px;
            padding: 10px 35px 25px;
        }

        @media (max-width: 900px) {
            .sidebar {
                width: 210px;
            }

            .main {
                margin-left: 210px;
            }

            .form-grid {
                grid-template-columns: 1fr;
            }

            .form-group.full {
                grid-column: auto;
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

            .topbar {
                padding: 18px;
            }

            .content {
                padding: 20px 15px;
            }

            .table-toolbar {
                flex-direction: column;
                align-items: flex-start;
            }

            .search-box {
                width: 100%;
            }

            .primary-button {
                width: 100%;
            }
        }

    </style>

</head>

<body>

<aside class="sidebar">

    <div class="brand">
        <div class="brand-title">Sunrise Dental</div>
        <div class="brand-subtitle">
            Clinic Management System
        </div>
    </div>

    <div class="nav-title">Main Menu</div>

    <a class="nav-link"
       href="<%= contextPath %>/dashboard">
        <span class="nav-icon">⌂</span>
        Dashboard
    </a>

    <a class="nav-link active"
       href="<%= contextPath %>/patients">
        <span class="nav-icon">♙</span>
        Patients
    </a>

    <a class="nav-link"
       href="<%= contextPath %>/appointments">
        <span class="nav-icon">◷</span>
        Appointments
    </a>

    <a class="nav-link"
       href="<%= contextPath %>/dentists">
        <span class="nav-icon">⚕</span>
        Dentists
    </a>

    <div class="nav-title" style="margin-top:20px;">
        Management
    </div>

    <a class="nav-link"
       href="<%= contextPath %>/treatments">
        <span class="nav-icon">✚</span>
        Treatments
    </a>

    <a class="nav-link"
       href="<%= contextPath %>/bills">
        <span class="nav-icon">▤</span>
        Billing
    </a>

    <% if (user != null &&
           "ADMIN".equalsIgnoreCase(user.getRole())) { %>

        <a class="nav-link"
           href="<%= contextPath %>/manage-users">
            <span class="nav-icon">⚙</span>
            Manage Users
        </a>

    <% } %>

</aside>

<main class="main">

    <div class="topbar">

        <div class="page-heading">
            <h1>Patients</h1>
            <p>Manage patient records and information</p>
        </div>

        <div class="user-area">

            <div class="user-avatar">
                <%= user != null &&
                    user.getUsername() != null &&
                    !user.getUsername().isBlank()
                    ? user.getUsername()
                        .substring(0,1)
                        .toUpperCase()
                    : "U" %>
            </div>

            <div>
                <div class="user-name">
                    <%= user != null ? user.getUsername() : "" %>
                </div>

                <div class="user-role">
                    <%= user != null ? user.getRole() : "" %>
                </div>
            </div>

        </div>

    </div>

    <div class="content">

        <div class="breadcrumb">

            <a href="<%= contextPath %>/dashboard">
                Dashboard
            </a>

            <span>/</span>

            <span>Patients</span>

        </div>

        <% if (error != null && !error.isBlank()) { %>

            <div class="error">
                <%= error %>
            </div>

        <% } %>

        <div class="stats">

            <div class="stat-card">

                <div class="stat-icon">♙</div>

                <div>

                    <div class="stat-number">
                        <%= patientCount %>
                    </div>

                    <div class="stat-label">
                        Registered Patients
                    </div>

                </div>

            </div>

        </div>

        <div class="card">

            <div class="card-header">

                <div class="card-title">
                    Add New Patient
                </div>

                <div class="card-description">
                    Enter the patient's personal information
                </div>

            </div>

            <form method="post"
                  action="<%= contextPath %>/patients">

                <div class="form-grid">

                    <div class="form-group">

                        <label for="patientName">
                            Patient Name
                            <span class="required">*</span>
                        </label>

                        <input type="text"
                               id="patientName"
                               name="patientName"
                               required
                               maxlength="100"
                               placeholder="Enter patient full name">

                    </div>

                    <div class="form-group">

                        <label for="contactNumber">
                            Contact Number
                            <span class="required">*</span>
                        </label>

                        <input type="text"
                               id="contactNumber"
                               name="contactNumber"
                               required
                               maxlength="20"
                               placeholder="Enter contact number">

                    </div>

                    <div class="form-group full">

                        <label for="address">
                            Address
                            <span class="required">*</span>
                        </label>

                        <input type="text"
                               id="address"
                               name="address"
                               required
                               maxlength="255"
                               placeholder="Enter patient's address">

                    </div>

                </div>

                <div class="form-footer">

                    <button type="submit"
                            class="primary-button">
                        + Add Patient
                    </button>

                </div>

            </form>

        </div>

        <div class="card">

            <div class="card-header">

                <div class="card-title">
                    Registered Patients
                </div>

                <div class="card-description">
                    View all patients registered in the clinic
                </div>

            </div>

            <div class="table-toolbar">

                <div class="table-info">
                    <%= patientCount %> patient(s) registered
                </div>

                <div class="search-box">

                    <span class="search-icon">⌕</span>

                    <input type="text"
                           id="patientSearch"
                           placeholder="Search patients..."
                           onkeyup="searchPatients()">

                </div>

            </div>

            <div class="table-wrapper">

                <table id="patientTable">

                    <thead>

                    <tr>
                        <th>ID</th>
                        <th>Patient Name</th>
                        <th>Address</th>
                        <th>Contact Number</th>
                    </tr>

                    </thead>

                    <tbody>

                    <% if (patients == null || patients.isEmpty()) { %>

                        <tr>

                            <td colspan="4">

                                <div class="empty-state">

                                    <h3>No Patients Found</h3>

                                    <p>
                                        There are no registered
                                        patients yet.
                                    </p>

                                </div>

                            </td>

                        </tr>

                    <% } else { %>

                        <% for (Patient patient : patients) { %>

                            <tr>

                                <td>
                                    <span class="patient-id">
                                        <%= patient.getId() %>
                                    </span>
                                </td>

                                <td class="patient-name">
                                    <%= patient.getPatientName() %>
                                </td>

                                <td>
                                    <%= patient.getAddress() %>
                                </td>

                                <td class="contact">
                                    <%= patient.getContactNumber() %>
                                </td>

                            </tr>

                        <% } %>

                    <% } %>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

    <footer>
        © 2026 Sunrise Dental Clinic Management System
    </footer>

</main>

<script>

function searchPatients() {

    const input =
        document.getElementById("patientSearch");

    const filter =
        input.value.toLowerCase();

    const rows =
        document.querySelectorAll("#patientTable tbody tr");

    rows.forEach(function(row) {

        const text =
            row.textContent.toLowerCase();

        row.style.display =
            text.includes(filter) ? "" : "none";

    });

}

</script>

</body>

</html>