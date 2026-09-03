<%@ page import="com.sunrise.dentalclinic.entity.User" %>

<%
User dentistSidebarUser =
(User) session.getAttribute("loggedInUser");

String dentistSidebarContextPath =
        request.getContextPath();

String dentistSidebarUsername =
        dentistSidebarUser != null
                ? dentistSidebarUser.getUsername()
                : "Doctor";

String dentistSidebarInitial =
        dentistSidebarUsername != null &&
        !dentistSidebarUsername.trim().isEmpty()
                ? dentistSidebarUsername.trim()
                    .substring(0, 1)
                    .toUpperCase()
                : "D";

String dentistCurrentPath =
        request.getRequestURI();

boolean dentistDashboardActive =
        dentistCurrentPath.endsWith("/dentist-dashboard");

boolean dentistProfileActive =
        dentistCurrentPath.endsWith("/dentist-profile");

boolean dentistBookingsActive =
        dentistCurrentPath.endsWith("/dentist-bookings");


%>

<style>

    .dentist-sidebar {
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

        box-shadow:
                4px 0 15px rgba(0,0,0,0.08);
    }


    .dentist-sidebar-brand {

        padding: 25px 22px 22px;

        border-bottom:
                1px solid rgba(255,255,255,0.10);
    }


    .dentist-sidebar-brand h2 {

        margin: 0;

        font-size: 22px;

        font-weight: 700;

        color: #ffffff;

        line-height: 1.2;
    }


    .dentist-sidebar-brand p {

        margin: 6px 0 0;

        font-size: 12px;

        color: rgba(255,255,255,0.65);

        line-height: 1.4;
    }


    .dentist-sidebar-nav {

        flex: 1;

        padding: 20px 14px;

        overflow-y: auto;
    }


    .dentist-sidebar-nav::-webkit-scrollbar {

        width: 5px;
    }


    .dentist-sidebar-nav::-webkit-scrollbar-track {

        background: transparent;
    }


    .dentist-sidebar-nav::-webkit-scrollbar-thumb {

        background:
                rgba(255,255,255,0.15);

        border-radius: 10px;
    }


    .dentist-nav-section-title {

        padding:
                10px 12px 8px;

        font-size: 10px;

        font-weight: 700;

        text-transform: uppercase;

        letter-spacing: 1px;

        color:
                rgba(255,255,255,0.45);
    }


    .dentist-nav-item {

        height: 43px;

        display: flex;

        align-items: center;

        gap: 13px;

        padding: 0 13px;

        margin-bottom: 5px;

        border-radius: 9px;

        white-space: nowrap;

        color:
                rgba(255,255,255,0.78);

        text-decoration: none;

        transition:
                background 0.2s ease,
                color 0.2s ease;
    }


    .dentist-nav-item:hover {

        background:
                rgba(255,255,255,0.08);

        color: #ffffff;
    }


    .dentist-nav-item.active {

        background:
                rgba(21,154,156,0.25);

        color: #ffffff;

        box-shadow:
                inset 3px 0 0 #159a9c;
    }


    .dentist-nav-icon {

        width: 25px;

        min-width: 25px;

        height: 25px;

        display: flex;

        align-items: center;

        justify-content: center;

        flex-shrink: 0;
    }


    .dentist-nav-icon svg {

        width: 18px;

        height: 18px;

        display: block;

        stroke: currentColor;

        fill: none;

        stroke-width: 1.8;

        stroke-linecap: round;

        stroke-linejoin: round;
    }


    .dentist-nav-label {

        font-size: 14px;

        font-weight: 500;
    }


    .dentist-sidebar-footer {

        padding: 15px 14px;

        border-top:
                1px solid rgba(255,255,255,0.10);
    }


    .dentist-sidebar-user {

        display: flex;

        align-items: center;

        gap: 11px;

        padding: 9px 10px;

        margin-bottom: 8px;
    }


    .dentist-user-avatar {

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


    .dentist-user-details {

        min-width: 0;
    }


    .dentist-user-name {

        font-size: 13px;

        font-weight: 600;

        color: #ffffff;

        white-space: nowrap;

        overflow: hidden;

        text-overflow: ellipsis;
    }


    .dentist-user-role {

        margin-top: 2px;

        font-size: 10px;

        color:
                rgba(255,255,255,0.55);

        text-transform: uppercase;
    }


    .dentist-logout-link {

        height: 40px;

        display: flex;

        align-items: center;

        gap: 13px;

        padding: 0 13px;

        border-radius: 9px;

        color:
                rgba(255,255,255,0.70);

        text-decoration: none;

        font-size: 13px;

        transition:
                background 0.2s ease,
                color 0.2s ease;
    }


    .dentist-logout-link:hover {

        background:
                rgba(255,255,255,0.08);

        color: #ffffff;
    }


    @media (max-width: 950px) {

        .dentist-sidebar {

            position: relative;

            width: 100%;

            height: auto;

            min-height: auto;
        }

        .dentist-sidebar-nav {

            max-height: 450px;
        }
    }

</style>

<!-- =========================================================
     DENTIST SIDEBAR
     ========================================================= -->

<aside class="dentist-sidebar">


<!-- BRAND -->

<div class="dentist-sidebar-brand">

    <h2>
        Sunrise Dental
    </h2>

    <p>
        Dentist Portal
    </p>

</div>


<!-- NAVIGATION -->

<nav class="dentist-sidebar-nav">


    <div class="dentist-nav-section-title">
        Main
    </div>


    <!-- DASHBOARD -->

    <a
            href="<%= dentistSidebarContextPath %>/dentist-dashboard"
            class="dentist-nav-item
            <%= dentistDashboardActive ? "active" : "" %>"
    >

        <span class="dentist-nav-icon">

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

        <span class="dentist-nav-label">
            Dashboard
        </span>

    </a>


    <!-- MY PROFILE -->

    <a
            href="<%= dentistSidebarContextPath %>/dentist-profile"
            class="dentist-nav-item
            <%= dentistProfileActive ? "active" : "" %>"
    >

        <span class="dentist-nav-icon">

            <svg viewBox="0 0 24 24">

                <circle
                        cx="12"
                        cy="8"
                        r="4"
                />

                <path
                        d="M4 21a8 8 0 0 1 16 0"
                />

            </svg>

        </span>

        <span class="dentist-nav-label">
            My Profile
        </span>

    </a>


    <!-- MY BOOKINGS -->

    <a
            href="<%= dentistSidebarContextPath %>/dentist-bookings"
            class="dentist-nav-item
            <%= dentistBookingsActive ? "active" : "" %>"
    >

        <span class="dentist-nav-icon">

            <svg viewBox="0 0 24 24">

                <rect
                        x="3"
                        y="4"
                        width="18"
                        height="17"
                        rx="2"
                />

                <line
                        x1="8"
                        y1="2"
                        x2="8"
                        y2="6"
                />

                <line
                        x1="16"
                        y1="2"
                        x2="16"
                        y2="6"
                />

                <line
                        x1="3"
                        y1="10"
                        x2="21"
                        y2="10"
                />

                <path
                        d="M8 15l2 2 5-5"
                />

            </svg>

        </span>

        <span class="dentist-nav-label">
            My Bookings
        </span>

    </a>

</nav>


<!-- FOOTER -->

<div class="dentist-sidebar-footer">


    <div class="dentist-sidebar-user">

        <div class="dentist-user-avatar">

            <%= dentistSidebarInitial %>

        </div>


        <div class="dentist-user-details">

            <div class="dentist-user-name">

                <%= dentistSidebarUsername %>

            </div>

            <div class="dentist-user-role">

                DENTIST

            </div>

        </div>

    </div>


    <!-- LOGOUT -->

    <a
            href="<%= dentistSidebarContextPath %>/logout"
            class="dentist-logout-link"
    >

        <span class="dentist-nav-icon">

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
