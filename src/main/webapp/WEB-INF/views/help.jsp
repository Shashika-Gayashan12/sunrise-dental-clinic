<%@ page import="com.sunrise.dentalclinic.entity.User" %>

<%
    // =========================================================
    // GET LOGGED-IN USER
    // =========================================================

    User loggedInUser =
            (User) request.getAttribute("loggedInUser");

    if (loggedInUser == null) {

        response.sendRedirect(
                request.getContextPath() + "/login"
        );

        return;
    }

    String contextPath =
            request.getContextPath();

    String userRole =
            loggedInUser.getRole() != null
                    ? loggedInUser.getRole().toUpperCase()
                    : "";
%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Help & User Guide - Sunrise Dental Clinic
    </title>


    <style>

        * {
            box-sizing: border-box;
        }


        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f5f7fb;
            color: #1f2937;
        }


        /* =====================================================
           MAIN CONTENT
           ===================================================== */

        .main-content {

            margin-left: 250px;

            min-height: 100vh;

            padding: 30px 35px 40px;
        }


        /* =====================================================
           PAGE HEADER
           ===================================================== */

        .page-header {

            display: flex;

            justify-content: space-between;

            align-items: center;

            margin-bottom: 25px;
        }


        .page-title h1 {

            margin: 0 0 6px;

            font-size: 28px;

            color: #123b63;
        }


        .page-title p {

            margin: 0;

            color: #6b7280;

            font-size: 14px;
        }


        /* =====================================================
           PDF BUTTON
           ===================================================== */

        .download-btn {

            display: inline-flex;

            align-items: center;

            gap: 9px;

            padding: 12px 18px;

            background: #123b63;

            color: white;

            text-decoration: none;

            border-radius: 8px;

            font-size: 14px;

            font-weight: 600;

            transition: 0.2s ease;
        }


        .download-btn:hover {

            background: #0d2d4d;

            transform: translateY(-1px);
        }


        .download-btn svg {

            width: 18px;

            height: 18px;

            fill: none;

            stroke: currentColor;

            stroke-width: 2;

            stroke-linecap: round;

            stroke-linejoin: round;
        }


        /* =====================================================
           INTRO CARD
           ===================================================== */

        .intro-card {

            background: white;

            border-radius: 12px;

            padding: 25px;

            margin-bottom: 25px;

            border: 1px solid #e5e7eb;

            box-shadow: 0 2px 8px rgba(0,0,0,0.04);
        }


        .intro-card h2 {

            margin: 0 0 10px;

            color: #123b63;

            font-size: 20px;
        }


        .intro-card p {

            margin: 0;

            line-height: 1.7;

            color: #5b6472;

            font-size: 14px;
        }


        /* =====================================================
           GUIDE GRID
           ===================================================== */

        .guide-grid {

            display: grid;

            grid-template-columns:
                repeat(2, minmax(0, 1fr));

            gap: 20px;
        }


        /* =====================================================
           GUIDE CARD
           ===================================================== */

        .guide-card {

            background: white;

            border: 1px solid #e5e7eb;

            border-radius: 12px;

            padding: 22px;

            box-shadow:
                0 2px 8px rgba(0,0,0,0.04);

            transition: 0.2s ease;
        }


        .guide-card:hover {

            transform: translateY(-2px);

            box-shadow:
                0 5px 15px rgba(0,0,0,0.07);
        }


        .guide-heading {

            display: flex;

            align-items: center;

            gap: 13px;

            margin-bottom: 12px;
        }


        .guide-icon {

            width: 42px;

            height: 42px;

            border-radius: 10px;

            background: #eaf3fb;

            display: flex;

            align-items: center;

            justify-content: center;

            flex-shrink: 0;
        }


        .guide-icon svg {

            width: 21px;

            height: 21px;

            fill: none;

            stroke: #123b63;

            stroke-width: 2;

            stroke-linecap: round;

            stroke-linejoin: round;
        }


        .guide-heading h3 {

            margin: 0;

            font-size: 17px;

            color: #123b63;
        }


        .guide-card p {

            margin: 0 0 10px;

            color: #5b6472;

            font-size: 14px;

            line-height: 1.65;
        }


        .guide-card ol,
        .guide-card ul {

            margin: 8px 0 0;

            padding-left: 20px;

            color: #4b5563;

            font-size: 14px;

            line-height: 1.8;
        }


        .guide-card li {

            margin-bottom: 3px;
        }


        /* =====================================================
           ROLE CARD
           ===================================================== */

        .role-section {

            margin-top: 25px;
        }


        .section-title {

            font-size: 21px;

            color: #123b63;

            margin: 0 0 15px;
        }


        .role-grid {

            display: grid;

            grid-template-columns:
                repeat(3, minmax(0, 1fr));

            gap: 18px;
        }


        .role-card {

            background: white;

            border: 1px solid #e5e7eb;

            border-radius: 12px;

            padding: 20px;
        }


        .role-card h3 {

            margin: 0 0 9px;

            color: #123b63;

            font-size: 17px;
        }


        .role-card p {

            margin: 0;

            color: #5b6472;

            font-size: 13px;

            line-height: 1.65;
        }


        /* =====================================================
           WARNING / VALIDATION
           ===================================================== */

        .warning-card {

            margin-top: 25px;

            background: #fffaf0;

            border: 1px solid #f1dfb5;

            border-radius: 12px;

            padding: 22px;
        }


        .warning-card h2 {

            margin: 0 0 10px;

            color: #8a5a00;

            font-size: 19px;
        }


        .warning-card ul {

            margin: 0;

            padding-left: 20px;

            color: #6b5a36;

            font-size: 14px;

            line-height: 1.8;
        }


        /* =====================================================
           FOOTER
           ===================================================== */

        .help-footer {

            margin-top: 30px;

            padding-top: 20px;

            border-top: 1px solid #e5e7eb;

            text-align: center;

            color: #8a929d;

            font-size: 13px;
        }


        /* =====================================================
           RESPONSIVE
           ===================================================== */

        @media (max-width: 1000px) {

            .guide-grid {

                grid-template-columns: 1fr;
            }

            .role-grid {

                grid-template-columns: 1fr;
            }
        }


        @media (max-width: 700px) {

            .main-content {

                margin-left: 0;

                padding: 20px;
            }


            .page-header {

                flex-direction: column;

                align-items: flex-start;

                gap: 15px;
            }
        }

    </style>

</head>


<body>


<!-- =========================================================
     EXISTING SIDEBAR
     ========================================================= -->

<jsp:include page="sidebar.jsp" />


<!-- =========================================================
     MAIN CONTENT
     ========================================================= -->

<div class="main-content">


    <!-- =====================================================
         HEADER
         ===================================================== -->

    <div class="page-header">

        <div class="page-title">

            <h1>
                Help & User Guide
            </h1>

            <p>
                Learn how to use the Sunrise Dental Clinic Management System.
            </p>

        </div>


        <!-- PDF DOWNLOAD -->

        <a
                href="<%= contextPath %>/assets/UserGuide.pdf"
                class="download-btn"
                download
        >

            <svg viewBox="0 0 24 24">

                <path d="M12 3v12"></path>

                <path d="M7 10l5 5 5-5"></path>

                <path d="M5 21h14"></path>

            </svg>

            Download User Guide PDF

        </a>

    </div>


    <!-- =====================================================
         INTRODUCTION
         ===================================================== -->

    <div class="intro-card">

        <h2>
            Welcome to Sunrise Dental Clinic Management System
        </h2>

        <p>
            This Help section provides a step-by-step guide for
            using the main features of the system. Users can manage
            patients, appointments, dentists, treatments, billing
            and other clinic operations according to their assigned
            role and permissions.
        </p>

    </div>


    <!-- =====================================================
         BASIC USER GUIDE
         ===================================================== -->

    <div class="guide-grid">


        <!-- LOGIN -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <rect x="3" y="5"
                              width="18"
                              height="14"
                              rx="2"></rect>

                        <path d="M8 10h8"></path>

                        <path d="M8 14h5"></path>

                    </svg>

                </div>

                <h3>
                    1. Login
                </h3>

            </div>

            <p>
                Use your registered username and password to access
                the system.
            </p>

            <ol>
                <li>Open the Sunrise Dental Clinic system.</li>
                <li>Enter your username.</li>
                <li>Enter your password.</li>
                <li>Click the Login button.</li>
                <li>You will be redirected to the Dashboard.</li>
            </ol>

        </div>


        <!-- DASHBOARD -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <rect x="3" y="3"
                              width="7" height="7"></rect>

                        <rect x="14" y="3"
                              width="7" height="7"></rect>

                        <rect x="3" y="14"
                              width="7" height="7"></rect>

                        <rect x="14" y="14"
                              width="7" height="7"></rect>

                    </svg>

                </div>

                <h3>
                    2. Dashboard
                </h3>

            </div>

            <p>
                The Dashboard provides a quick overview of the
                clinic management system.
            </p>

            <ul>
                <li>View appointment statistics.</li>
                <li>View today's appointments.</li>
                <li>Access Patients.</li>
                <li>Access Appointments.</li>
                <li>Access Dentists and Treatments.</li>
                <li>Access Billing.</li>
            </ul>

        </div>


        <!-- PATIENT -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <circle cx="12" cy="8"
                                r="3"></circle>

                        <path d="M5 21c0-4 3-6 7-6s7 2 7 6"></path>

                    </svg>

                </div>

                <h3>
                    3. Patient Management
                </h3>

            </div>

            <p>
                Patient Management allows authorized users to
                maintain patient records.
            </p>

            <ol>
                <li>Open the Patients section.</li>
                <li>Register a new patient.</li>
                <li>Enter the required patient information.</li>
                <li>Save the patient record.</li>
                <li>Search and view existing patient records.</li>
            </ol>

        </div>


        <!-- APPOINTMENT -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <rect x="3" y="5"
                              width="18"
                              height="16"
                              rx="2"></rect>

                        <path d="M16 3v4"></path>

                        <path d="M8 3v4"></path>

                        <path d="M3 10h18"></path>

                    </svg>

                </div>

                <h3>
                    4. Appointments
                </h3>

            </div>

            <p>
                Appointments can be registered and managed using
                the appointment module.
            </p>

            <ol>
                <li>Select the patient.</li>
                <li>Select the dentist.</li>
                <li>Select the treatment.</li>
                <li>Select an available date and time.</li>
                <li>Confirm the appointment details.</li>
                <li>Save the appointment.</li>
            </ol>

        </div>


        <!-- DENTIST -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <circle cx="12" cy="7"
                                r="3"></circle>

                        <path d="M6 21c0-4 2.5-7 6-7s6 3 6 7"></path>

                    </svg>

                </div>

                <h3>
                    5. Dentist Management
                </h3>

            </div>

            <p>
                The Dentist section is used to manage dentist
                information and availability.
            </p>

            <ul>
                <li>Add dentist records.</li>
                <li>View dentist details.</li>
                <li>Update dentist information.</li>
                <li>Manage dentist availability.</li>
                <li>Check available appointment times.</li>
            </ul>

        </div>


        <!-- TREATMENT -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <path d="M7 4h10"></path>

                        <path d="M8 4v4"></path>

                        <path d="M16 4v4"></path>

                        <path d="M6 8h12"></path>

                        <path d="M7 8v8a5 5 0 0 0 10 0V8"></path>

                    </svg>

                </div>

                <h3>
                    6. Treatments
                </h3>

            </div>

            <p>
                Treatments contain the services provided by the
                dental clinic and their associated costs.
            </p>

            <ul>
                <li>View available treatments.</li>
                <li>Add new treatments.</li>
                <li>Update treatment details.</li>
                <li>Maintain treatment costs.</li>
            </ul>

        </div>


        <!-- AVAILABILITY -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <circle cx="12" cy="12"
                                r="9"></circle>

                        <path d="M12 7v5l3 2"></path>

                    </svg>

                </div>

                <h3>
                    7. Dentist Availability
                </h3>

            </div>

            <p>
                Dentist availability controls when appointments
                can be registered for a particular dentist.
            </p>

            <ol>
                <li>Select the dentist.</li>
                <li>Select the available day/date.</li>
                <li>Set the starting time.</li>
                <li>Set the ending time.</li>
                <li>Save the availability.</li>
            </ol>

        </div>


        <!-- BILLING -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <rect x="4" y="3"
                              width="16"
                              height="18"
                              rx="2"></rect>

                        <path d="M8 7h8"></path>

                        <path d="M8 11h8"></path>

                        <path d="M8 15h5"></path>

                    </svg>

                </div>

                <h3>
                    8. Billing
                </h3>

            </div>

            <p>
                Billing is used to calculate and manage the
                patient's treatment bill.
            </p>

            <ol>
                <li>Select the relevant appointment.</li>
                <li>Review the treatment cost.</li>
                <li>Enter or verify the consultation fee.</li>
                <li>Check the calculated total.</li>
                <li>Save the bill.</li>
                <li>Use the printable bill option when required.</li>
            </ol>

        </div>


        <!-- APPOINTMENT STATUS -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <path d="M5 12l4 4L19 6"></path>

                    </svg>

                </div>

                <h3>
                    9. Appointment Status
                </h3>

            </div>

            <p>
                Appointments can be monitored according to their
                current status.
            </p>

            <ul>
                <li>Pending</li>
                <li>Confirmed</li>
                <li>Completed</li>
                <li>Cancelled</li>
            </ul>

        </div>


        <!-- SEARCH -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <circle cx="10.5"
                                cy="10.5"
                                r="6.5"></circle>

                        <path d="M16 16l5 5"></path>

                    </svg>

                </div>

                <h3>
                    10. Search & View Records
                </h3>

            </div>

            <p>
                Use the available search and filter functions to
                quickly find required records.
            </p>

            <ul>
                <li>Search patient records.</li>
                <li>Search appointment information.</li>
                <li>Filter appointments by status.</li>
                <li>View appointment details.</li>
                <li>Review billing information.</li>
            </ul>

        </div>


<% if ("ADMIN".equals(userRole)) { %>

        <!-- USER MANAGEMENT -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <circle cx="9"
                                cy="8"
                                r="3"></circle>

                        <circle cx="17"
                                cy="9"
                                r="2"></circle>

                        <path d="M3 21c0-4 2-6 6-6s6 2 6 6"></path>

                        <path d="M15 15c3 0 5 2 5 6"></path>

                    </svg>

                </div>

                <h3>
                    11. Manage Users
                </h3>

            </div>

            <p>
                This section is available only to administrators
                and is used to manage system user accounts.
            </p>

            <ul>
                <li>Create user accounts.</li>
                <li>View registered users.</li>
                <li>Update user information.</li>
                <li>Manage user roles and status.</li>
                <li>Activate or deactivate accounts.</li>
            </ul>

        </div>

<% } %>


        <!-- LOGOUT -->

        <div class="guide-card">

            <div class="guide-heading">

                <div class="guide-icon">

                    <svg viewBox="0 0 24 24">

                        <path d="M10 17l5-5-5-5"></path>

                        <path d="M15 12H3"></path>

                        <path d="M21 3v18"></path>

                    </svg>

                </div>

                <h3>
                    12. Logout
                </h3>

            </div>

            <p>
                Always log out after completing your work,
                especially when using a shared computer.
            </p>

            <ol>
                <li>Go to the sidebar.</li>
                <li>Click Logout.</li>
                <li>The current session will be ended.</li>
                <li>You will be returned to the Login page.</li>
            </ol>

        </div>

    </div>


    <!-- =====================================================
         USER ROLES
         ===================================================== -->

    <div class="role-section">

        <h2 class="section-title">
            User Roles
        </h2>


        <div class="role-grid">


            <div class="role-card">

                <h3>
                    Administrator
                </h3>

                <p>
                    Administrators have access to system-wide
                    management functions, including user management
                    and the main clinic management modules.
                </p>

            </div>


            <div class="role-card">

                <h3>
                    Staff / User
                </h3>

                <p>
                    Staff users can perform the clinic operations
                    allowed by their assigned permissions, such as
                    managing patients, appointments and billing.
                </p>

            </div>


            <div class="role-card">

                <h3>
                    Dentist
                </h3>

                <p>
                    Dentist users can access the functions provided
                    for dentists and view appointments assigned to
                    their dentist account.
                </p>

            </div>

        </div>

    </div>


    <!-- =====================================================
         VALIDATION / COMMON ERRORS
         ===================================================== -->

    <div class="warning-card">

        <h2>
            Common Validation & Error Messages
        </h2>

        <ul>

            <li>
                Make sure all required fields are completed before
                submitting a form.
            </li>

            <li>
                Appointment dates cannot be selected from the past.
            </li>

            <li>
                An appointment can only be created during the
                dentist's available time.
            </li>

            <li>
                The system prevents conflicting active appointments
                for the same dentist and time.
            </li>

            <li>
                Check the selected patient, dentist and treatment
                before confirming an appointment.
            </li>

            <li>
                If a login fails, verify the username, password and
                account status.
            </li>

            <li>
                If an unexpected error occurs, contact the system
                administrator.
            </li>

        </ul>

    </div>


    <!-- =====================================================
         PDF NOTE
         ===================================================== -->

    <div class="intro-card" style="margin-top:25px;">

        <h2>
            User Guide PDF
        </h2>

        <p>
            A complete step-by-step User Guide is available as a
            downloadable PDF. The guide can be used by new users
            to learn the system before performing clinic operations.
            Click the <strong>Download User Guide PDF</strong>
            button at the top of this page to access it.
        </p>

    </div>


    <!-- =====================================================
         FOOTER
         ===================================================== -->

    <div class="help-footer">

        Sunrise Dental Clinic Management System
        &nbsp; | &nbsp;
        Help & User Guide

    </div>


</div>


</body>

</html>