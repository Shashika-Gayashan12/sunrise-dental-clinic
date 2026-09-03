<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.sunrise.dentalclinic.entity.Appointment" %>
<%@ page import="com.sunrise.dentalclinic.entity.Patient" %>
<%@ page import="com.sunrise.dentalclinic.entity.Treatment" %>
<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>

<%
    Appointment appointment =
            (Appointment) request.getAttribute("appointment");

    Patient patient =
            (Patient) request.getAttribute("patient");

    Treatment treatment =
            (Treatment) request.getAttribute("treatment");

    Dentist dentist =
            (Dentist) request.getAttribute("dentist");

    String error =
            (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Appointment Details - Sunrise Dental Clinic</title>

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
            margin-bottom: 25px;
        }

        .page-header h1 {
            margin: 0;
            color: #123b3a;
            font-size: 30px;
        }

        .page-header p {
            margin-top: 8px;
            color: #6b7280;
        }

        .back-btn {
            display: inline-block;
            margin-bottom: 20px;
            padding: 9px 15px;
            background: white;
            color: #0f766e;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            text-decoration: none;
            font-size: 14px;
            font-weight: 600;
        }

        .back-btn:hover {
            background: #f0fdfa;
        }

        .error-box {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #b91c1c;
            padding: 16px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .details-card {
            background: white;
            border-radius: 16px;
            padding: 30px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
            max-width: 1000px;
        }

        .section {
            margin-bottom: 30px;
        }

        .section:last-child {
            margin-bottom: 0;
        }

        .section-title {
            font-size: 18px;
            color: #123b3a;
            font-weight: 700;
            margin-bottom: 18px;
            padding-bottom: 10px;
            border-bottom: 1px solid #e5e7eb;
        }

        .details-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 18px;
        }

        .detail-box {
            background: #f8fafc;
            border: 1px solid #e5e7eb;
            border-radius: 10px;
            padding: 16px;
        }

        .label {
            display: block;
            font-size: 12px;
            color: #6b7280;
            text-transform: uppercase;
            font-weight: 700;
            margin-bottom: 7px;
        }

        .value {
            font-size: 16px;
            font-weight: 600;
            color: #1f2937;
        }

        .status {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
        }

        .pending {
            background: #fff7ed;
            color: #c2410c;
        }

        .confirmed {
            background: #ecfdf5;
            color: #15803d;
        }

        .completed {
            background: #eff6ff;
            color: #1d4ed8;
        }

        .cancelled {
            background: #fef2f2;
            color: #b91c1c;
        }

        .unknown {
            background: #f3f4f6;
            color: #4b5563;
        }

        @media (max-width: 900px) {

            .main {
                margin-left: 0;
                padding: 25px;
            }

            .details-grid {
                grid-template-columns: 1fr;
            }
        }

    </style>

</head>

<body>

<jsp:include page="dentist-sidebar.jsp" />

<div class="main">

    <a href="<%= request.getContextPath() %>/dentist-bookings"
       class="back-btn">
        ← Back to My Bookings
    </a>

    <div class="page-header">

        <h1>Appointment Details</h1>

        <p>
            View complete information about this appointment.
        </p>

    </div>


    <% if (error != null) { %>

        <div class="error-box">
            <%= error %>
        </div>

    <% } %>


    <% if (appointment != null) { %>

        <div class="details-card">


            <!-- APPOINTMENT INFORMATION -->

            <div class="section">

                <div class="section-title">
                    Appointment Information
                </div>

                <div class="details-grid">

                    <div class="detail-box">

                        <span class="label">
                            Appointment Number
                        </span>

                        <span class="value">

                            <%
                                if (appointment.getAppointmentNumber() != null &&
                                        !appointment.getAppointmentNumber().trim().isEmpty()) {
                            %>

                                <%= appointment.getAppointmentNumber() %>

                            <%
                                } else {
                            %>

                                #<%= appointment.getId() %>

                            <%
                                }
                            %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Appointment ID
                        </span>

                        <span class="value">
                            <%= appointment.getId() %>
                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Date
                        </span>

                        <span class="value">
                            <%= appointment.getAppointmentDate() != null
                                    ? appointment.getAppointmentDate()
                                    : "-" %>
                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Time
                        </span>

                        <span class="value">
                            <%= appointment.getAppointmentTime() != null
                                    ? appointment.getAppointmentTime()
                                    : "-" %>
                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Status
                        </span>

                        <%
                            String status =
                                    appointment.getStatus();

                            if (status == null) {
                                status = "UNKNOWN";
                            }

                            String statusClass =
                                    "unknown";

                            if ("PENDING".equalsIgnoreCase(status)) {
                                statusClass = "pending";
                            } else if ("CONFIRMED".equalsIgnoreCase(status)) {
                                statusClass = "confirmed";
                            } else if ("COMPLETED".equalsIgnoreCase(status)) {
                                statusClass = "completed";
                            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                                statusClass = "cancelled";
                            }
                        %>

                        <span class="status <%= statusClass %>">
                            <%= status %>
                        </span>

                    </div>

                </div>

            </div>


            <!-- PATIENT INFORMATION -->

            <div class="section">

                <div class="section-title">
                    Patient Information
                </div>

                <div class="details-grid">

                    <div class="detail-box">

                        <span class="label">
                            Patient ID
                        </span>

                        <span class="value">

                            <%= patient != null &&
                                    patient.getId() != null
                                    ? patient.getId()
                                    : "-" %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Patient Name
                        </span>

                        <span class="value">

                            <%= patient != null &&
                                    patient.getPatientName() != null
                                    ? patient.getPatientName()
                                    : "Not available" %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Contact Number
                        </span>

                        <span class="value">

                            <%= patient != null &&
                                    patient.getContactNumber() != null &&
                                    !patient.getContactNumber().trim().isEmpty()
                                    ? patient.getContactNumber()
                                    : "Not provided" %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Address
                        </span>

                        <span class="value">

                            <%= patient != null &&
                                    patient.getAddress() != null &&
                                    !patient.getAddress().trim().isEmpty()
                                    ? patient.getAddress()
                                    : "Not provided" %>

                        </span>

                    </div>

                </div>

            </div>


            <!-- TREATMENT INFORMATION -->

            <div class="section">

                <div class="section-title">
                    Treatment Information
                </div>

                <div class="details-grid">

                    <div class="detail-box">

                        <span class="label">
                            Treatment ID
                        </span>

                        <span class="value">

                            <%= treatment != null &&
                                    treatment.getId() != null
                                    ? treatment.getId()
                                    : "-" %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Treatment
                        </span>

                        <span class="value">

                            <%= treatment != null &&
                                    treatment.getTreatmentName() != null
                                    ? treatment.getTreatmentName()
                                    : "Not available" %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Treatment Cost
                        </span>

                        <span class="value">

                            <%= treatment != null &&
                                    treatment.getTreatmentCost() != null
                                    ? "LKR " + treatment.getTreatmentCost()
                                    : "Not available" %>

                        </span>

                    </div>

                </div>

            </div>


            <!-- DENTIST INFORMATION -->

            <div class="section">

                <div class="section-title">
                    Dentist Information
                </div>

                <div class="details-grid">

                    <div class="detail-box">

                        <span class="label">
                            Dentist
                        </span>

                        <span class="value">

                            <%= dentist != null &&
                                    dentist.getDentistName() != null
                                    ? dentist.getDentistName()
                                    : "Not available" %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Specialization
                        </span>

                        <span class="value">

                            <%= dentist != null &&
                                    dentist.getSpecialization() != null
                                    ? dentist.getSpecialization()
                                    : "Not available" %>

                        </span>

                    </div>


                    <div class="detail-box">

                        <span class="label">
                            Contact Number
                        </span>

                        <span class="value">

                            <%= dentist != null &&
                                    dentist.getContactNumber() != null &&
                                    !dentist.getContactNumber().trim().isEmpty()
                                    ? dentist.getContactNumber()
                                    : "Not provided" %>

                        </span>

                    </div>

                </div>

            </div>


        </div>

    <% } %>

</div>

</body>

</html>