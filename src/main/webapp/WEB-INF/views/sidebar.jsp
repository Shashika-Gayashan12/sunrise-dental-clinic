<%@ page import="com.sunrise.dentalclinic.entity.User" %>

<%
    User sidebarUser = (User) request.getAttribute("loggedInUser");

    if (sidebarUser == null && session != null) {
        sidebarUser = (User) session.getAttribute("loggedInUser");
    }

    String sidebarContextPath = request.getContextPath();

    String sidebarUsername = sidebarUser != null
            ? sidebarUser.getUsername()
            : "User";

    String sidebarRole = sidebarUser != null
            ? sidebarUser.getRole()
            : "";

    String sidebarInitial = sidebarUsername != null &&
            !sidebarUsername.trim().isEmpty()
            ? sidebarUsername.trim().substring(0, 1).toUpperCase()
            : "U";

    String currentPath = request.getRequestURI();

    boolean dashboardActive =
            currentPath.endsWith("/dashboard");

    boolean patientsActive =
            currentPath.endsWith("/patients");

    boolean appointmentsActive =
            currentPath.endsWith("/appointments");

    boolean dentistsActive =
            currentPath.endsWith("/dentists");

    boolean treatmentsActive =
            currentPath.endsWith("/treatments");

    boolean billsActive =
            currentPath.endsWith("/bills");

    boolean manageUsersActive =
            currentPath.endsWith("/manage-users");
%>

<style>

    /* =========================================================
       COMMON SIDEBAR
       ========================================================= */

    .sidebar {
        position: fixed;
        left: 0;
        top: 0;
        bottom: 0;

        width: 250px;

        background: linear-gradient(
                180deg,
                #0b3448 0%,
                #0f3d56 55%,
                #0a3145 100%
        );

        color: #ffffff;

        display: flex;
        flex-direction: column;

        z-index: 1000;

        overflow: hidden;

        box-shadow: 4px 0 15px rgba(0, 0, 0, 0.08);
    }


    /* =========================================================
       BRAND
       ========================================================= */

    .sidebar-brand {
        padding: 25px 22px 22px;

        border-bottom: 1px solid rgba(255,255,255,0.10);
    }

    .sidebar-brand h2 {
        margin: 0;

        font-size: 22px;
        font-weight: 700;

        color: #ffffff;

        line-height: 1.2;
    }

    .sidebar-brand p {
        margin: 6px 0 0;

        font-size: 12px;

        color: rgba(255,255,255,0.65);

        line-height: 1.4;
    }


    /* =========================================================
       NAVIGATION
       ========================================================= */

    .sidebar-nav {
        flex: 1;

        padding: 20px 14px;

        overflow-y: auto;
        overflow-x: hidden;
    }

    .sidebar-nav::-webkit-scrollbar {
        width: 5px;
    }

    .sidebar-nav::-webkit-scrollbar-track {
        background: transparent;
    }

    .sidebar-nav::-webkit-scrollbar-thumb {
        background: rgba(255,255,255,0.15);
        border-radius: 10px;
    }


    .nav-section-title {
        padding: 10px 12px 8px;

        font-size: 10px;

        font-weight: 700;

        text-transform: uppercase;

        letter-spacing: 1px;

        color: rgba(255,255,255,0.45);
    }


    .nav-item {
        height: 43px;

        display: flex;

        align-items: center;

        gap: 13px;

        padding: 0 13px;

        margin-bottom: 5px;

        border-radius: 9px;

        white-space: nowrap;

        color: rgba(255,255,255,0.78);

        text-decoration: none;

        transition:
                background 0.2s ease,
                color 0.2s ease;
    }


    .nav-item:hover {
        background: rgba(255,255,255,0.08);

        color: #ffffff;
    }


    .nav-item.active {
        background: rgba(21,154,156,0.25);

        color: #ffffff;

        box-shadow:
                inset 3px 0 0 #159a9c;
    }


    /* =========================================================
       ICON
       ========================================================= */

    .nav-icon {
        width: 25px;

        min-width: 25px;

        height: 25px;

        display: flex;

        align-items: center;

        justify-content: center;

        flex-shrink: 0;
    }

    .nav-icon svg {
        width: 18px;

        height: 18px;

        display: block;

        stroke: currentColor;

        fill: none;

        stroke-width: 1.8;

        stroke-linecap: round;

        stroke-linejoin: round;
    }


    .nav-label {
        font-size: 14px;

        font-weight: 500;
    }


    /* =========================================================
       ADMIN SECTION
       ========================================================= */

    .admin-section {
        margin-top: 18px;
    }


    /* =========================================================
       SIDEBAR FOOTER
       ========================================================= */

    .sidebar-footer {
        padding: 15px 14px;

        border-top: 1px solid rgba(255,255,255,0.10);
    }


    .sidebar-user {
        display: flex;

        align-items: center;

        gap: 11px;

        padding: 9px 10px;

        margin-bottom: 8px;
    }


    .user-avatar {
        width: 35px;

        height: 35px;

        min-width: 35px;

        border-radius: 50%;

        background: #159a9c;

        color: #ffffff;

        display: flex;

        align-items: center;

        justify-content: center;

        font-size: 14px;

        font-weight: 700;
    }


    .user-details {
        min-width: 0;
    }


    .user-name {
        font-size: 13px;

        font-weight: 600;

        color: #ffffff;

        white-space: nowrap;

        overflow: hidden;

        text-overflow: ellipsis;
    }


    .user-role {
        margin-top: 2px;

        font-size: 10px;

        color: rgba(255,255,255,0.55);

        text-transform: uppercase;
    }


    .logout-link {
        height: 40px;

        display: flex;

        align-items: center;

        gap: 13px;

        padding: 0 13px;

        border-radius: 9px;

        color: rgba(255,255,255,0.70);

        text-decoration: none;

        font-size: 13px;

        transition:
                background 0.2s ease,
                color 0.2s ease;
    }


    .logout-link:hover {
        background: rgba(255,255,255,0.08);

        color: #ffffff;
    }


    /* =========================================================
       RESPONSIVE
       ========================================================= */

    @media (max-width: 950px) {

        .sidebar {
            position: relative;

            width: 100%;

            height: auto;

            min-height: auto;
        }

        .sidebar-nav {
            max-height: 450px;
        }

    }

</style>


<!-- =========================================================
     SIDEBAR
     ========================================================= -->

<aside class="sidebar">

    <!-- BRAND -->
    <div class="sidebar-brand">

        <h2>Sunrise Dental</h2>

        <p>Dental Clinic Management System</p>

    </div>


    <!-- NAVIGATION -->
    <nav class="sidebar-nav">

        <!-- MAIN -->
        <div class="nav-section-title">
            Main
        </div>


        <!-- DASHBOARD -->
        <a
                href="<%= sidebarContextPath %>/dashboard"
                class="nav-item <%= dashboardActive ? "active" : "" %>"
        >

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <rect
                            x="3"
                            y="3"
                            width="7"
                            height="7"
                            rx="1"
                    />

                    <rect
                            x="14"
                            y="3"
                            width="7"
                            height="7"
                            rx="1"
                    />

                    <rect
                            x="3"
                            y="14"
                            width="7"
                            height="7"
                            rx="1"
                    />

                    <rect
                            x="14"
                            y="14"
                            width="7"
                            height="7"
                            rx="1"
                    />

                </svg>

            </span>

            <span class="nav-label">
                Dashboard
            </span>

        </a>


        <!-- PATIENTS -->
        <a
                href="<%= sidebarContextPath %>/patients"
                class="nav-item <%= patientsActive ? "active" : "" %>"
        >

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <path
                            d="M20 21a8 8 0 0 0-16 0"
                    />

                    <circle
                            cx="12"
                            cy="7"
                            r="4"
                    />

                </svg>

            </span>

            <span class="nav-label">
                Patients
            </span>

        </a>


        <!-- APPOINTMENTS -->
        <a
                href="<%= sidebarContextPath %>/appointments"
                class="nav-item <%= appointmentsActive ? "active" : "" %>"
        >

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <rect
                            x="3"
                            y="4"
                            width="18"
                            height="17"
                            rx="2"
                    />

                    <line
                            x1="16"
                            y1="2"
                            x2="16"
                            y2="6"
                    />

                    <line
                            x1="8"
                            y1="2"
                            x2="8"
                            y2="6"
                    />

                    <line
                            x1="3"
                            y1="10"
                            x2="21"
                            y2="10"
                    />

                    <line
                            x1="8"
                            y1="14"
                            x2="8"
                            y2="14"
                    />

                    <line
                            x1="12"
                            y1="14"
                            x2="12"
                            y2="14"
                    />

                    <line
                            x1="16"
                            y1="14"
                            x2="16"
                            y2="14"
                    />

                </svg>

            </span>

            <span class="nav-label">
                Appointments
            </span>

        </a>


        <!-- DENTISTS -->
        <a
                href="<%= sidebarContextPath %>/dentists"
                class="nav-item <%= dentistsActive ? "active" : "" %>"
        >

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <circle
                            cx="12"
                            cy="7"
                            r="4"
                    />

                    <path
                            d="M5 21a7 7 0 0 1 14 0"
                    />

                </svg>

            </span>

            <span class="nav-label">
                Dentists
            </span>

        </a>


        <!-- TREATMENTS -->
        <a
                href="<%= sidebarContextPath %>/treatments"
                class="nav-item <%= treatmentsActive ? "active" : "" %>"
        >

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <path
                            d="M7 3h10"
                    />

                    <path
                            d="M9 3v6l-4 8a3 3 0 0 0 2.7 4.3h8.6A3 3 0 0 0 19 17l-4-8V3"
                    />

                    <line
                            x1="8"
                            y1="15"
                            x2="16"
                            y2="15"
                    />

                </svg>

            </span>

            <span class="nav-label">
                Treatments
            </span>

        </a>


        <!-- BILLING -->
        <a
                href="<%= sidebarContextPath %>/bills"
                class="nav-item <%= billsActive ? "active" : "" %>"
        >

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <rect
                            x="3"
                            y="4"
                            width="18"
                            height="17"
                            rx="2"
                    />

                    <line
                            x1="7"
                            y1="8"
                            x2="17"
                            y2="8"
                    />

                    <line
                            x1="7"
                            y1="12"
                            x2="17"
                            y2="12"
                    />

                    <line
                            x1="7"
                            y1="16"
                            x2="13"
                            y2="16"
                    />

                </svg>

            </span>

            <span class="nav-label">
                Billing
            </span>

        </a>


        <!-- ADMINISTRATION -->
        <% if ("ADMIN".equalsIgnoreCase(sidebarRole)) { %>

            <div class="admin-section">

                <div class="nav-section-title">
                    Administration
                </div>


                <!-- MANAGE USERS -->
                <a
                        href="<%= sidebarContextPath %>/manage-users"
                        class="nav-item <%= manageUsersActive ? "active" : "" %>"
                >

                    <span class="nav-icon">

                        <svg viewBox="0 0 24 24">

                            <circle
                                    cx="9"
                                    cy="7"
                                    r="4"
                            />

                            <path
                                    d="M2 21a7 7 0 0 1 14 0"
                            />

                            <path
                                    d="M19 8v6"
                            />

                            <path
                                    d="M16 11h6"
                            />

                        </svg>

                    </span>

                    <span class="nav-label">
                        Manage Users
                    </span>

                </a>

            </div>

        <% } %>

    </nav>


    <!-- FOOTER -->
    <div class="sidebar-footer">

        <div class="sidebar-user">

            <div class="user-avatar">
                <%= sidebarInitial %>
            </div>

            <div class="user-details">

                <div class="user-name">
                    <%= sidebarUsername %>
                </div>

                <div class="user-role">
                    <%= sidebarRole %>
                </div>

            </div>

        </div>


        <!-- LOGOUT -->
        <a
                href="<%= sidebarContextPath %>/logout"
                class="logout-link"
        >

            <span class="nav-icon">

                <svg viewBox="0 0 24 24">

                    <path
                            d="M10 17l5-5-5-5"
                    />

                    <path
                            d="M15 12H3"
                    />

                    <path
                            d="M21 19V5a2 2 0 0 0-2-2h-6"
                    />

                </svg>

            </span>

            <span>
                Logout
            </span>

        </a>

    </div>

</aside>