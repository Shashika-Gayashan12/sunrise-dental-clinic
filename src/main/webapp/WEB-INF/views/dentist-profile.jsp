<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>
<%@ page import="com.sunrise.dentalclinic.entity.User" %>

<%
    Dentist dentist =
            (Dentist) request.getAttribute("dentist");

    User loggedInUser =
            (User) request.getAttribute("loggedInUser");

    String error =
            (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Dentist Profile - Sunrise Dental Clinic</title>

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

        .profile-card {
            background: white;
            border-radius: 16px;
            padding: 35px;
            max-width: 900px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
        }

        .profile-top {
            display: flex;
            align-items: center;
            gap: 20px;
            padding-bottom: 30px;
            border-bottom: 1px solid #e5e7eb;
            margin-bottom: 30px;
        }

        .profile-icon {
            width: 75px;
            height: 75px;
            border-radius: 50%;
            background: #e8f5f3;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            font-weight: bold;
            color: #0f766e;
        }

        .profile-title h2 {
            margin: 0;
            font-size: 24px;
            color: #123b3a;
        }

        .profile-title p {
            margin: 7px 0 0;
            color: #6b7280;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 22px;
        }

        .info-box {
            background: #f8fafc;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 20px;
        }

        .info-label {
            font-size: 12px;
            font-weight: bold;
            color: #6b7280;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 8px;
        }

        .info-value {
            font-size: 17px;
            font-weight: 600;
            color: #1f2937;
        }

        .error-box {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #b91c1c;
            padding: 15px 18px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .account-box {
            margin-top: 25px;
            background: #f0fdfa;
            border: 1px solid #ccfbf1;
            border-radius: 12px;
            padding: 20px;
        }

        .account-box h3 {
            margin: 0 0 15px;
            color: #115e59;
            font-size: 18px;
        }

        .account-row {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
        }

        .account-label {
            color: #6b7280;
        }

        .account-value {
            font-weight: 600;
            color: #123b3a;
        }

        .status-active {
            color: #15803d;
        }

        @media (max-width: 900px) {

            .main {
                margin-left: 0;
                padding: 25px;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }
        }

    </style>

</head>

<body>

<jsp:include page="dentist-sidebar.jsp" />

<div class="main">

    <div class="page-header">

        <h1>My Profile</h1>

        <p>
            View your dentist profile and account information.
        </p>

    </div>

    <% if (error != null) { %>

        <div class="error-box">
            <%= error %>
        </div>

    <% } %>


    <% if (dentist != null) { %>

        <div class="profile-card">

            <div class="profile-top">

                <div class="profile-icon">
                    D
                </div>

                <div class="profile-title">

                    <h2>
                        <%= dentist.getDentistName() %>
                    </h2>

                    <p>
                        <%= dentist.getSpecialization() %>
                    </p>

                </div>

            </div>


            <div class="info-grid">

                <div class="info-box">

                    <div class="info-label">
                        Dentist ID
                    </div>

                    <div class="info-value">
                        <%= dentist.getId() %>
                    </div>

                </div>


                <div class="info-box">

                    <div class="info-label">
                        Dentist Name
                    </div>

                    <div class="info-value">
                        <%= dentist.getDentistName() %>
                    </div>

                </div>


                <div class="info-box">

                    <div class="info-label">
                        Specialization
                    </div>

                    <div class="info-value">
                        <%= dentist.getSpecialization() %>
                    </div>

                </div>


                <div class="info-box">

                    <div class="info-label">
                        Contact Number
                    </div>

                    <div class="info-value">

                        <%
                            String contact =
                                    dentist.getContactNumber();

                            if (contact == null ||
                                    contact.trim().isEmpty()) {
                        %>

                            Not provided

                        <%
                            } else {
                        %>

                            <%= contact %>

                        <%
                            }
                        %>

                    </div>

                </div>

            </div>


            <% if (loggedInUser != null) { %>

                <div class="account-box">

                    <h3>Account Information</h3>

                    <div class="account-row">

                        <span class="account-label">
                            Username
                        </span>

                        <span class="account-value">
                            <%= loggedInUser.getUsername() %>
                        </span>

                    </div>


                    <div class="account-row">

                        <span class="account-label">
                            Role
                        </span>

                        <span class="account-value">
                            <%= loggedInUser.getRole() %>
                        </span>

                    </div>


                    <div class="account-row">

                        <span class="account-label">
                            Status
                        </span>

                        <span class="account-value status-active">
                            <%= loggedInUser.getStatus() %>
                        </span>

                    </div>

                </div>

            <% } %>

        </div>

    <% } %>

</div>

</body>

</html>