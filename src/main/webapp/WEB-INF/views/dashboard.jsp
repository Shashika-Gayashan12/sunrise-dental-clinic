<%@ page import="com.sunrise.dentalclinic.entity.User" %>

<%
    String contextPath = request.getContextPath();

    User user =
            (User) request.getAttribute("loggedInUser");

    if (user == null) {
        response.sendRedirect(
                contextPath + "/login"
        );
        return;
    }

    Integer totalAppointmentsObj =
            (Integer) request.getAttribute("totalAppointments");

    Integer pendingAppointmentsObj =
            (Integer) request.getAttribute("pendingAppointments");

    Integer confirmedAppointmentsObj =
            (Integer) request.getAttribute("confirmedAppointments");

    Integer completedAppointmentsObj =
            (Integer) request.getAttribute("completedAppointments");

    Integer cancelledAppointmentsObj =
            (Integer) request.getAttribute("cancelledAppointments");

    Integer todayAppointmentsObj =
            (Integer) request.getAttribute("todayAppointments");


    int totalAppointments =
            totalAppointmentsObj == null ? 0 : totalAppointmentsObj;

    int pendingAppointments =
            pendingAppointmentsObj == null ? 0 : pendingAppointmentsObj;

    int confirmedAppointments =
            confirmedAppointmentsObj == null ? 0 : confirmedAppointmentsObj;

    int completedAppointments =
            completedAppointmentsObj == null ? 0 : completedAppointmentsObj;

    int cancelledAppointments =
            cancelledAppointmentsObj == null ? 0 : cancelledAppointmentsObj;

    int todayAppointments =
            todayAppointmentsObj == null ? 0 : todayAppointmentsObj;


    String dashboardUrl =
            contextPath + "/dashboard";

    String patientsUrl =
            contextPath + "/patients";

    String appointmentsUrl =
            contextPath + "/appointments";

    String dentistsUrl =
            contextPath + "/dentists";

    String treatmentsUrl =
            contextPath + "/treatments";

    String billsUrl =
            contextPath + "/bills";

    String logoutUrl =
            contextPath + "/logout";

    String manageUsersUrl =
            contextPath + "/manage-users";


    String username =
            user.getUsername() == null
                    ? "User"
                    : user.getUsername();

    String role =
            user.getRole() == null
                    ? "USER"
                    : user.getRole();


    String initial =
            username.trim().isEmpty()
                    ? "U"
                    : username
                        .trim()
                        .substring(0, 1)
                        .toUpperCase();
%>


<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
      content="width=device-width, initial-scale=1.0">

<title>
    Dashboard - Sunrise Dental Clinic
</title>


<style>

/* =====================================================
   GLOBAL
   ===================================================== */

* {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
}

html {
    scroll-behavior: smooth;
}

body {
    font-family:
        "Segoe UI",
        Arial,
        sans-serif;

    background: #f5f7fb;

    color: #1f2937;

    min-height: 100vh;
}

a {
    text-decoration: none;
}


/* =====================================================
   LAYOUT
   ===================================================== */

.layout {
    display: flex;
    min-height: 100vh;
}


/* =====================================================
   SIDEBAR
   ===================================================== */

.sidebar {
    width: 250px;

    background:
        linear-gradient(
            180deg,
            #0b3448 0%,
            #0f3d56 55%,
            #0a3145 100%
        );

    color: white;

    position: fixed;

    left: 0;
    top: 0;
    bottom: 0;

    display: flex;

    flex-direction: column;

    z-index: 100;

    overflow: hidden;
}


/* =====================================================
   BRAND
   ===================================================== */

.brand {
    padding: 28px 24px;

    border-bottom:
        1px solid
        rgba(255,255,255,0.10);

    flex-shrink: 0;
}

.brand-logo {
    width: 46px;
    height: 46px;

    border-radius: 12px;

    background: #159a9c;

    display: flex;

    align-items: center;
    justify-content: center;

    font-size: 18px;

    font-weight: 800;

    margin-bottom: 14px;

    box-shadow:
        0 5px 15px
        rgba(0,0,0,0.18);
}

.brand h1 {
    font-size: 18px;

    font-weight: 700;

    letter-spacing: 0.2px;

    margin-bottom: 5px;
}

.brand p {
    font-size: 11px;

    color: #a9d4d5;

    line-height: 1.5;
}


/* =====================================================
   SIDEBAR NAV
   ===================================================== */

.sidebar-nav {
    padding: 24px 14px;

    flex: 1;

    overflow-y: auto;
}

.nav-title {
    font-size: 10px;

    text-transform: uppercase;

    letter-spacing: 1.3px;

    color: #82b8bd;

    font-weight: 700;

    padding:
        0 12px 10px;
}

.nav-item {
    display: flex;

    align-items: center;

    gap: 13px;

    padding: 12px 13px;

    margin-bottom: 5px;

    border-radius: 9px;

    color: #c9e1e4;

    font-size: 14px;

    font-weight: 500;

    transition:
        background 0.2s ease,
        color 0.2s ease,
        transform 0.2s ease;
}

.nav-item:hover {
    background:
        rgba(255,255,255,0.08);

    color: white;

    transform:
        translateX(2px);
}

.nav-item.active {
    background: #159a9c;

    color: white;

    box-shadow:
        0 5px 15px
        rgba(21,154,156,0.25);
}


/* =====================================================
   SVG NAV ICONS
   ===================================================== */

.nav-icon {
    width: 25px;

    min-width: 25px;

    height: 22px;

    display: flex;

    align-items: center;

    justify-content: center;
}

.nav-icon svg {
    width: 18px;

    height: 18px;

    stroke: currentColor;

    fill: none;

    stroke-width: 1.8;

    stroke-linecap: round;

    stroke-linejoin: round;
}

.admin-nav {
    color: #8fe0d9;
}

.admin-nav:hover {
    color: white;
}


/* =====================================================
   LOGOUT
   ===================================================== */

.logout-btn {
    display: flex;

    align-items: center;

    gap: 13px;

    width: 100%;

    padding: 12px 13px;

    margin-bottom: 12px;

    border-radius: 9px;

    color: #c9e1e4;

    background: transparent;

    font-size: 14px;

    font-weight: 500;

    transition:
        background 0.2s ease,
        color 0.2s ease,
        transform 0.2s ease;
}

.logout-btn:hover {
    background:
        rgba(255,255,255,0.08);

    color: white;

    transform:
        translateX(2px);
}


/* =====================================================
   SIDEBAR FOOTER
   ===================================================== */

.sidebar-footer {
    padding: 18px 15px;

    border-top:
        1px solid
        rgba(255,255,255,0.10);

    flex-shrink: 0;
}

.sidebar-user {
    display: flex;

    align-items: center;

    gap: 11px;

    padding: 10px;

    border-radius: 10px;

    background:
        rgba(255,255,255,0.05);
}

.user-avatar {
    width: 38px;
    height: 38px;

    border-radius: 50%;

    background: #159a9c;

    display: flex;

    align-items: center;
    justify-content: center;

    font-size: 14px;

    font-weight: 700;

    flex-shrink: 0;
}

.user-details {
    min-width: 0;
}

.user-details strong {
    display: block;

    color: white;

    font-size: 13px;

    white-space: nowrap;

    overflow: hidden;

    text-overflow: ellipsis;
}

.user-details span {
    display: block;

    color: #91bec1;

    font-size: 11px;

    margin-top: 3px;
}


/* =====================================================
   MAIN
   ===================================================== */

.main {
    margin-left: 250px;

    width:
        calc(100% - 250px);

    min-height: 100vh;
}


/* =====================================================
   TOPBAR
   ===================================================== */

.topbar {
    height: 76px;

    background: white;

    border-bottom:
        1px solid #e8edf2;

    display: flex;

    align-items: center;

    justify-content: space-between;

    padding: 0 35px;

    position: sticky;

    top: 0;

    z-index: 50;
}

.page-title h2 {
    font-size: 20px;

    color: #0f3d56;

    margin-bottom: 3px;
}

.page-title p {
    font-size: 12px;

    color: #8a96a3;
}

.topbar-right {
    display: flex;

    align-items: center;

    gap: 15px;
}

.system-status {
    display: flex;

    align-items: center;

    gap: 7px;

    background: #ecfdf5;

    color: #047857;

    padding: 7px 11px;

    border-radius: 20px;

    font-size: 11px;

    font-weight: 600;
}

.status-dot {
    width: 7px;
    height: 7px;

    background: #10b981;

    border-radius: 50%;
}

.top-user {
    display: flex;

    align-items: center;

    gap: 9px;

    padding-left: 14px;

    border-left:
        1px solid #e5e7eb;
}

.top-avatar {
    width: 36px;
    height: 36px;

    background: #e8f7f7;

    color: #0f7779;

    border-radius: 50%;

    display: flex;

    align-items: center;
    justify-content: center;

    font-size: 13px;

    font-weight: 700;
}

.top-user-info strong {
    display: block;

    color: #334155;

    font-size: 12px;
}

.top-user-info span {
    display: block;

    color: #94a3b8;

    font-size: 10px;

    margin-top: 2px;
}


/* =====================================================
   CONTENT
   ===================================================== */

.content {
    padding: 32px 35px 45px;

    max-width: 1500px;

    margin: auto;
}


/* =====================================================
   WELCOME
   ===================================================== */

.welcome {
    background:
        linear-gradient(
            135deg,
            #0f3d56 0%,
            #14536d 55%,
            #159a9c 100%
        );

    border-radius: 15px;

    padding: 30px 32px;

    color: white;

    display: flex;

    align-items: center;

    justify-content: space-between;

    gap: 25px;

    margin-bottom: 30px;

    position: relative;

    overflow: hidden;

    box-shadow:
        0 10px 25px
        rgba(15,61,86,0.16);
}

.welcome::after {
    content: "";

    position: absolute;

    width: 220px;
    height: 220px;

    border-radius: 50%;

    border:
        1px solid
        rgba(255,255,255,0.10);

    right: -65px;

    top: -100px;
}

.welcome-text {
    position: relative;

    z-index: 2;
}

.welcome-label {
    font-size: 11px;

    text-transform: uppercase;

    letter-spacing: 1.3px;

    color: #9edfe0;

    font-weight: 700;

    margin-bottom: 8px;
}

.welcome h1 {
    font-size: 28px;

    margin-bottom: 7px;

    font-weight: 700;
}

.welcome p {
    color: #c5e4e5;

    font-size: 13px;

    line-height: 1.5;
}

.welcome-badge {
    position: relative;

    z-index: 2;

    background:
        rgba(255,255,255,0.10);

    border:
        1px solid
        rgba(255,255,255,0.16);

    padding: 18px 22px;

    border-radius: 12px;

    min-width: 150px;

    text-align: center;
}

.welcome-badge .big {
    display: block;

    font-size: 23px;

    font-weight: 700;

    margin-bottom: 4px;
}

.welcome-badge .small {
    color: #b8dfe1;

    font-size: 10px;

    text-transform: uppercase;

    letter-spacing: 0.8px;
}


/* =====================================================
   SECTION HEADER
   ===================================================== */

.section-header {
    display: flex;

    justify-content: space-between;

    align-items: center;

    margin-bottom: 17px;
}

.section-header h3 {
    color: #0f3d56;

    font-size: 17px;
}

.section-header span {
    color: #94a3b8;

    font-size: 11px;
}


/* =====================================================
   MANAGEMENT CARDS
   ===================================================== */

.cards {
    display: grid;

    grid-template-columns:
        repeat(
            auto-fit,
            minmax(210px, 1fr)
        );

    gap: 18px;
}

.card {
    background: white;

    border:
        1px solid #edf0f3;

    border-radius: 13px;

    padding: 23px;

    color: #1f2937;

    position: relative;

    overflow: hidden;

    transition:
        transform 0.2s ease,
        box-shadow 0.2s ease,
        border-color 0.2s ease;

    box-shadow:
        0 3px 12px
        rgba(15,61,86,0.045);
}

.card:hover {
    transform:
        translateY(-4px);

    border-color:
        #d5e9e9;

    box-shadow:
        0 12px 28px
        rgba(15,61,86,0.10);
}

.card-top {
    display: flex;

    align-items: center;

    justify-content: space-between;

    margin-bottom: 18px;
}


/* =====================================================
   SVG CARD ICON
   ===================================================== */

.card-icon {
    width: 45px;
    height: 45px;

    border-radius: 11px;

    background: #eaf8f8;

    color: #159a9c;

    display: flex;

    align-items: center;
    justify-content: center;
}

.card-icon svg {
    width: 21px;

    height: 21px;

    stroke: currentColor;

    fill: none;

    stroke-width: 1.8;

    stroke-linecap: round;

    stroke-linejoin: round;
}


/* =====================================================
   SVG CARD ARROW
   ===================================================== */

.card-arrow {
    width: 30px;
    height: 30px;

    border-radius: 50%;

    background: #f5f8fa;

    color: #94a3b8;

    display: flex;

    align-items: center;
    justify-content: center;

    transition: 0.2s;
}

.card-arrow svg {
    width: 15px;

    height: 15px;

    stroke: currentColor;

    fill: none;

    stroke-width: 1.8;

    stroke-linecap: round;

    stroke-linejoin: round;
}

.card:hover .card-arrow {
    background: #159a9c;

    color: white;
}

.card h4 {
    color: #0f3d56;

    font-size: 16px;

    margin-bottom: 7px;
}

.card p {
    color: #7b8794;

    font-size: 12px;

    line-height: 1.6;

    min-height: 38px;

    margin-bottom: 18px;
}

.card-link {
    color: #159a9c;

    font-size: 11px;

    font-weight: 700;

    text-transform: uppercase;

    letter-spacing: 0.5px;
}


/* =====================================================
   ADMIN CARD
   ===================================================== */

.admin-card {
    background:
        linear-gradient(
            135deg,
            #ffffff,
            #f2f8fa
        );

    border-color: #dcecef;
}

.admin-card .card-icon {
    background: #e7f0f5;

    color: #0f3d56;
}

.admin-card .card-link {
    color: #0f3d56;
}


/* =====================================================
   APPOINTMENT STATISTICS
   ===================================================== */

.stats-grid {
    display: grid;

    grid-template-columns:
        repeat(
            auto-fit,
            minmax(190px, 1fr)
        );

    gap: 16px;

    margin-bottom: 28px;
}

.stat-card {
    background: white;

    border:
        1px solid #edf0f3;

    border-radius: 13px;

    padding: 20px;

    display: flex;

    align-items: center;

    gap: 15px;

    box-shadow:
        0 3px 12px
        rgba(15,61,86,0.045);

    transition:
        transform 0.2s ease,
        box-shadow 0.2s ease;
}

.stat-card:hover {
    transform:
        translateY(-3px);

    box-shadow:
        0 10px 22px
        rgba(15,61,86,0.09);
}

.stat-icon {
    width: 45px;
    height: 45px;

    min-width: 45px;

    border-radius: 11px;

    background: #eaf8f8;

    color: #159a9c;

    display: flex;

    align-items: center;
    justify-content: center;
}

.stat-icon svg {
    width: 21px;

    height: 21px;

    stroke: currentColor;

    fill: none;

    stroke-width: 1.8;

    stroke-linecap: round;

    stroke-linejoin: round;
}

.pending-stat {
    background: #fff7d6;

    color: #956b00;
}

.confirmed-stat {
    background: #dcfce7;

    color: #166534;
}

.completed-stat {
    background: #e0f2fe;

    color: #0369a1;
}

.cancelled-stat {
    background: #fee2e2;

    color: #991b1b;
}

.today-stat {
    background: #e8f7f7;

    color: #0f7779;
}

.stat-content {
    min-width: 0;
}

.stat-label {
    display: block;

    color: #7b8794;

    font-size: 11px;

    line-height: 1.4;

    margin-bottom: 5px;
}

.stat-number {
    display: block;

    color: #0f3d56;

    font-size: 24px;

    font-weight: 700;
}


/* =====================================================
   INFO SECTION
   ===================================================== */

.info-section {
    display: grid;

    grid-template-columns:
        1fr 1fr;

    gap: 18px;

    margin-top: 28px;
}

.info-card {
    background: white;

    border:
        1px solid #edf0f3;

    border-radius: 13px;

    padding: 22px;

    box-shadow:
        0 3px 12px
        rgba(15,61,86,0.045);
}

.info-card h4 {
    color: #0f3d56;

    font-size: 14px;

    margin-bottom: 16px;
}

.info-row {
    display: flex;

    align-items: center;

    justify-content: space-between;

    padding: 10px 0;

    border-bottom:
        1px solid #f0f2f4;
}

.info-row:last-child {
    border-bottom: none;
}

.info-label {
    color: #7b8794;

    font-size: 12px;
}

.info-value {
    color: #334155;

    font-size: 12px;

    font-weight: 600;
}


/* =====================================================
   FOOTER
   ===================================================== */

footer {
    text-align: center;

    padding: 10px 20px 28px;

    color: #9aa5b1;

    font-size: 11px;
}


/* =====================================================
   MOBILE
   ===================================================== */

@media (max-width: 900px) {

    .sidebar {
        width: 215px;
    }

    .main {
        margin-left: 215px;

        width:
            calc(100% - 215px);
    }

    .content {
        padding: 25px;
    }

    .topbar {
        padding: 0 25px;
    }
}


@media (max-width: 700px) {

    .layout {
        display: block;
    }

    .sidebar {
        position: relative;

        width: 100%;

        min-height: auto;

        overflow: visible;
    }

    .brand {
        padding: 20px;
    }

    .sidebar-nav {
        padding: 12px 15px;

        display: flex;

        flex-wrap: wrap;

        gap: 5px;
    }

    .nav-title {
        width: 100%;

        padding-bottom: 5px;
    }

    .nav-item {
        margin: 0;

        padding: 9px 11px;
    }

    .sidebar-footer {
        display: none;
    }

    .main {
        margin-left: 0;

        width: 100%;
    }

    .topbar {
        height: auto;

        padding: 18px 20px;

        gap: 15px;
    }

    .top-user-info {
        display: none;
    }

    .content {
        padding: 20px;
    }

    .welcome {
        padding: 25px;

        flex-direction: column;

        align-items: flex-start;
    }

    .welcome h1 {
        font-size: 23px;
    }

    .welcome-badge {
        width: 100%;
    }

    .info-section {
        grid-template-columns: 1fr;
    }

    .stats-grid {
        grid-template-columns: 1fr 1fr;
    }
}


@media (max-width: 480px) {

    .cards {
        grid-template-columns: 1fr;
    }

    .stats-grid {
        grid-template-columns: 1fr;
    }

    .system-status {
        display: none;
    }

    .topbar {
        justify-content: flex-start;
    }
}

</style>

</head>


<body>


<div class="layout">


<!-- =====================================================
     SIDEBAR
     ===================================================== -->

<aside class="sidebar">


    <div class="brand">

        <div class="brand-logo">
            SD
        </div>

        <h1>
            Sunrise Dental Clinic
        </h1>

        <p>
            Dental Clinic Management System
        </p>

    </div>


    <nav class="sidebar-nav">


        <div class="nav-title">
            Main Menu
        </div>


        <!-- =================================================
             DASHBOARD
             ================================================= -->

        <a class="nav-item active"
           href="<%= dashboardUrl %>">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <rect x="3"
                          y="3"
                          width="7"
                          height="7"
                          rx="1"></rect>

                    <rect x="14"
                          y="3"
                          width="7"
                          height="7"
                          rx="1"></rect>

                    <rect x="3"
                          y="14"
                          width="7"
                          height="7"
                          rx="1"></rect>

                    <rect x="14"
                          y="14"
                          width="7"
                          height="7"
                          rx="1"></rect>

                </svg>

            </span>

            <span>
                Dashboard
            </span>

        </a>


        <!-- =================================================
             PATIENTS
             ================================================= -->

        <a class="nav-item"
           href="<%= patientsUrl %>">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <path d="M20 21a8 8 0 0 0-16 0"></path>

                    <circle cx="12"
                            cy="7"
                            r="4"></circle>

                </svg>

            </span>

            <span>
                Patients
            </span>

        </a>


        <!-- =================================================
             APPOINTMENTS
             ================================================= -->

        <a class="nav-item"
           href="<%= appointmentsUrl %>">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <rect x="3"
                          y="4"
                          width="18"
                          height="17"
                          rx="2"></rect>

                    <line x1="16"
                          y1="2"
                          x2="16"
                          y2="6"></line>

                    <line x1="8"
                          y1="2"
                          x2="8"
                          y2="6"></line>

                    <line x1="3"
                          y1="10"
                          x2="21"
                          y2="10"></line>

                    <line x1="8"
                          y1="14"
                          x2="8"
                          y2="14"></line>

                    <line x1="12"
                          y1="14"
                          x2="12"
                          y2="14"></line>

                    <line x1="16"
                          y1="14"
                          x2="16"
                          y2="14"></line>

                    <line x1="8"
                          y1="18"
                          x2="8"
                          y2="18"></line>

                    <line x1="12"
                          y1="18"
                          x2="12"
                          y2="18"></line>

                </svg>

            </span>

            <span>
                Appointments
            </span>

        </a>


        <!-- =================================================
             DENTISTS
             ================================================= -->

        <a class="nav-item"
           href="<%= dentistsUrl %>">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <circle cx="12"
                            cy="7"
                            r="4"></circle>

                    <path d="M5 21c0-4 3-7 7-7s7 3 7 7"></path>

                    <path d="M9 4.5c.7-.7 1.8-1.2 3-1.2"></path>

                    <path d="M9 17h6"></path>

                </svg>

            </span>

            <span>
                Dentists
            </span>

        </a>


        <!-- =================================================
             TREATMENTS
             ================================================= -->

        <a class="nav-item"
           href="<%= treatmentsUrl %>">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <rect x="7"
                          y="3"
                          width="10"
                          height="18"
                          rx="2"></rect>

                    <line x1="12"
                          y1="7"
                          x2="12"
                          y2="17"></line>

                    <line x1="8"
                          y1="12"
                          x2="16"
                          y2="12"></line>

                </svg>

            </span>

            <span>
                Treatments
            </span>

        </a>


        <!-- =================================================
             BILLING
             ================================================= -->

        <a class="nav-item"
           href="<%= billsUrl %>">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <path d="M6 3h12v18l-3-2-3 2-3-2-3 2V3z"></path>

                    <line x1="9"
                          y1="8"
                          x2="15"
                          y2="8"></line>

                    <line x1="9"
                          y1="12"
                          x2="15"
                          y2="12"></line>

                    <line x1="9"
                          y1="16"
                          x2="13"
                          y2="16"></line>

                </svg>

            </span>

            <span>
                Billing
            </span>

        </a>


        <% if ("ADMIN".equalsIgnoreCase(role)) { %>


            <div class="nav-title"
                 style="margin-top:18px;">

                Administration

            </div>


            <!-- =============================================
                 MANAGE USERS
                 ============================================= -->

            <a class="nav-item admin-nav"
               href="<%= manageUsersUrl %>">

                <span class="nav-icon">

                    <svg viewBox="0 0 24 24"
                         aria-hidden="true">

                        <circle cx="12"
                                cy="8"
                                r="3"></circle>

                        <path d="M5 21a7 7 0 0 1 14 0"></path>

                        <path d="M19 8v4"></path>

                        <path d="M17 10h4"></path>

                    </svg>

                </span>

                <span>
                    Manage Users
                </span>

            </a>


        <% } %>


    </nav>


    <!-- =====================================================
         SIDEBAR FOOTER
         ===================================================== -->

    <div class="sidebar-footer">


        <!-- =================================================
             LOGOUT
             ================================================= -->

        <a class="logout-btn"
           href="<%= logoutUrl %>">

            <span class="nav-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <path d="M10 17l5-5-5-5"></path>

                    <path d="M15 12H3"></path>

                    <path d="M21 3v18"></path>

                </svg>

            </span>

            <span>
                Logout
            </span>

        </a>


        <div class="sidebar-user">


            <div class="user-avatar">
                <%= initial %>
            </div>


            <div class="user-details">

                <strong>
                    <%= username %>
                </strong>

                <span>
                    <%= role %>
                </span>

            </div>


        </div>


    </div>


</aside>


<!-- =====================================================
     MAIN
     ===================================================== -->

<main class="main">


<header class="topbar">


    <div class="page-title">

        <h2>
            Dashboard
        </h2>

        <p>
            Overview of your dental clinic system
        </p>

    </div>


    <div class="topbar-right">


        <div class="system-status">

            <span class="status-dot"></span>

            System Online

        </div>


        <div class="top-user">


            <div class="top-avatar">
                <%= initial %>
            </div>


            <div class="top-user-info">

                <strong>
                    <%= username %>
                </strong>

                <span>
                    <%= role %>
                </span>

            </div>


        </div>


    </div>


</header>


<div class="content">


<!-- =====================================================
     WELCOME
     ===================================================== -->

<section class="welcome">


    <div class="welcome-text">


        <div class="welcome-label">
            Sunrise Dental Clinic
        </div>


        <h1>
            Welcome back,
            <%= username %>!
        </h1>


        <p>
            Manage patients, appointments,
            dentists, treatments and billing
            from one place.
        </p>


    </div>


    <div class="welcome-badge">

        <span class="big">
            <%= role %>
        </span>

        <span class="small">
            Current Role
        </span>

    </div>


</section>


<!-- =====================================================
     MANAGEMENT
     ===================================================== -->

<div class="section-header">

    <h3>
        Clinic Management
    </h3>

    <span>
        Quick access
    </span>

</div>


<div class="cards">


    <!-- =================================================
         PATIENTS
         ================================================= -->

    <a class="card"
       href="<%= patientsUrl %>">

        <div class="card-top">

            <div class="card-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <path d="M20 21a8 8 0 0 0-16 0"></path>

                    <circle cx="12"
                            cy="7"
                            r="4"></circle>

                </svg>

            </div>


            <div class="card-arrow">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <line x1="5"
                          y1="12"
                          x2="19"
                          y2="12"></line>

                    <polyline points="12 5 19 12 12 19"></polyline>

                </svg>

            </div>

        </div>


        <h4>
            Patients
        </h4>


        <p>
            Register new patients and
            manage existing patient
            information.
        </p>


        <span class="card-link">
            Manage Patients
        </span>

    </a>


    <!-- =================================================
         APPOINTMENTS
         ================================================= -->

    <a class="card"
       href="<%= appointmentsUrl %>">

        <div class="card-top">

            <div class="card-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <rect x="3"
                          y="4"
                          width="18"
                          height="17"
                          rx="2"></rect>

                    <line x1="16"
                          y1="2"
                          x2="16"
                          y2="6"></line>

                    <line x1="8"
                          y1="2"
                          x2="8"
                          y2="6"></line>

                    <line x1="3"
                          y1="10"
                          x2="21"
                          y2="10"></line>

                    <circle cx="8"
                            cy="15"
                            r="0.5"></circle>

                    <circle cx="12"
                            cy="15"
                            r="0.5"></circle>

                    <circle cx="16"
                            cy="15"
                            r="0.5"></circle>

                    <circle cx="8"
                            cy="18"
                            r="0.5"></circle>

                    <circle cx="12"
                            cy="18"
                            r="0.5"></circle>

                </svg>

            </div>


            <div class="card-arrow">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <line x1="5"
                          y1="12"
                          x2="19"
                          y2="12"></line>

                    <polyline points="12 5 19 12 12 19"></polyline>

                </svg>

            </div>

        </div>


        <h4>
            Appointments
        </h4>


        <p>
            Schedule, view and manage
            patient dental appointments.
        </p>


        <span class="card-link">
            Manage Appointments
        </span>

    </a>


    <!-- =================================================
         DENTISTS
         ================================================= -->

    <a class="card"
       href="<%= dentistsUrl %>">

        <div class="card-top">

            <div class="card-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <circle cx="12"
                            cy="7"
                            r="4"></circle>

                    <path d="M5 21c0-4 3-7 7-7s7 3 7 7"></path>

                    <path d="M9 17h6"></path>

                </svg>

            </div>


            <div class="card-arrow">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <line x1="5"
                          y1="12"
                          x2="19"
                          y2="12"></line>

                    <polyline points="12 5 19 12 12 19"></polyline>

                </svg>

            </div>

        </div>


        <h4>
            Dentists
        </h4>


        <p>
            Manage dentists,
            specializations and
            clinic availability.
        </p>


        <span class="card-link">
            Manage Dentists
        </span>

    </a>


    <!-- =================================================
         TREATMENTS
         ================================================= -->

    <a class="card"
       href="<%= treatmentsUrl %>">

        <div class="card-top">

            <div class="card-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <path d="M12 3v18"></path>

                    <path d="M3 12h18"></path>

                    <rect x="5"
                          y="5"
                          width="14"
                          height="14"
                          rx="2"></rect>

                </svg>

            </div>


            <div class="card-arrow">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <line x1="5"
                          y1="12"
                          x2="19"
                          y2="12"></line>

                    <polyline points="12 5 19 12 12 19"></polyline>

                </svg>

            </div>

        </div>


        <h4>
            Treatments
        </h4>


        <p>
            Manage dental treatments
            and their associated costs.
        </p>


        <span class="card-link">
            Manage Treatments
        </span>

    </a>


    <!-- =================================================
         BILLING
         ================================================= -->

    <a class="card"
       href="<%= billsUrl %>">

        <div class="card-top">

            <div class="card-icon">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <path d="M6 3h12v18l-3-2-3 2-3-2-3 2V3z"></path>

                    <line x1="9"
                          y1="8"
                          x2="15"
                          y2="8"></line>

                    <line x1="9"
                          y1="12"
                          x2="15"
                          y2="12"></line>

                    <line x1="9"
                          y1="16"
                          x2="13"
                          y2="16"></line>

                </svg>

            </div>


            <div class="card-arrow">

                <svg viewBox="0 0 24 24"
                     aria-hidden="true">

                    <line x1="5"
                          y1="12"
                          x2="19"
                          y2="12"></line>

                    <polyline points="12 5 19 12 12 19"></polyline>

                </svg>

            </div>

        </div>


        <h4>
            Billing
        </h4>


        <p>
            Create, view and manage
            patient billing records.
        </p>


        <span class="card-link">
            Manage Billing
        </span>

    </a>


    <!-- =================================================
         ADMIN
         ================================================= -->

    <% if ("ADMIN".equalsIgnoreCase(role)) { %>


        <a class="card admin-card"
           href="<%= manageUsersUrl %>">


            <div class="card-top">

                <div class="card-icon">

                    <svg viewBox="0 0 24 24"
                         aria-hidden="true">

                        <circle cx="12"
                                cy="8"
                                r="3"></circle>

                        <path d="M5 21a7 7 0 0 1 14 0"></path>

                        <path d="M19 8v4"></path>

                        <path d="M17 10h4"></path>

                    </svg>

                </div>


                <div class="card-arrow">

                    <svg viewBox="0 0 24 24"
                         aria-hidden="true">

                        <line x1="5"
                              y1="12"
                              x2="19"
                              y2="12"></line>

                        <polyline points="12 5 19 12 12 19"></polyline>

                    </svg>

                </div>

            </div>


            <h4>
                Manage Users
            </h4>


            <p>
                Approve, activate,
                deactivate and manage
                system user accounts.
            </p>


            <span class="card-link">
                Manage Users
            </span>


        </a>


    <% } %>


</div>


<!-- =====================================================
     APPOINTMENT OVERVIEW
     ===================================================== -->

<div class="section-header"
     style="margin-top:32px;">

    <h3>
        Appointment Overview
    </h3>

    <span>
        Live appointment statistics
    </span>

</div>


<div class="stats-grid">


    <!-- =================================================
         TOTAL
         ================================================= -->

    <a class="stat-card"
       href="<%= appointmentsUrl %>?filter=all">

        <div class="stat-icon">

            <svg viewBox="0 0 24 24"
                 aria-hidden="true">

                <rect x="3"
                      y="4"
                      width="18"
                      height="17"
                      rx="2"></rect>

                <line x1="16"
                      y1="2"
                      x2="16"
                      y2="6"></line>

                <line x1="8"
                      y1="2"
                      x2="8"
                      y2="6"></line>

                <line x1="3"
                      y1="10"
                      x2="21"
                      y2="10"></line>

            </svg>

        </div>

        <div class="stat-content">

            <span class="stat-label">
                Total Appointments
            </span>

            <strong class="stat-number">
                <%= totalAppointments %>
            </strong>

        </div>

    </a>


    <!-- =================================================
         PENDING
         ================================================= -->

    <a class="stat-card"
       href="<%= appointmentsUrl %>?filter=pending">

        <div class="stat-icon pending-stat">

            <svg viewBox="0 0 24 24"
                 aria-hidden="true">

                <circle cx="12"
                        cy="12"
                        r="9"></circle>

                <polyline points="12 7 12 12 15 14"></polyline>

            </svg>

        </div>

        <div class="stat-content">

            <span class="stat-label">
                Pending
            </span>

            <strong class="stat-number">
                <%= pendingAppointments %>
            </strong>

        </div>

    </a>


    <!-- =================================================
         CONFIRMED
         ================================================= -->

    <a class="stat-card"
       href="<%= appointmentsUrl %>?filter=confirmed">

        <div class="stat-icon confirmed-stat">

            <svg viewBox="0 0 24 24"
                 aria-hidden="true">

                <circle cx="12"
                        cy="12"
                        r="9"></circle>

                <polyline points="8 12 11 15 16 9"></polyline>

            </svg>

        </div>

        <div class="stat-content">

            <span class="stat-label">
                Confirmed
            </span>

            <strong class="stat-number">
                <%= confirmedAppointments %>
            </strong>

        </div>

    </a>


    <!-- =================================================
         COMPLETED
         ================================================= -->

    <a class="stat-card"
       href="<%= appointmentsUrl %>?filter=completed">

        <div class="stat-icon completed-stat">

            <svg viewBox="0 0 24 24"
                 aria-hidden="true">

                <path d="M20 6L9 17l-5-5"></path>

                <circle cx="12"
                        cy="12"
                        r="9"></circle>

            </svg>

        </div>

        <div class="stat-content">

            <span class="stat-label">
                Completed
            </span>

            <strong class="stat-number">
                <%= completedAppointments %>
            </strong>

        </div>

    </a>


    <!-- =================================================
         CANCELLED
         ================================================= -->

    <a class="stat-card"
       href="<%= appointmentsUrl %>?filter=cancelled">

        <div class="stat-icon cancelled-stat">

            <svg viewBox="0 0 24 24"
                 aria-hidden="true">

                <circle cx="12"
                        cy="12"
                        r="9"></circle>

                <line x1="9"
                      y1="9"
                      x2="15"
                      y2="15"></line>

                <line x1="15"
                      y1="9"
                      x2="9"
                      y2="15"></line>

            </svg>

        </div>

        <div class="stat-content">

            <span class="stat-label">
                Cancelled
            </span>

            <strong class="stat-number">
                <%= cancelledAppointments %>
            </strong>

        </div>

    </a>


    <!-- =================================================
         TODAY
         ================================================= -->

    <a class="stat-card"
       href="<%= appointmentsUrl %>?filter=today">

        <div class="stat-icon today-stat">

            <svg viewBox="0 0 24 24"
                 aria-hidden="true">

                <rect x="3"
                      y="4"
                      width="18"
                      height="17"
                      rx="2"></rect>

                <line x1="16"
                      y1="2"
                      x2="16"
                      y2="6"></line>

                <line x1="8"
                      y1="2"
                      x2="8"
                      y2="6"></line>

                <line x1="3"
                      y1="10"
                      x2="21"
                      y2="10"></line>

                <circle cx="12"
                        cy="15"
                        r="2"></circle>

            </svg>

        </div>

        <div class="stat-content">

            <span class="stat-label">
                Today's Appointments
            </span>

            <strong class="stat-number">
                <%= todayAppointments %>
            </strong>

        </div>

    </a>


</div>


<!-- =====================================================
     INFORMATION
     ===================================================== -->

<div class="info-section">


    <!-- =================================================
         SYSTEM INFORMATION
         ================================================= -->

    <div class="info-card">

        <h4>
            System Information
        </h4>


        <div class="info-row">

            <span class="info-label">
                Clinic
            </span>

            <span class="info-value">
                Sunrise Dental Clinic
            </span>

        </div>


        <div class="info-row">

            <span class="info-label">
                System Status
            </span>

            <span class="info-value">
                Active
            </span>

        </div>


        <div class="info-row">

            <span class="info-label">
                User Role
            </span>

            <span class="info-value">
                <%= role %>
            </span>

        </div>


    </div>


    <!-- =================================================
         ACCOUNT INFORMATION
         ================================================= -->

    <div class="info-card">

        <h4>
            Account Information
        </h4>


        <div class="info-row">

            <span class="info-label">
                Username
            </span>

            <span class="info-value">
                <%= username %>
            </span>

        </div>


        <div class="info-row">

            <span class="info-label">
                Account Status
            </span>

            <span class="info-value">
                Active
            </span>

        </div>


        <div class="info-row">

            <span class="info-label">
                Access Level
            </span>

            <span class="info-value">
                <%= role %>
            </span>

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