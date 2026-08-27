package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // ============================================================
        // CHECK LOGIN SESSION
        // ============================================================

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        // ============================================================
        // GET LOGGED-IN USER
        // ============================================================

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        User user = (User) userObject;

        // ============================================================
        // CHECK USER STATUS
        // ============================================================

        if (!"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        // ============================================================
        // UPDATE SESSION ROLE
        // ============================================================

        session.setAttribute(
                "role",
                user.getRole()
        );

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        String contextPath =
                request.getContextPath().trim();

        StringBuilder html =
                new StringBuilder();

        // ============================================================
        // URLS
        // ============================================================

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

        // ============================================================
        // HTML START
        // ============================================================

        html.append("""
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

                .nav-icon {
                    width: 25px;

                    min-width: 25px;

                    text-align: center;

                    font-size: 16px;
                }

                .admin-nav {
                    color: #8fe0d9;
                }

                .admin-nav:hover {
                    color: white;
                }

                /* =====================================================
                   LOGOUT BUTTON
                   Dashboard ONLY
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

                .logout-btn .nav-icon {
                    width: 25px;

                    min-width: 25px;

                    text-align: center;

                    font-size: 16px;
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
                   MAIN AREA
                   ===================================================== */

                .main {
                    margin-left: 250px;

                    width:
                        calc(100% - 250px);

                    min-height: 100vh;
                }

                /* =====================================================
                   TOP BAR
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

                .status {
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
                            minmax(230px, 1fr)
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

                .card-icon {
                    width: 45px;
                    height: 45px;

                    border-radius: 11px;

                    background: #eaf8f8;

                    color: #159a9c;

                    display: flex;

                    align-items: center;
                    justify-content: center;

                    font-size: 20px;

                    font-weight: 700;
                }

                .card-arrow {
                    width: 30px;
                    height: 30px;

                    border-radius: 50%;

                    background: #f5f8fa;

                    color: #94a3b8;

                    display: flex;

                    align-items: center;
                    justify-content: center;

                    font-size: 15px;

                    transition: 0.2s;
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
                   QUICK INFO
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

                }

                @media (max-width: 480px) {

                    .cards {
                        grid-template-columns: 1fr;
                    }

                    .status {
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
                """);

        // ============================================================
        // SIDEBAR
        // ============================================================

        html.append("""
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
                """);

        // ============================================================
        // DASHBOARD
        // ============================================================

        html.append(
                "<a class=\"nav-item active\" href=\"" +
                        dashboardUrl +
                        "\">"
        );

        html.append("""
                            <span class="nav-icon">
                                ▦
                            </span>

                            <span>
                                Dashboard
                            </span>

                        </a>
                """);

        // ============================================================
        // PATIENTS
        // ============================================================

        html.append(
                "<a class=\"nav-item\" href=\"" +
                        patientsUrl +
                        "\">"
        );

        html.append("""
                            <span class="nav-icon">
                                ♙
                            </span>

                            <span>
                                Patients
                            </span>

                        </a>
                """);

        // ============================================================
        // APPOINTMENTS
        // ============================================================

        html.append(
                "<a class=\"nav-item\" href=\"" +
                        appointmentsUrl +
                        "\">"
        );

        html.append("""
                            <span class="nav-icon">
                                ◷
                            </span>

                            <span>
                                Appointments
                            </span>

                        </a>
                """);

        // ============================================================
        // DENTISTS
        // ============================================================

        html.append(
                "<a class=\"nav-item\" href=\"" +
                        dentistsUrl +
                        "\">"
        );

        html.append("""
                            <span class="nav-icon">
                                ♢
                            </span>

                            <span>
                                Dentists
                            </span>

                        </a>
                """);

        // ============================================================
        // TREATMENTS
        // ============================================================

        html.append(
                "<a class=\"nav-item\" href=\"" +
                        treatmentsUrl +
                        "\">"
        );

        html.append("""
                            <span class="nav-icon">
                                ✚
                            </span>

                            <span>
                                Treatments
                            </span>

                        </a>
                """);

        // ============================================================
        // BILLING
        // ============================================================

        html.append(
                "<a class=\"nav-item\" href=\"" +
                        billsUrl +
                        "\">"
        );

        html.append("""
                            <span class="nav-icon">
                                ▤
                            </span>

                            <span>
                                Billing
                            </span>

                        </a>
                """);

        // ============================================================
        // ADMIN ONLY
        // ============================================================

        if ("ADMIN".equalsIgnoreCase(
                user.getRole())) {

            html.append("""
                        <div class="nav-title"
                             style="margin-top:18px;">
                            Administration
                        </div>
                    """);

            html.append(
                    "<a class=\"nav-item admin-nav\" href=\"" +
                            manageUsersUrl +
                            "\">"
            );

            html.append("""
                            <span class="nav-icon">
                                ⚙
                            </span>

                            <span>
                                Manage Users
                            </span>

                        </a>
                    """);
        }

        // ============================================================
        // SIDEBAR END + LOGOUT
        // ============================================================

        html.append("""
                    </nav>

                    <div class="sidebar-footer">

                        <!-- Logout is intentionally shown
                             on Dashboard only -->

                        <a class="logout-btn"
                           href="
                """);

        html.append(logoutUrl);

        html.append("""
                           ">

                            <span class="nav-icon">
                                ↪
                            </span>

                            <span>
                                Logout
                            </span>

                        </a>

                        <div class="sidebar-user">

                            <div class="user-avatar">
                """);

        // ============================================================
        // SIDEBAR USER INITIAL
        // ============================================================

        html.append(
                getInitial(
                        user.getUsername()
                )
        );

        html.append("""
                            </div>

                            <div class="user-details">

                                <strong>
                """);

        html.append(
                escapeHtml(
                        user.getUsername()
                )
        );

        html.append("""
                                </strong>

                                <span>
                """);

        html.append(
                escapeHtml(
                        user.getRole()
                )
        );

        html.append("""
                                </span>

                            </div>

                        </div>

                    </div>

                </aside>
                """);

        // ============================================================
        // MAIN
        // ============================================================

        html.append("""
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

                            <div class="status">

                                <span class="status-dot"></span>

                                System Online

                            </div>

                            <div class="top-user">

                                <div class="top-avatar">
                """);

        html.append(
                getInitial(
                        user.getUsername()
                )
        );

        html.append("""
                                </div>

                                <div class="top-user-info">

                                    <strong>
                """);

        html.append(
                escapeHtml(
                        user.getUsername()
                )
        );

        html.append("""
                                    </strong>

                                    <span>
                """);

        html.append(
                escapeHtml(
                        user.getRole()
                )
        );

        html.append("""
                                    </span>

                                </div>

                            </div>

                        </div>

                    </header>

                    <div class="content">

                """);

        // ============================================================
        // WELCOME
        // ============================================================

        html.append("""
                        <section class="welcome">

                            <div class="welcome-text">

                                <div class="welcome-label">
                                    Sunrise Dental Clinic
                                </div>

                                <h1>
                                    Welcome back,
                """);

        html.append(
                escapeHtml(
                        user.getUsername()
                )
        );

        html.append("""
                                    !
                                </h1>

                                <p>
                                    Manage patients, appointments,
                                    dentists, treatments and billing
                                    from one place.
                                </p>

                            </div>

                            <div class="welcome-badge">

                                <span class="big">
                """);

        html.append(
                escapeHtml(
                        user.getRole()
                )
        );

        html.append("""
                                </span>

                                <span class="small">
                                    Current Role
                                </span>

                            </div>

                        </section>
                """);

        // ============================================================
        // MANAGEMENT TITLE
        // ============================================================

        html.append("""
                        <div class="section-header">

                            <h3>
                                Clinic Management
                            </h3>

                            <span>
                                Quick access
                            </span>

                        </div>

                        <div class="cards">
                """);

        // ============================================================
        // PATIENTS CARD
        // ============================================================

        html.append(
                "<a class=\"card\" href=\"" +
                        patientsUrl +
                        "\">"
        );

        html.append("""
                            <div class="card-top">

                                <div class="card-icon">
                                    ♙
                                </div>

                                <div class="card-arrow">
                                    →
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
                                Manage Patients →
                            </span>

                        </a>
                """);

        // ============================================================
        // APPOINTMENTS CARD
        // ============================================================

        html.append(
                "<a class=\"card\" href=\"" +
                        appointmentsUrl +
                        "\">"
        );

        html.append("""
                            <div class="card-top">

                                <div class="card-icon">
                                    ◷
                                </div>

                                <div class="card-arrow">
                                    →
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
                                Manage Appointments →
                            </span>

                        </a>
                """);

        // ============================================================
        // DENTISTS CARD
        // ============================================================

        html.append(
                "<a class=\"card\" href=\"" +
                        dentistsUrl +
                        "\">"
        );

        html.append("""
                            <div class="card-top">

                                <div class="card-icon">
                                    ♢
                                </div>

                                <div class="card-arrow">
                                    →
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
                                Manage Dentists →
                            </span>

                        </a>
                """);

        // ============================================================
        // TREATMENTS CARD
        // ============================================================

        html.append(
                "<a class=\"card\" href=\"" +
                        treatmentsUrl +
                        "\">"
        );

        html.append("""
                            <div class="card-top">

                                <div class="card-icon">
                                    ✚
                                </div>

                                <div class="card-arrow">
                                    →
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
                                Manage Treatments →
                            </span>

                        </a>
                """);

        // ============================================================
        // BILLING CARD
        // ============================================================

        html.append(
                "<a class=\"card\" href=\"" +
                        billsUrl +
                        "\">"
        );

        html.append("""
                            <div class="card-top">

                                <div class="card-icon">
                                    ▤
                                </div>

                                <div class="card-arrow">
                                    →
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
                                Manage Billing →
                            </span>

                        </a>
                """);

        // ============================================================
        // ADMIN CARD
        // ============================================================

        if ("ADMIN".equalsIgnoreCase(
                user.getRole())) {

            html.append(
                    "<a class=\"card admin-card\" href=\"" +
                            manageUsersUrl +
                            "\">"
            );

            html.append("""
                                <div class="card-top">

                                    <div class="card-icon">
                                        ⚙
                                    </div>

                                    <div class="card-arrow">
                                        →
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
                                    Manage Users →
                                </span>

                            </a>
                    """);
        }

        // ============================================================
        // INFO SECTION
        // ============================================================

        html.append("""
                        </div>

                        <div class="info-section">

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
                """);

        html.append(
                escapeHtml(
                        user.getRole()
                )
        );

        html.append("""
                                    </span>

                                </div>

                            </div>

                            <div class="info-card">

                                <h4>
                                    Account Information
                                </h4>

                                <div class="info-row">

                                    <span class="info-label">
                                        Username
                                    </span>

                                    <span class="info-value">
                """);

        html.append(
                escapeHtml(
                        user.getUsername()
                )
        );

        html.append("""
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
                """);

        html.append(
                escapeHtml(
                        user.getRole()
                )
        );

        html.append("""
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
                """);

        // ============================================================
        // SEND RESPONSE
        // ============================================================

        response.getWriter()
                .write(
                        html.toString()
                );
    }

    // ============================================================
    // GET USER INITIAL
    // ============================================================

    private String getInitial(
            String username) {

        if (username == null ||
                username.trim().isEmpty()) {

            return "U";
        }

        return escapeHtml(
                username
                        .trim()
                        .substring(0, 1)
                        .toUpperCase()
        );
    }

    // ============================================================
    // HTML ESCAPE
    // ============================================================

    private String escapeHtml(
            String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}