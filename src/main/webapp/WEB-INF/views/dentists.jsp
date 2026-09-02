<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.sunrise.dentalclinic.entity.User" %>
<%@ page import="com.sunrise.dentalclinic.entity.Dentist" %>
<%@ page import="com.sunrise.dentalclinic.entity.DentistAvailability" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>


<%
    // =========================================================
    // SESSION / USER
    // =========================================================

    User user =
            (User) session.getAttribute("loggedInUser");

    if (user == null) {

        response.sendRedirect(
                request.getContextPath() + "/login"
        );

        return;
    }


    // =========================================================
    // DATA FROM SERVLET
    // =========================================================

    List<Dentist> dentists =
            (List<Dentist>) request.getAttribute("dentists");

    Map<Long, List<DentistAvailability>> availabilityMap =
            (Map<Long, List<DentistAvailability>>)
                    request.getAttribute("availabilityMap");


    String error =
            (String) request.getAttribute("error");


    if (dentists == null) {
        dentists = new ArrayList<>();
    }

    if (availabilityMap == null) {
        availabilityMap = new java.util.HashMap<>();
    }


    // =========================================================
    // USER DETAILS
    // =========================================================

    String username =
            user.getUsername();

    String role =
            user.getRole();


    // =========================================================
    // URLS
    // =========================================================

    String contextPath =
            request.getContextPath();

    String dashboardUrl =
            contextPath + "/dashboard";

    String dentistsUrl =
            contextPath + "/dentists";
%>


<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width,
                   initial-scale=1.0">

    <title>
        Dentists - Sunrise Dental Clinic
    </title>


    <style>

        /* =====================================================
           RESET
        ===================================================== */

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

            color: #243746;

            min-height: 100vh;
        }


        a {
            text-decoration: none;
        }


        /* =====================================================
           MAIN CONTENT
        ===================================================== */

        .main {

            margin-left: 250px;

            min-height: 100vh;

            padding: 28px 32px;
        }


        /* =====================================================
           TOP BAR
        ===================================================== */

        .topbar {

            display: flex;

            justify-content: space-between;

            align-items: center;

            gap: 20px;

            margin-bottom: 28px;
        }


        .page-title {

            font-size: 27px;

            font-weight: 700;

            color: #183b4d;
        }


        .page-subtitle {

            font-size: 13px;

            color: #71808c;

            margin-top: 5px;
        }


        .page-badge {

            display: inline-flex;

            align-items: center;

            gap: 7px;

            background: #e3f6f5;

            color: #137d80;

            padding:
                9px 14px;

            border-radius: 20px;

            font-size: 12px;

            font-weight: 600;
        }


        /* =====================================================
           BACK BUTTON
        ===================================================== */

        .back-btn {

            display: inline-flex;

            align-items: center;

            gap: 7px;

            background: white;

            color: #536773;

            border:
                1px solid #e2e8ec;

            padding:
                9px 13px;

            border-radius: 8px;

            font-size: 13px;

            font-weight: 600;

            margin-bottom: 20px;

            transition: 0.2s ease;
        }


        .back-btn:hover {

            border-color: #159a9c;

            color: #159a9c;
        }


        .back-btn svg {

            width: 16px;

            height: 16px;

            stroke: currentColor;

            fill: none;

            stroke-width: 1.8;

            stroke-linecap: round;

            stroke-linejoin: round;
        }


        /* =====================================================
           ERROR
        ===================================================== */

        .error-box {

            background: #fff1f1;

            border:
                1px solid #f1caca;

            color: #a83c3c;

            padding:
                12px 15px;

            border-radius: 8px;

            margin-bottom: 20px;

            font-size: 13px;

            font-weight: 500;
        }


        /* =====================================================
           CONTENT GRID
        ===================================================== */

        .content-grid {

            display: grid;

            grid-template-columns:
                340px minmax(0, 1fr);

            gap: 22px;

            align-items: start;
        }


        /* =====================================================
           CARD
        ===================================================== */

        .card {

            background: white;

            border-radius: 13px;

            border:
                1px solid #e7ecef;

            box-shadow:
                0 3px 14px
                rgba(
                    23,
                    52,
                    67,
                    0.05
                );

            overflow: hidden;
        }


        .card-header {

            padding:
                20px 21px;

            border-bottom:
                1px solid #edf1f3;
        }


        .card-title {

            font-size: 16px;

            font-weight: 700;

            color: #183b4d;
        }


        .card-description {

            font-size: 12px;

            color: #7a8992;

            margin-top: 5px;

            line-height: 1.5;
        }


        .card-body {

            padding: 21px;
        }


        /* =====================================================
           FORM
        ===================================================== */

        .form-group {

            margin-bottom: 16px;
        }


        .form-label {

            display: block;

            font-size: 12px;

            font-weight: 600;

            color: #445863;

            margin-bottom: 7px;
        }


        .form-input,
        .form-select {

            width: 100%;

            height: 42px;

            border:
                1px solid #dce4e8;

            border-radius: 8px;

            padding:
                0 12px;

            background: #ffffff;

            color: #304754;

            font-size: 13px;

            outline: none;

            transition:
                border-color 0.2s ease,
                box-shadow 0.2s ease;
        }


        .form-input:focus,
        .form-select:focus {

            border-color: #159a9c;

            box-shadow:
                0 0 0 3px
                rgba(
                    21,
                    154,
                    156,
                    0.10
                );
        }


        .form-select {

            cursor: pointer;
        }


        .submit-btn {

            width: 100%;

            height: 42px;

            border: none;

            border-radius: 8px;

            background: #159a9c;

            color: white;

            font-size: 13px;

            font-weight: 700;

            cursor: pointer;

            transition:
                background 0.2s ease,
                transform 0.2s ease;
        }


        .submit-btn:hover {

            background: #118789;

            transform: translateY(-1px);
        }


        /* =====================================================
           DENTIST LIST
        ===================================================== */

        .dentist-list {

            display: flex;

            flex-direction: column;

            gap: 15px;
        }


        .dentist-card {

            border:
                1px solid #e5ebee;

            border-radius: 11px;

            padding: 17px;

            transition:
                box-shadow 0.2s ease,
                border-color 0.2s ease;
        }


        .dentist-card:hover {

            border-color:
                #cbdfe1;

            box-shadow:
                0 4px 15px
                rgba(
                    20,
                    70,
                    80,
                    0.06
                );
        }


        .dentist-top {

            display: flex;

            justify-content: space-between;

            align-items: flex-start;

            gap: 15px;

            margin-bottom: 14px;
        }


        .dentist-main {

            display: flex;

            align-items: center;

            gap: 12px;

            min-width: 0;
        }


        .dentist-avatar {

            width: 45px;

            height: 45px;

            min-width: 45px;

            border-radius: 11px;

            background: #e5f5f5;

            color: #159a9c;

            display: flex;

            align-items: center;

            justify-content: center;
        }


        .dentist-avatar svg {

            width: 23px;

            height: 23px;

            stroke: currentColor;

            fill: none;

            stroke-width: 1.7;

            stroke-linecap: round;

            stroke-linejoin: round;
        }


        .dentist-name {

            font-size: 15px;

            font-weight: 700;

            color: #183b4d;

            margin-bottom: 4px;
        }


        .dentist-specialization {

            font-size: 12px;

            color: #75858e;
        }


        .dentist-id {

            background: #f0f4f6;

            color: #687a84;

            padding:
                5px 8px;

            border-radius: 6px;

            font-size: 10px;

            font-weight: 700;

            white-space: nowrap;
        }


        /* =====================================================
           DENTIST DETAILS
        ===================================================== */

        .dentist-details {

            display: grid;

            grid-template-columns:
                repeat(2, minmax(0, 1fr));

            gap: 10px;

            margin-bottom: 15px;
        }


        .detail-box {

            background: #f8fafb;

            border-radius: 8px;

            padding:
                10px 12px;
        }


        .detail-label {

            font-size: 10px;

            color: #84929a;

            margin-bottom: 4px;

            text-transform: uppercase;

            letter-spacing: 0.5px;
        }


        .detail-value {

            font-size: 12px;

            color: #3e525d;

            font-weight: 600;

            word-break: break-word;
        }


        /* =====================================================
           AVAILABILITY
        ===================================================== */

        .availability-section {

            border-top:
                1px solid #edf1f3;

            padding-top: 15px;
        }


        .availability-title {

            display: flex;

            align-items: center;

            gap: 7px;

            font-size: 12px;

            font-weight: 700;

            color: #435863;

            margin-bottom: 11px;
        }


        .availability-title svg {

            width: 16px;

            height: 16px;

            stroke: #159a9c;

            fill: none;

            stroke-width: 1.8;

            stroke-linecap: round;

            stroke-linejoin: round;
        }


        .schedule-list {

            display: flex;

            flex-wrap: wrap;

            gap: 7px;

            margin-bottom: 14px;
        }


        .schedule-item {

            background: #eef8f8;

            border:
                1px solid #d8eeee;

            border-radius: 7px;

            padding:
                7px 9px;

            font-size: 11px;

            color: #3c6268;

        }


        .schedule-day {

            font-weight: 700;

            color: #137d80;
        }


        .schedule-time {

            margin-left: 4px;

            color: #5e737a;
        }


        .no-schedule {

            color: #98a5ab;

            font-size: 11px;

            margin-bottom: 14px;
        }


        /* =====================================================
           AVAILABILITY FORM
        ===================================================== */

        .schedule-form {

            display: grid;

            grid-template-columns:
                1.1fr 1fr 1fr auto;

            gap: 8px;

            align-items: end;
        }


        .schedule-form .form-group {

            margin-bottom: 0;
        }


        .schedule-form .form-label {

            font-size: 10px;

            margin-bottom: 5px;
        }


        .schedule-form .form-input,
        .schedule-form .form-select {

            height: 38px;

            font-size: 11px;

            padding:
                0 9px;
        }


        .schedule-btn {

            height: 38px;

            padding:
                0 13px;

            border: none;

            border-radius: 7px;

            background: #183b4d;

            color: white;

            font-size: 11px;

            font-weight: 700;

            cursor: pointer;

            white-space: nowrap;

            transition: 0.2s ease;
        }


        .schedule-btn:hover {

            background: #159a9c;
        }


        /* =====================================================
           EMPTY STATE
        ===================================================== */

        .empty-state {

            text-align: center;

            padding:
                45px 20px;

            color: #8a989f;
        }


        .empty-icon {

            width: 48px;

            height: 48px;

            margin:
                0 auto 12px;

            border-radius: 12px;

            background: #eef4f5;

            color: #159a9c;

            display: flex;

            align-items: center;

            justify-content: center;
        }


        .empty-icon svg {

            width: 23px;

            height: 23px;

            stroke: currentColor;

            fill: none;

            stroke-width: 1.7;

            stroke-linecap: round;

            stroke-linejoin: round;
        }


        .empty-title {

            font-size: 14px;

            font-weight: 700;

            color: #536771;

            margin-bottom: 5px;
        }


        .empty-text {

            font-size: 12px;

            color: #8a989f;
        }


        /* =====================================================
           FOOTER
        ===================================================== */

        .page-footer {

            text-align: center;

            padding:
                25px 0 5px;

            color: #9aa6ac;

            font-size: 11px;
        }


        /* =====================================================
           RESPONSIVE
        ===================================================== */

        @media (max-width: 1100px) {

            .content-grid {

                grid-template-columns:
                    290px minmax(0, 1fr);
            }

            .schedule-form {

                grid-template-columns:
                    1fr 1fr;

            }

            .schedule-btn {

                width: 100%;
            }
        }


        @media (max-width: 950px) {

            .main {

                margin-left: 0;

                padding:
                    24px 20px;
            }

            .content-grid {

                grid-template-columns: 1fr;
            }
        }


        @media (max-width: 700px) {

            .main {

                margin-left: 0;

                padding:
                    20px 15px;
            }

            .topbar {

                align-items: flex-start;

                flex-direction: column;
            }

            .dentist-details {

                grid-template-columns: 1fr;
            }

            .schedule-form {

                grid-template-columns: 1fr;
            }

            .dentist-top {

                flex-direction: column;
            }
        }

    </style>

</head>


<body>


<!-- =========================================================
     COMMON SIDEBAR
========================================================= -->

<jsp:include page="sidebar.jsp" />



<!-- =========================================================
     MAIN CONTENT
========================================================= -->

<main class="main">


    <!-- TOP BAR -->

    <div class="topbar">


        <div>

            <h1 class="page-title">
                Dentist Management
            </h1>

            <p class="page-subtitle">
                Manage dentists and their availability schedules
            </p>

        </div>


        <div class="page-badge">

            <svg width="15"
                 height="15"
                 viewBox="0 0 24 24"
                 fill="none"
                 stroke="currentColor"
                 stroke-width="1.8"
                 stroke-linecap="round"
                 stroke-linejoin="round">

                <circle cx="12"
                        cy="7"
                        r="3"></circle>

                <path d="M5 20c.5-3.7 3-6 7-6s6.5 2.3 7 6"></path>

            </svg>

            Dentist Management

        </div>


    </div>



    <!-- BACK BUTTON -->

    <a href="<%= dashboardUrl %>"
       class="back-btn">

        <svg viewBox="0 0 24 24">

            <path d="M19 12H5"></path>

            <path d="M11 18l-6-6 6-6"></path>

        </svg>

        Back to Dashboard

    </a>



    <!-- ERROR -->

    <% if (error != null && !error.trim().isEmpty()) { %>

        <div class="error-box">

            <%= error %>

        </div>

    <% } %>



    <!-- =================================================
         CONTENT GRID
    ================================================= -->

    <div class="content-grid">


        <!-- =================================================
             ADD DENTIST
        ================================================= -->

        <section class="card">


            <div class="card-header">

                <div class="card-title">
                    Add New Dentist
                </div>

                <div class="card-description">
                    Register a new dentist in the clinic system.
                </div>

            </div>


            <div class="card-body">


                <form method="post"
                      action="<%= dentistsUrl %>">


                    <input type="hidden"
                           name="action"
                           value="addDentist">


                    <!-- Dentist Name -->

                    <div class="form-group">

                        <label class="form-label">
                            Dentist Name
                        </label>

                        <input
                                type="text"
                                name="dentistName"
                                class="form-input"
                                placeholder="Enter dentist name"
                                required
                        >

                    </div>


                    <!-- Specialization -->

                    <div class="form-group">

                        <label class="form-label">
                            Specialization
                        </label>

                        <select
                                name="specialization"
                                class="form-select"
                                required
                        >

                            <option value="">
                                Select specialization
                            </option>

                            <option value="General Dentistry">
                                General Dentistry
                            </option>

                            <option value="Orthodontics">
                                Orthodontics
                            </option>

                            <option value="Endodontics">
                                Endodontics
                            </option>

                            <option value="Periodontics">
                                Periodontics
                            </option>

                            <option value="Prosthodontics">
                                Prosthodontics
                            </option>

                            <option value="Pediatric Dentistry">
                                Pediatric Dentistry
                            </option>

                            <option value="Oral & Maxillofacial Surgery">
                                Oral & Maxillofacial Surgery
                            </option>

                            <option value="Oral Medicine">
                                Oral Medicine
                            </option>

                        </select>

                    </div>


                    <!-- Contact Number -->

                    <div class="form-group">

                        <label class="form-label">
                            Contact Number
                        </label>

                        <input
                                type="text"
                                name="contactNumber"
                                class="form-input"
                                placeholder="Enter contact number"
                                required
                        >

                    </div>


                    <!-- Submit -->

                    <button
                            type="submit"
                            class="submit-btn">

                        Add Dentist

                    </button>


                </form>


            </div>


        </section>



        <!-- =================================================
             REGISTERED DENTISTS
        ================================================= -->

        <section class="card">


            <div class="card-header">

                <div class="card-title">
                    Registered Dentists
                </div>

                <div class="card-description">
                    View registered dentists and manage their schedules.
                </div>

            </div>


            <div class="card-body">


                <% if (dentists.isEmpty()) { %>


                    <!-- EMPTY -->

                    <div class="empty-state">


                        <div class="empty-icon">

                            <svg viewBox="0 0 24 24">

                                <circle cx="12"
                                        cy="7"
                                        r="3"></circle>

                                <path d="M5 20c.5-3.7 3-6 7-6s6.5 2.3 7 6"></path>

                            </svg>

                        </div>


                        <div class="empty-title">
                            No Dentists Found
                        </div>


                        <div class="empty-text">
                            Add a dentist using the form to get started.
                        </div>


                    </div>


                <% } else { %>


                    <div class="dentist-list">


                        <% for (Dentist dentist : dentists) { %>


                            <div class="dentist-card">


                                <!-- =================================
                                     DENTIST HEADER
                                ================================== -->

                                <div class="dentist-top">


                                    <div class="dentist-main">


                                        <div class="dentist-avatar">

                                            <svg viewBox="0 0 24 24">

                                                <circle cx="12"
                                                        cy="7.5"
                                                        r="3"></circle>

                                                <path d="M5.5 20c.5-3.8 3-6 6.5-6s6 2.2 6.5 6"></path>

                                            </svg>

                                        </div>


                                        <div>

                                            <div class="dentist-name">

                                                <%= dentist.getDentistName() %>

                                            </div>


                                            <div class="dentist-specialization">

                                                <%= dentist.getSpecialization() %>

                                            </div>

                                        </div>


                                    </div>


                                    <div class="dentist-id">

                                        ID #<%= dentist.getId() %>

                                    </div>


                                </div>



                                <!-- =================================
                                     DETAILS
                                ================================== -->

                                <div class="dentist-details">


                                    <div class="detail-box">

                                        <div class="detail-label">
                                            Dentist ID
                                        </div>

                                        <div class="detail-value">
                                            #<%= dentist.getId() %>
                                        </div>

                                    </div>


                                    <div class="detail-box">

                                        <div class="detail-label">
                                            Contact Number
                                        </div>

                                        <div class="detail-value">

                                            <%= dentist.getContactNumber() %>

                                        </div>

                                    </div>


                                </div>



                                <!-- =================================
                                     AVAILABILITY
                                ================================== -->

                                <div class="availability-section">


                                    <div class="availability-title">


                                        <svg viewBox="0 0 24 24">

                                            <rect x="4"
                                                  y="5"
                                                  width="16"
                                                  height="15"
                                                  rx="2"></rect>

                                            <path d="M8 3v4"></path>

                                            <path d="M16 3v4"></path>

                                            <path d="M4 10h16"></path>

                                        </svg>


                                        Availability Schedule


                                    </div>


                                    <%
                                        List<DentistAvailability> schedules =
                                                availabilityMap.get(
                                                        dentist.getId()
                                                );

                                        if (schedules == null) {

                                            schedules =
                                                    new ArrayList<>();
                                        }
                                    %>


                                    <% if (schedules.isEmpty()) { %>


                                        <div class="no-schedule">

                                            No availability schedule added yet.

                                        </div>


                                    <% } else { %>


                                        <div class="schedule-list">


                                            <% for (
                                                DentistAvailability schedule
                                                : schedules
                                            ) { %>


                                                <div class="schedule-item">

                                                    <span class="schedule-day">

                                                        <%= schedule.getDayOfWeek() %>

                                                    </span>


                                                    <span class="schedule-time">

                                                        <%= schedule.getStartTime() %>
                                                        -
                                                        <%= schedule.getEndTime() %>

                                                    </span>

                                                </div>


                                            <% } %>


                                        </div>


                                    <% } %>



                                    <!-- =================================
                                         ADD SCHEDULE FORM
                                    ================================== -->

                                    <form
                                            method="post"
                                            action="<%= dentistsUrl %>"
                                            class="schedule-form"
                                    >


                                        <input
                                                type="hidden"
                                                name="action"
                                                value="addAvailability"
                                        >


                                        <input
                                                type="hidden"
                                                name="dentistId"
                                                value="<%= dentist.getId() %>"
                                        >


                                        <!-- Day -->

                                        <div class="form-group">

                                            <label class="form-label">
                                                Day
                                            </label>

                                            <select
                                                    name="dayOfWeek"
                                                    class="form-select"
                                                    required
                                            >

                                                <option value="">
                                                    Select Day
                                                </option>

                                                <option value="Monday">
                                                    Monday
                                                </option>

                                                <option value="Tuesday">
                                                    Tuesday
                                                </option>

                                                <option value="Wednesday">
                                                    Wednesday
                                                </option>

                                                <option value="Thursday">
                                                    Thursday
                                                </option>

                                                <option value="Friday">
                                                    Friday
                                                </option>

                                                <option value="Saturday">
                                                    Saturday
                                                </option>

                                                <option value="Sunday">
                                                    Sunday
                                                </option>

                                            </select>

                                        </div>


                                        <!-- Start -->

                                        <div class="form-group">

                                            <label class="form-label">
                                                Start Time
                                            </label>

                                            <input
                                                    type="time"
                                                    name="startTime"
                                                    class="form-input"
                                                    required
                                            >

                                        </div>


                                        <!-- End -->

                                        <div class="form-group">

                                            <label class="form-label">
                                                End Time
                                            </label>

                                            <input
                                                    type="time"
                                                    name="endTime"
                                                    class="form-input"
                                                    required
                                            >

                                        </div>


                                        <!-- Button -->

                                        <button
                                                type="submit"
                                                class="schedule-btn"
                                        >

                                            Add Schedule

                                        </button>


                                    </form>


                                </div>


                            </div>


                        <% } %>


                    </div>


                <% } %>


            </div>


        </section>


    </div>



    <!-- =========================================================
         FOOTER
    ========================================================== -->

    <div class="page-footer">

        © 2026 Sunrise Dental Clinic Management System

    </div>


</main>


</body>

</html>