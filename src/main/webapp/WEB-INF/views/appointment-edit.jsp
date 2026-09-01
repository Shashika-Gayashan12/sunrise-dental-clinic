<%@ page import="java.util.List" %>
<%@ page import="com.sunrise.dentalclinic.entity.Appointment" %>
<%@ page import="com.sunrise.dentalclinic.entity.Patient" %>
<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>
<%@ page import="com.sunrise.dentalclinic.entity.Treatment" %>

<%
String contextPath = request.getContextPath();


Appointment appointment =
        (Appointment) request.getAttribute("appointment");

List<Patient> patients =
        (List<Patient>) request.getAttribute("patients");

List<Dentist> dentists =
        (List<Dentist>) request.getAttribute("dentists");

List<Treatment> treatments =
        (List<Treatment>) request.getAttribute("treatments");


%>

<!DOCTYPE html>

<html>
<head>


<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>Edit Appointment | Sunrise Dental Clinic</title>

<style>

    * {
        box-sizing: border-box;
    }

    body {
        margin: 0;
        font-family: "Segoe UI", Arial, sans-serif;
        background: #f4f7fb;
        color: #1e293b;
    }

    .page {
        min-height: 100vh;
        padding: 40px 20px;
    }

    .container {
        max-width: 900px;
        margin: auto;
    }

    /* Header */

    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
    }

    .header-title h1 {
        margin: 0;
        color: #123b55;
        font-size: 30px;
        font-weight: 700;
    }

    .header-title p {
        margin: 7px 0 0;
        color: #64748b;
        font-size: 14px;
    }

    .appointment-number {
        padding: 9px 15px;
        border-radius: 8px;
        background: #e8f7f7;
        color: #087f82;
        font-size: 14px;
        font-weight: 700;
    }

    /* Card */

    .card {
        background: #ffffff;
        border: 1px solid #e2e8f0;
        border-radius: 16px;
        box-shadow: 0 8px 30px rgba(15, 61, 86, 0.08);
        overflow: hidden;
    }

    .card-header {
        padding: 23px 30px;
        border-bottom: 1px solid #edf2f7;
        display: flex;
        align-items: center;
        gap: 13px;
    }

    .icon {
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

    .card-header h2 {
        margin: 0;
        font-size: 19px;
        color: #123b55;
    }

    .card-header p {
        margin: 4px 0 0;
        color: #94a3b8;
        font-size: 12px;
    }

    /* Form */

    .form {
        padding: 30px;
    }

    .section-title {
        margin-bottom: 18px;
        font-size: 13px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.7px;
        color: #64748b;
    }

    .form-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
    }

    .form-group {
        display: flex;
        flex-direction: column;
    }

    .form-group.full {
        grid-column: 1 / -1;
    }

    label {
        margin-bottom: 8px;
        font-size: 13px;
        font-weight: 700;
        color: #475569;
    }

    .required {
        color: #ef4444;
    }

    input,
    select {
        width: 100%;
        height: 46px;
        padding: 0 13px;
        border: 1px solid #dbe2ea;
        border-radius: 8px;
        background: #ffffff;
        color: #1e293b;
        font-family: inherit;
        font-size: 14px;
        outline: none;
        transition: 0.2s ease;
    }

    input:focus,
    select:focus {
        border-color: #159a9c;
        box-shadow: 0 0 0 3px rgba(21, 154, 156, 0.10);
    }

    input[readonly] {
        background: #f8fafc;
        color: #64748b;
        cursor: not-allowed;
    }

    .help-text {
        margin-top: 6px;
        color: #94a3b8;
        font-size: 11px;
    }

    /* Status */

    .status-select {
        font-weight: 600;
    }

    /* Footer */

    .form-footer {
        margin-top: 30px;
        padding-top: 22px;
        border-top: 1px solid #edf2f7;
        display: flex;
        justify-content: flex-end;
        gap: 10px;
    }

    .button {
        height: 44px;
        padding: 0 20px;
        border-radius: 8px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        text-decoration: none;
        border: none;
        font-family: inherit;
        font-size: 13px;
        font-weight: 700;
        cursor: pointer;
        transition: 0.2s ease;
    }

    .cancel {
        background: #e8edf3;
        color: #334155;
    }

    .cancel:hover {
        background: #dce3eb;
    }

    .save {
        background: #159a9c;
        color: #ffffff;
        box-shadow: 0 4px 12px rgba(21, 154, 156, 0.20);
    }

    .save:hover {
        background: #117f81;
        transform: translateY(-1px);
    }

    /* Warning */

    .warning {
        margin-top: 20px;
        padding: 13px 15px;
        border-radius: 8px;
        background: #fff7ed;
        border: 1px solid #fed7aa;
        color: #9a3412;
        font-size: 12px;
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

        .header-title h1 {
            font-size: 25px;
        }

        .card-header,
        .form {
            padding: 20px;
        }

        .form-grid {
            grid-template-columns: 1fr;
        }

        .form-group.full {
            grid-column: auto;
        }

        .form-footer {
            flex-direction: column-reverse;
        }

        .button {
            width: 100%;
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

            <div class="form">

                <h2 style="color:#b91c1c;">
                    Appointment not found
                </h2>

                <a class="button cancel"
                   href="<%= contextPath %>/appointments">

                    Back to Appointments

                </a>

            </div>

        </div>

    <%
        } else {
    %>


        <!-- Page Header -->

        <div class="page-header">

            <div class="header-title">

                <h1>Edit Appointment</h1>

                <p>
                    Update the appointment information below
                </p>

            </div>

            <div class="appointment-number">

                #<%= appointment.getAppointmentNumber() %>

            </div>

        </div>


        <!-- Main Card -->

        <div class="card">

            <div class="card-header">

                <div class="icon">
                    +
                </div>

                <div>

                    <h2>
                        Appointment Information
                    </h2>

                    <p>
                        Modify appointment details
                    </p>

                </div>

            </div>


            <form class="form"
                  method="post"
                  action="<%= contextPath %>/appointments">

                <input type="hidden"
                       name="action"
                       value="update">

                <input type="hidden"
                       name="id"
                       value="<%= appointment.getId() %>">


                <div class="section-title">
                    Patient & Treatment
                </div>


                <div class="form-grid">


                    <!-- Patient -->

                    <div class="form-group">

                        <label for="patientId">
                            Patient
                            <span class="required">*</span>
                        </label>

                        <select id="patientId"
                                name="patientId"
                                required>

                            <option value="">
                                Select Patient
                            </option>

                            <%
                                if (patients != null) {

                                    for (Patient p : patients) {

                                        boolean selected =
                                                appointment.getPatientId() != null &&
                                                appointment.getPatientId().equals(p.getId());
                            %>

                                <option value="<%= p.getId() %>"
                                        <%= selected ? "selected" : "" %>>

                                    <%= p.getPatientName() %>

                                </option>

                            <%
                                    }
                                }
                            %>

                        </select>

                    </div>


                    <!-- Dentist -->

                    <div class="form-group">

                        <label for="dentistId">
                            Dentist
                            <span class="required">*</span>
                        </label>

                        <select id="dentistId"
                                name="dentistId"
                                required>

                            <option value="">
                                Select Dentist
                            </option>

                            <%
                                if (dentists != null) {

                                    for (Dentist d : dentists) {

                                        boolean selected =
                                                appointment.getDentistId() != null &&
                                                appointment.getDentistId().equals(d.getId());
                            %>

                                <option value="<%= d.getId() %>"
                                        <%= selected ? "selected" : "" %>>

                                    <%= d.getDentistName() %>

                                </option>

                            <%
                                    }
                                }
                            %>

                        </select>

                    </div>


                    <!-- Treatment -->

                    <div class="form-group full">

                        <label for="treatmentId">
                            Treatment
                            <span class="required">*</span>
                        </label>

                        <select id="treatmentId"
                                name="treatmentId"
                                required>

                            <option value="">
                                Select Treatment
                            </option>

                            <%
                                if (treatments != null) {

                                    for (Treatment t : treatments) {

                                        boolean selected =
                                                appointment.getTreatmentId() != null &&
                                                appointment.getTreatmentId().equals(t.getId());
                            %>

                                <option value="<%= t.getId() %>"
                                        <%= selected ? "selected" : "" %>>

                                    <%= t.getTreatmentName() %>

                                </option>

                            <%
                                    }
                                }
                            %>

                        </select>

                    </div>

                </div>


                <div class="section-title"
                     style="margin-top:30px;">

                    Appointment Schedule

                </div>


                <div class="form-grid">


                    <!-- Date -->

                    <div class="form-group">

                        <label for="appointmentDate">

                            Appointment Date
                            <span class="required">*</span>

                        </label>

                        <input type="date"
                               id="appointmentDate"
                               name="appointmentDate"
                               value="<%= appointment.getAppointmentDate() %>"
                               required>

                    </div>


                    <!-- Time -->

                    <div class="form-group">

                        <label for="appointmentTime">

                            Appointment Time
                            <span class="required">*</span>

                        </label>

                        <input type="time"
                               id="appointmentTime"
                               name="appointmentTime"
                               value="<%= appointment.getAppointmentTime() != null
                                       ? appointment.getAppointmentTime().toString()
                                       : "" %>"
                               required>

                    </div>


                    <!-- Status -->

                    <div class="form-group">

                        <label for="status">
                            Appointment Status
                        </label>

                        <select id="status"
                                name="status"
                                class="status-select">

                            <option value="PENDING"
                                <%= "PENDING".equalsIgnoreCase(appointment.getStatus())
                                        ? "selected" : "" %>>
                                Pending
                            </option>

                            <option value="CONFIRMED"
                                <%= "CONFIRMED".equalsIgnoreCase(appointment.getStatus())
                                        ? "selected" : "" %>>
                                Confirmed
                            </option>

                            <option value="COMPLETED"
                                <%= "COMPLETED".equalsIgnoreCase(appointment.getStatus())
                                        ? "selected" : "" %>>
                                Completed
                            </option>

                            <option value="CANCELLED"
                                <%= "CANCELLED".equalsIgnoreCase(appointment.getStatus())
                                        ? "selected" : "" %>>
                                Cancelled
                            </option>

                        </select>

                    </div>


                    <!-- Appointment Number -->

                    <div class="form-group">

                        <label for="appointmentNumber">

                            Appointment Number

                        </label>

                        <input type="text"
                               id="appointmentNumber"
                               value="<%= appointment.getAppointmentNumber() %>"
                               readonly>

                        <div class="help-text">
                            Appointment number cannot be changed.
                        </div>

                    </div>

                </div>


                <div class="warning">

                    Please review all appointment details before saving
                    the changes.

                </div>


                <!-- Buttons -->

                <div class="form-footer">

                    <a class="button cancel"
                       href="<%= contextPath %>/appointments">

                        Cancel

                    </a>

                    <button type="submit"
                            class="button save">

                        Save Changes

                    </button>

                </div>

            </form>

        </div>


    <%
        }
    %>

</div>


</div>

</body>
</html>

