<%@ page import="com.sunrise.dentalclinic.entity.Appointment" %>
<%@ page import="com.sunrise.dentalclinic.entity.Patient" %>
<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>
<%@ page import="com.sunrise.dentalclinic.entity.Treatment" %>

<%
String contextPath = request.getContextPath();


Appointment appointment = (Appointment) request.getAttribute("appointment");
Patient patient = (Patient) request.getAttribute("patient");
Dentist dentist = (Dentist) request.getAttribute("dentist");
Treatment treatment = (Treatment) request.getAttribute("treatment");


%>

<!DOCTYPE html>

<html>
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Appointment Details | Sunrise Dental Clinic</title>

<style>

    * {
        box-sizing: border-box;
    }

    body {
        margin: 0;
        padding: 0;
        font-family: "Segoe UI", Arial, sans-serif;
        background: #f4f7fb;
        color: #1e293b;
    }

    .page {
        min-height: 100vh;
        padding: 45px 25px;
    }

    .container {
        max-width: 950px;
        margin: 0 auto;
    }

    /* Header */

    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
    }

    .header-left h1 {
        margin: 0 0 6px;
        font-size: 30px;
        font-weight: 700;
        color: #123b55;
    }

    .header-left p {
        margin: 0;
        color: #64748b;
        font-size: 14px;
    }

    .appointment-number {
        background: #e8f7f7;
        color: #087f82;
        padding: 9px 15px;
        border-radius: 8px;
        font-size: 14px;
        font-weight: 700;
    }

    /* Main Card */

    .card {
        background: #ffffff;
        border: 1px solid #e2e8f0;
        border-radius: 16px;
        box-shadow: 0 8px 30px rgba(15, 61, 86, 0.08);
        overflow: hidden;
    }

    .card-top {
        padding: 24px 30px;
        border-bottom: 1px solid #edf2f7;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .card-title {
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .title-icon {
        width: 42px;
        height: 42px;
        border-radius: 10px;
        background: #e8f7f7;
        color: #159a9c;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 21px;
        font-weight: bold;
    }

    .card-title h2 {
        margin: 0;
        font-size: 19px;
        color: #123b55;
    }

    .card-title span {
        display: block;
        margin-top: 3px;
        font-size: 12px;
        color: #94a3b8;
    }

    /* Information Grid */

    .details {
        padding: 30px;
    }

    .section-title {
        font-size: 13px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.7px;
        color: #64748b;
        margin-bottom: 16px;
    }

    .info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 16px;
    }

    .info-box {
        border: 1px solid #e8edf3;
        background: #fbfcfe;
        border-radius: 11px;
        padding: 17px 18px;
        transition: 0.2s ease;
    }

    .info-box:hover {
        border-color: #cbd5e1;
        background: #ffffff;
    }

    .info-label {
        font-size: 12px;
        color: #94a3b8;
        margin-bottom: 7px;
        font-weight: 600;
    }

    .info-value {
        font-size: 15px;
        font-weight: 700;
        color: #1e293b;
        word-break: break-word;
    }

    .info-value.secondary {
        color: #64748b;
        font-weight: 600;
    }

    /* Appointment Schedule */

    .schedule {
        margin-top: 28px;
    }

    .schedule-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 16px;
    }

    .schedule-box {
        border-radius: 12px;
        padding: 20px;
        background: #f7fafc;
        border: 1px solid #e8edf3;
    }

    .schedule-label {
        font-size: 12px;
        color: #64748b;
        font-weight: 600;
        margin-bottom: 8px;
    }

    .schedule-value {
        font-size: 17px;
        font-weight: 700;
        color: #123b55;
    }

    /* Status */

    .status-box {
        margin-top: 28px;
        padding: 20px;
        border-radius: 12px;
        background: #f8fafc;
        border: 1px solid #e8edf3;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .status-label {
        font-size: 13px;
        color: #64748b;
        font-weight: 600;
    }

    .status {
        display: inline-flex;
        align-items: center;
        gap: 7px;
        padding: 7px 14px;
        border-radius: 30px;
        background: #dcfce7;
        color: #166534;
        font-size: 12px;
        font-weight: 700;
    }

    .status-dot {
        width: 7px;
        height: 7px;
        background: #22c55e;
        border-radius: 50%;
    }

    /* Footer */

    .card-footer {
        padding: 22px 30px;
        background: #fafbfd;
        border-top: 1px solid #edf2f7;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .appointment-id {
        font-size: 12px;
        color: #94a3b8;
    }

    .appointment-id strong {
        color: #64748b;
    }

    .buttons {
        display: flex;
        gap: 10px;
    }

    .button {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 11px 18px;
        border-radius: 8px;
        text-decoration: none;
        font-size: 13px;
        font-weight: 700;
        transition: all 0.2s ease;
    }

    .back {
        background: #e8edf3;
        color: #334155;
    }

    .back:hover {
        background: #dce3eb;
    }

    .edit {
        background: #159a9c;
        color: #ffffff;
        box-shadow: 0 4px 12px rgba(21, 154, 156, 0.2);
    }

    .edit:hover {
        background: #117f81;
        transform: translateY(-1px);
    }

    /* Not Found */

    .not-found-wrapper {
        padding: 55px 30px;
        text-align: center;
    }

    .not-found-icon {
        width: 60px;
        height: 60px;
        margin: 0 auto 18px;
        border-radius: 50%;
        background: #fee2e2;
        color: #b91c1c;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 25px;
        font-weight: bold;
    }

    .not-found {
        margin: 0 0 20px;
        color: #b91c1c;
        font-size: 16px;
        font-weight: 700;
    }

    /* Responsive */

    @media (max-width: 700px) {

        .page {
            padding: 25px 15px;
        }

        .page-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 15px;
        }

        .header-left h1 {
            font-size: 25px;
        }

        .card-top {
            padding: 20px;
        }

        .details {
            padding: 20px;
        }

        .info-grid,
        .schedule-grid {
            grid-template-columns: 1fr;
        }

        .card-footer {
            padding: 20px;
            flex-direction: column;
            align-items: stretch;
            gap: 18px;
        }

        .appointment-id {
            text-align: center;
        }

        .buttons {
            width: 100%;
        }

        .button {
            flex: 1;
        }

    }

</style>


</head>

<body>

<div class="page">


<div class="container">

    <%
        if (appointment == null) {
    %>

        <div class="card">

            <div class="not-found-wrapper">

                <div class="not-found-icon">
                    !
                </div>

                <p class="not-found">
                    Appointment not found.
                </p>

                <a class="button back"
                   href="<%= contextPath %>/appointments">
                    Back to Appointments
                </a>

            </div>

        </div>

    <%
        } else {
    %>

        <div class="page-header">

            <div class="header-left">

                <h1>Appointment Details</h1>

                <p>
                    View complete information about this appointment
                </p>

            </div>

            <div class="appointment-number">
                #<%= appointment.getAppointmentNumber() %>
            </div>

        </div>


        <div class="card">

            <div class="card-top">

                <div class="card-title">

                    <div class="title-icon">
                        +
                    </div>

                    <div>
                        <h2>Appointment Information</h2>
                        <span>Sunrise Dental Clinic</span>
                    </div>

                </div>

            </div>


            <div class="details">

                <div class="section-title">
                    Patient & Medical Information
                </div>


                <div class="info-grid">

                    <div class="info-box">

                        <div class="info-label">
                            Patient
                        </div>

                        <div class="info-value">

                            <%
                                if (patient != null) {
                            %>

                                <%= patient.getPatientName() %>

                            <%
                                } else {
                            %>

                                Patient ID:
                                <%= appointment.getPatientId() %>

                            <%
                                }
                            %>

                        </div>

                    </div>


                    <div class="info-box">

                        <div class="info-label">
                            Dentist
                        </div>

                        <div class="info-value">

                            <%
                                if (dentist != null) {
                            %>

                                <%= dentist.getDentistName() %>

                            <%
                                } else {
                            %>

                                Dentist ID:
                                <%= appointment.getDentistId() %>

                            <%
                                }
                            %>

                        </div>

                    </div>


                    <div class="info-box">

                        <div class="info-label">
                            Treatment
                        </div>

                        <div class="info-value">

                            <%
                                if (treatment != null) {
                            %>

                                <%= treatment.getTreatmentName() %>

                            <%
                                } else {
                            %>

                                Treatment ID:
                                <%= appointment.getTreatmentId() %>

                            <%
                                }
                            %>

                        </div>

                    </div>


                    <div class="info-box">

                        <div class="info-label">
                            Appointment Number
                        </div>

                        <div class="info-value secondary">
                            <%= appointment.getAppointmentNumber() %>
                        </div>

                    </div>

                </div>


                <div class="schedule">

                    <div class="section-title">
                        Appointment Schedule
                    </div>

                    <div class="schedule-grid">

                        <div class="schedule-box">

                            <div class="schedule-label">
                                Appointment Date
                            </div>

                            <div class="schedule-value">
                                <%= appointment.getAppointmentDate() %>
                            </div>

                        </div>


                        <div class="schedule-box">

                            <div class="schedule-label">
                                Appointment Time
                            </div>

                            <div class="schedule-value">

                                <%
                                    if (appointment.getAppointmentTime() != null) {
                                %>

                                    <%= appointment.getAppointmentTime()
                                            .toString()
                                            .substring(0, 5) %>

                                <%
                                    } else {
                                %>

                                    Not specified

                                <%
                                    }
                                %>

                            </div>

                        </div>

                    </div>

                </div>


                <div class="status-box">

                    <div class="status-label">
                        Appointment Status
                    </div>

                    <div class="status">

                        <span class="status-dot"></span>

                        <%= appointment.getStatus() %>

                    </div>

                </div>

            </div>


            <div class="card-footer">

                <div class="appointment-id">
                    Appointment ID:
                    <strong><%= appointment.getId() %></strong>
                </div>


                <div class="buttons">

                    <a class="button back"
                       href="<%= contextPath %>/appointments">

                        Back

                    </a>


                    <%
                        if (!"CANCELLED".equalsIgnoreCase(
                                appointment.getStatus())) {
                    %>

                        <a class="button edit"
                           href="<%= contextPath %>/appointments?action=edit&id=<%= appointment.getId() %>">

                            Edit Appointment

                        </a>

                    <%
                        }
                    %>

                </div>

            </div>

        </div>

    <%
        }
    %>

</div>

</div>

</body>
</html>
