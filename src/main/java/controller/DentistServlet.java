package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.DentistAvailability;
import com.sunrise.dentalclinic.service.DentistService;
import com.sunrise.dentalclinic.service.DentistAvailabilityService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/dentists")
public class DentistServlet extends HttpServlet {

    private final DentistService dentistService =
            new DentistService();

    private final DentistAvailabilityService availabilityService =
            new DentistAvailabilityService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        showDentists(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");

        try {

            if ("addDentist".equals(action)) {

                addDentist(request, response);

            } else if ("addAvailability".equals(action)) {

                addAvailability(request, response);

            } else {

                response.sendRedirect(
                        request.getContextPath() + "/dentists"
                );
            }

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showDentists(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
        }
    }

    /*
     * ============================
     * ADD DENTIST
     * ============================
     */
    private void addDentist(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String dentistName =
                request.getParameter("dentistName");

        String specialization =
                request.getParameter("specialization");

        String contactNumber =
                request.getParameter("contactNumber");

        if (dentistName == null ||
                dentistName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Dentist name is required."
            );
        }

        if (specialization == null ||
                specialization.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Specialization is required."
            );
        }

        if (contactNumber == null ||
                contactNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Contact number is required."
            );
        }

        Dentist dentist =
                new Dentist(
                        dentistName.trim(),
                        specialization.trim(),
                        contactNumber.trim()
                );

        dentistService.addDentist(dentist);

        response.sendRedirect(
                request.getContextPath() + "/dentists"
        );
    }

    /*
     * ============================
     * ADD AVAILABILITY
     * ============================
     */
    private void addAvailability(
            HttpServletRequest request,
            HttpServletResponse response)
            throws SQLException, IOException {

        String dentistIdText =
                request.getParameter("dentistId");

        if (dentistIdText == null ||
                dentistIdText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Dentist ID is required."
            );
        }

        Long dentistId;

        try {

            dentistId =
                    Long.parseLong(
                            dentistIdText.trim()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Invalid dentist ID: " + dentistIdText
            );
        }

        String dayOfWeek =
                request.getParameter("dayOfWeek");

        if (dayOfWeek == null ||
                dayOfWeek.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select a day."
            );
        }

        String startTimeText =
                request.getParameter("startTime");

        String endTimeText =
                request.getParameter("endTime");

        if (startTimeText == null ||
                startTimeText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Start time is required."
            );
        }

        if (endTimeText == null ||
                endTimeText.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "End time is required."
            );
        }

        LocalTime startTime;
        LocalTime endTime;

        try {

            startTime =
                    LocalTime.parse(
                            startTimeText.trim()
                    );

            endTime =
                    LocalTime.parse(
                            endTimeText.trim()
                    );

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Please enter valid start and end times."
            );
        }

        if (!endTime.isAfter(startTime)) {

            throw new IllegalArgumentException(
                    "End time must be later than start time."
            );
        }

        DentistAvailability availability =
                new DentistAvailability(
                        dentistId,
                        dayOfWeek.trim(),
                        null,
                        startTime,
                        endTime
                );

        availabilityService.addAvailability(
                availability
        );

        response.sendRedirect(
                request.getContextPath() + "/dentists"
        );
    }

    /*
     * ============================
     * LOGIN CHECK
     * ============================
     */
    private boolean isLoggedIn(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        Object userObject =
                session.getAttribute("loggedInUser");

        return userObject != null;
    }

    /*
     * ============================
     * SHOW DENTISTS
     * ============================
     */
    private void showDentists(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<Dentist> dentists =
                    dentistService.getAllDentists();

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            String error =
                    (String) request.getAttribute("error");

            String contextPath =
                    request.getContextPath();

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

            String usersUrl =
                    contextPath + "/manage-users";

            String logoutUrl =
                    contextPath + "/logout";

            StringBuilder html =
                    new StringBuilder();

            /*
             * ============================
             * HTML START
             * ============================
             */

            html.append("""
                    <!DOCTYPE html>
                    <html lang="en">

                    <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width,
                          initial-scale=1.0">

                    <title>
                        Dentists | Sunrise Dental Clinic
                    </title>

                    <style>

                    * {
                        box-sizing: border-box;
                        margin: 0;
                        padding: 0;
                    }

                    body {
                        font-family:
                            "Segoe UI",
                            Arial,
                            sans-serif;

                        background: #f5f8fb;

                        color: #1f2937;

                        min-height: 100vh;
                    }

                    /* =========================
                       LAYOUT
                       ========================= */

                    .app-layout {
                        display: flex;
                        min-height: 100vh;
                    }

                    /* =========================
                       SIDEBAR
                       ========================= */

                    .sidebar {
                        width: 250px;
                        min-width: 250px;

                        background:
                            linear-gradient(
                                180deg,
                                #0b344b 0%,
                                #0f5368 100%
                            );

                        color: white;

                        min-height: 100vh;

                        position: fixed;

                        left: 0;
                        top: 0;
                        bottom: 0;

                        display: flex;
                        flex-direction: column;

                        box-shadow:
                            4px 0 18px
                            rgba(
                                15,
                                61,
                                86,
                                0.14
                            );

                        z-index: 1000;
                    }

                    /* =========================
                       SIDEBAR BRAND
                       ========================= */

                    .sidebar-brand {
                        padding:
                            24px 20px;

                        border-bottom:
                            1px solid
                            rgba(
                                255,
                                255,
                                255,
                                0.10
                            );
                    }

                    .brand {
                        display: flex;

                        align-items: center;

                        gap: 12px;
                    }

                    .brand-logo {
                        width: 44px;
                        height: 44px;

                        border-radius: 11px;

                        background:
                            rgba(
                                255,
                                255,
                                255,
                                0.13
                            );

                        border:
                            1px solid
                            rgba(
                                255,
                                255,
                                255,
                                0.18
                            );

                        display: flex;

                        align-items: center;

                        justify-content: center;

                        font-size: 15px;

                        font-weight: 800;

                        flex-shrink: 0;
                    }

                    .brand-text h1 {
                        font-size: 15px;

                        font-weight: 700;

                        line-height: 1.3;
                    }

                    .brand-text p {
                        color: #b9dce1;

                        font-size: 10px;

                        margin-top: 3px;
                    }

                    /* =========================
                       NAVIGATION
                       ========================= */

                    .sidebar-nav {
                        padding:
                            20px 12px;

                        flex: 1;

                        overflow-y: auto;
                    }

                    .nav-title {
                        color:
                            rgba(
                                255,
                                255,
                                255,
                                0.45
                            );

                        font-size: 10px;

                        text-transform:
                            uppercase;

                        letter-spacing:
                            1px;

                        font-weight: 800;

                        padding:
                            0 11px;

                        margin-bottom: 9px;
                    }

                    .nav-item {
                        display: flex;

                        align-items: center;

                        gap: 12px;

                        width: 100%;

                        padding:
                            12px 12px;

                        margin-bottom: 4px;

                        border-radius: 8px;

                        text-decoration: none;

                        color:
                            rgba(
                                255,
                                255,
                                255,
                                0.76
                            );

                        font-size: 13px;

                        font-weight: 600;

                        transition:
                            all 0.2s ease;
                    }

                    .nav-item:hover {
                        background:
                            rgba(
                                255,
                                255,
                                255,
                                0.09
                            );

                        color: white;

                        transform:
                            translateX(2px);
                    }

                    .nav-item.active {
                        background:
                            rgba(
                                255,
                                255,
                                255,
                                0.15
                            );

                        color: white;

                        box-shadow:
                            inset 3px 0 0
                            #45c7c9;
                    }

                    .nav-icon {
                        width: 25px;

                        height: 25px;

                        border-radius: 6px;

                        display: flex;

                        align-items: center;

                        justify-content: center;

                        background:
                            rgba(
                                255,
                                255,
                                255,
                                0.08
                            );

                        font-size: 12px;

                        flex-shrink: 0;
                    }

                    .nav-item.active
                    .nav-icon {
                        background:
                            rgba(
                                69,
                                199,
                                201,
                                0.20
                            );
                    }

                    /* =========================
                       SIDEBAR FOOTER
                       ========================= */

                    .sidebar-footer {
                        padding:
                            15px 12px;

                        border-top:
                            1px solid
                            rgba(
                                255,
                                255,
                                255,
                                0.10
                            );
                    }

                    .logout {
                        display: flex;

                        align-items: center;

                        gap: 12px;

                        width: 100%;

                        padding:
                            11px 12px;

                        border-radius: 8px;

                        color: #ffd9dd;

                        background:
                            rgba(
                                220,
                                53,
                                69,
                                0.12
                            );

                        text-decoration: none;

                        font-size: 13px;

                        font-weight: 700;

                        transition:
                            all 0.2s ease;
                    }

                    .logout:hover {
                        background:
                            rgba(
                                220,
                                53,
                                69,
                                0.24
                            );

                        color: white;
                    }

                    /* =========================
                       MAIN AREA
                       ========================= */

                    .main-area {
                        width: calc(100% - 250px);

                        margin-left: 250px;

                        min-height: 100vh;
                    }

                    /* =========================
                       TOP HEADER
                       ========================= */

                    .top-header {
                        height: 78px;

                        background: white;

                        border-bottom:
                            1px solid #e5ebef;

                        display: flex;

                        align-items: center;

                        justify-content:
                            space-between;

                        padding:
                            0 35px;

                        box-shadow:
                            0 2px 10px
                            rgba(
                                15,
                                61,
                                86,
                                0.04
                            );
                    }

                    .top-header-title h2 {
                        color: #0f3d56;

                        font-size: 20px;

                        margin-bottom: 3px;
                    }

                    .top-header-title p {
                        color: #87929d;

                        font-size: 11px;
                    }

                    .page-badge {
                        padding:
                            8px 13px;

                        border-radius: 20px;

                        background: #edf8f8;

                        color: #117779;

                        border:
                            1px solid #d8eeee;

                        font-size: 11px;

                        font-weight: 700;
                    }

                    /* =========================
                       CONTENT
                       ========================= */

                    .container {
                        width: 100%;

                        max-width: 1400px;

                        margin: 0 auto;

                        padding:
                            30px 35px 45px;
                    }

                    .page-top {
                        display: flex;

                        align-items: center;

                        justify-content:
                            space-between;

                        margin-bottom: 25px;

                        gap: 20px;
                    }

                    .page-heading h2 {
                        color: #0f3d56;

                        font-size: 27px;

                        margin-bottom: 6px;
                    }

                    .page-heading p {
                        color: #6b7280;

                        font-size: 14px;
                    }

                    .back {
                        display: inline-flex;

                        align-items: center;

                        gap: 8px;

                        text-decoration: none;

                        color: #0f5368;

                        background: white;

                        border:
                            1px solid #dbe5eb;

                        padding:
                            10px 16px;

                        border-radius: 8px;

                        font-size: 13px;

                        font-weight: 700;

                        transition:
                            all 0.2s ease;
                    }

                    .back:hover {
                        border-color: #159a9c;

                        color: #159a9c;

                        transform:
                            translateY(-1px);
                    }

                    /* =========================
                       ERROR
                       ========================= */

                    .error {
                        background: #fff1f2;

                        color: #b42333;

                        border:
                            1px solid #fecdd3;

                        border-left:
                            4px solid #dc3545;

                        padding:
                            14px 16px;

                        border-radius: 8px;

                        margin-bottom: 22px;

                        font-size: 14px;

                        font-weight: 600;
                    }

                    /* =========================
                       MANAGEMENT GRID
                       ========================= */

                    .management-grid {
                        display: grid;

                        grid-template-columns:
                            minmax(300px, 360px)
                            1fr;

                        gap: 24px;

                        align-items: start;
                    }

                    /* =========================
                       CARD
                       ========================= */

                    .card {
                        background: white;

                        border:
                            1px solid #e7edf2;

                        border-radius: 14px;

                        box-shadow:
                            0 5px 20px
                            rgba(
                                15,
                                61,
                                86,
                                0.06
                            );

                        overflow: hidden;
                    }

                    .card-header {
                        padding:
                            20px 22px;

                        border-bottom:
                            1px solid #edf1f4;
                    }

                    .card-header h3 {
                        color: #0f3d56;

                        font-size: 17px;

                        margin-bottom: 5px;
                    }

                    .card-header p {
                        color: #7b8794;

                        font-size: 12px;

                        line-height: 1.5;
                    }

                    .card-body {
                        padding: 22px;
                    }

                    /* =========================
                       FORM
                       ========================= */

                    .form-group {
                        margin-bottom: 17px;
                    }

                    .form-group:last-child {
                        margin-bottom: 0;
                    }

                    label {
                        display: block;

                        margin-bottom: 7px;

                        color: #374151;

                        font-size: 13px;

                        font-weight: 700;
                    }

                    .required {
                        color: #dc3545;
                    }

                    input,
                    select {
                        width: 100%;

                        height: 44px;

                        padding:
                            0 13px;

                        border:
                            1px solid #d8e0e6;

                        border-radius: 8px;

                        background: #fbfcfd;

                        color: #1f2937;

                        font-size: 14px;

                        transition:
                            all 0.2s ease;
                    }

                    input:focus,
                    select:focus {
                        outline: none;

                        background: white;

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

                    input::placeholder {
                        color: #a1aab4;
                    }

                    .submit-button {
                        width: 100%;

                        height: 45px;

                        border: none;

                        border-radius: 8px;

                        background:
                            linear-gradient(
                                135deg,
                                #159a9c,
                                #117779
                            );

                        color: white;

                        font-size: 14px;

                        font-weight: 700;

                        cursor: pointer;

                        margin-top: 5px;

                        transition:
                            all 0.2s ease;

                        box-shadow:
                            0 4px 10px
                            rgba(
                                21,
                                154,
                                156,
                                0.18
                            );
                    }

                    .submit-button:hover {
                        transform:
                            translateY(-1px);

                        box-shadow:
                            0 6px 14px
                            rgba(
                                21,
                                154,
                                156,
                                0.24
                            );
                    }

                    /* =========================
                       TABLE
                       ========================= */

                    .table-wrapper {
                        overflow-x: auto;
                    }

                    table {
                        width: 100%;

                        border-collapse:
                            collapse;

                        min-width: 850px;
                    }

                    thead th {
                        background: #f6f9fb;

                        color: #52616d;

                        padding:
                            13px 15px;

                        text-align: left;

                        font-size: 11px;

                        text-transform:
                            uppercase;

                        letter-spacing:
                            0.5px;

                        border-bottom:
                            1px solid #e5ebef;
                    }

                    tbody td {
                        padding:
                            16px 15px;

                        border-bottom:
                            1px solid #edf1f4;

                        vertical-align: top;

                        font-size: 13px;

                        color: #374151;
                    }

                    tbody tr:last-child td {
                        border-bottom: none;
                    }

                    tbody tr:hover {
                        background: #fbfdfe;
                    }

                    .id-badge {
                        display: inline-flex;

                        align-items: center;

                        justify-content: center;

                        min-width: 32px;

                        height: 28px;

                        padding:
                            0 8px;

                        border-radius: 7px;

                        background: #eef6f7;

                        color: #0f5368;

                        font-weight: 700;

                        font-size: 12px;
                    }

                    .dentist-name {
                        font-weight: 700;

                        color: #0f3d56;

                        margin-bottom: 3px;
                    }

                    .specialization {
                        display: inline-block;

                        padding:
                            6px 9px;

                        border-radius: 6px;

                        background: #edf8f8;

                        color: #117779;

                        font-size: 11px;

                        font-weight: 700;
                    }

                    .contact {
                        color: #52616d;

                        font-weight: 600;
                    }

                    /* =========================
                       AVAILABILITY
                       ========================= */

                    .availability-form {
                        background: #f8fafb;

                        border:
                            1px solid #e8eef1;

                        border-radius: 10px;

                        padding: 13px;

                        min-width: 240px;
                    }

                    .availability-title {
                        color: #0f3d56;

                        font-size: 12px;

                        font-weight: 700;

                        margin-bottom: 11px;
                    }

                    .availability-grid {
                        display: grid;

                        grid-template-columns:
                            1fr 1fr;

                        gap: 8px;
                    }

                    .availability-grid
                    .full {
                        grid-column:
                            1 / -1;
                    }

                    .availability-form
                    select,
                    .availability-form
                    input {
                        height: 36px;

                        font-size: 12px;

                        padding: 0 9px;

                        background: white;
                    }

                    .availability-form label {
                        font-size: 10px;

                        margin-bottom: 4px;

                        color: #6b7280;
                    }

                    .schedule-button {
                        width: 100%;

                        height: 36px;

                        margin-top: 9px;

                        border: none;

                        border-radius: 7px;

                        background: #0f3d56;

                        color: white;

                        font-size: 11px;

                        font-weight: 700;

                        cursor: pointer;

                        transition:
                            background 0.2s;
                    }

                    .schedule-button:hover {
                        background: #092c40;
                    }

                    .current-schedule {
                        margin-top: 12px;

                        padding-top: 11px;

                        border-top:
                            1px solid #e2e8ec;
                    }

                    .current-schedule-title {
                        font-size: 10px;

                        text-transform:
                            uppercase;

                        letter-spacing:
                            0.4px;

                        color: #7b8794;

                        font-weight: 800;

                        margin-bottom: 7px;
                    }

                    .schedule-item {
                        display: flex;

                        align-items: center;

                        justify-content:
                            space-between;

                        gap: 8px;

                        padding:
                            7px 9px;

                        background: white;

                        border:
                            1px solid #e5ebef;

                        border-radius: 6px;

                        margin-bottom: 5px;

                        font-size: 10px;

                        color: #52616d;
                    }

                    .schedule-day {
                        font-weight: 700;

                        color: #0f5368;
                    }

                    .schedule-time {
                        color: #52616d;

                        font-weight: 600;
                    }

                    /* =========================
                       EMPTY
                       ========================= */

                    .empty {
                        text-align: center;

                        padding:
                            55px 20px;

                        color: #8b97a3;
                    }

                    .empty-title {
                        color: #52616d;

                        font-size: 15px;

                        font-weight: 700;

                        margin-bottom: 5px;
                    }

                    .empty-text {
                        font-size: 12px;
                    }

                    /* =========================
                       FOOTER
                       ========================= */

                    footer {
                        text-align: center;

                        padding:
                            22px;

                        color: #8b97a3;

                        font-size: 11px;
                    }

                    /* =========================
                       MOBILE
                       ========================= */

                    @media (max-width: 950px) {

                        .sidebar {
                            width: 220px;
                            min-width: 220px;
                        }

                        .main-area {
                            width:
                                calc(100% - 220px);

                            margin-left: 220px;
                        }

                        .management-grid {
                            grid-template-columns: 1fr;
                        }
                    }

                    @media (max-width: 700px) {

                        .sidebar {
                            width: 68px;
                            min-width: 68px;
                        }

                        .sidebar-brand {
                            padding:
                                18px 11px;
                        }

                        .brand {
                            justify-content:
                                center;
                        }

                        .brand-text,
                        .nav-title {
                            display: none;
                        }

                        .sidebar-nav {
                            padding:
                                15px 8px;
                        }

                        .nav-item {
                            justify-content:
                                center;

                            padding:
                                11px 8px;
                        }

                        .nav-item span:not(.nav-icon) {
                            display: none;
                        }

                        .nav-icon {
                            width: 32px;
                            height: 32px;
                        }

                        .sidebar-footer {
                            padding:
                                12px 8px;
                        }

                        .logout {
                            justify-content:
                                center;

                            padding:
                                10px;
                        }

                        .logout span {
                            display: none;
                        }

                        .main-area {
                            width:
                                calc(100% - 68px);

                            margin-left: 68px;
                        }

                        .top-header {
                            padding:
                                0 18px;

                            height: 68px;
                        }

                        .top-header-title h2 {
                            font-size: 17px;
                        }

                        .page-badge {
                            display: none;
                        }

                        .container {
                            padding:
                                24px 15px 40px;
                        }

                        .page-top {
                            align-items:
                                flex-start;

                            flex-direction:
                                column;
                        }

                        .page-heading h2 {
                            font-size: 23px;
                        }

                        .card-body {
                            padding: 17px;
                        }
                    }

                    </style>

                    </head>

                    <body>

                    <div class="app-layout">

                    <!-- =========================
                         SIDEBAR
                         ========================= -->

                    <aside class="sidebar">

                        <div class="sidebar-brand">

                            <div class="brand">

                                <div class="brand-logo">
                                    SD
                                </div>

                                <div class="brand-text">

                                    <h1>
                                        Sunrise Dental Clinic
                                    </h1>

                                    <p>
                                        Management System
                                    </p>

                                </div>

                            </div>

                        </div>

                        <nav class="sidebar-nav">

                            <div class="nav-title">
                                Main Menu
                            </div>
                    """);

            /*
             * ============================
             * SIDEBAR LINKS
             * ============================
             */

            html.append(
                    navItem(
                            dashboardUrl,
                            "▦",
                            "Dashboard",
                            false
                    )
            );

            html.append(
                    navItem(
                            patientsUrl,
                            "♙",
                            "Patients",
                            false
                    )
            );

            html.append(
                    navItem(
                            appointmentsUrl,
                            "□",
                            "Appointments",
                            false
                    )
            );

            html.append(
                    navItem(
                            dentistsUrl,
                            "✚",
                            "Dentists",
                            true
                    )
            );

            html.append(
                    navItem(
                            treatmentsUrl,
                            "◇",
                            "Treatments",
                            false
                    )
            );

            html.append(
                    navItem(
                            billsUrl,
                            "▤",
                            "Billing",
                            false
                    )
            );

            html.append(
                    navItem(
                            usersUrl,
                            "♙",
                            "Manage Users",
                            false
                    )
            );

            html.append("""
                        </nav>

                        <div class="sidebar-footer">

                            <a class="logout"
                               href="
                    """);

            html.append(
                    escapeHtml(logoutUrl)
            );

            html.append("""
                            ">

                                <span>
                                    ⇥
                                </span>

                                <span>
                                    Logout
                                </span>

                            </a>

                        </div>

                    </aside>

                    <!-- =========================
                         MAIN AREA
                         ========================= -->

                    <div class="main-area">

                        <header class="top-header">

                            <div class="top-header-title">

                                <h2>
                                    Dentist Management
                                </h2>

                                <p>
                                    Manage dentists and
                                    weekly availability
                                </p>

                            </div>

                            <div class="page-badge">
                                Dentist Management
                            </div>

                        </header>

                        <main class="container">

                            <div class="page-top">

                                <div class="page-heading">

                                    <h2>
                                        Dentist Management
                                    </h2>

                                    <p>
                                        Register dentists and
                                        manage their weekly
                                        availability.
                                    </p>

                                </div>

                                <a class="back"
                                   href="
                    """);

            html.append(
                    escapeHtml(dashboardUrl)
            );

            html.append("""
                                ">
                                    ← Back to Dashboard
                                </a>

                            </div>
                    """);

            /*
             * ============================
             * ERROR
             * ============================
             */

            if (error != null &&
                    !error.isBlank()) {

                html.append("""
                            <div class="error">
                    """);

                html.append(
                        escapeHtml(error)
                );

                html.append("""
                            </div>
                    """);
            }

            /*
             * ============================
             * GRID
             * ============================
             */

            html.append("""
                            <div class="management-grid">

                                <!-- ADD DENTIST -->

                                <div class="card">

                                    <div class="card-header">

                                        <h3>
                                            Add New Dentist
                                        </h3>

                                        <p>
                                            Enter the dentist's
                                            professional details.
                                        </p>

                                    </div>

                                    <div class="card-body">

                                        <form method="post"
                                              action="
                    """);

            html.append(
                    escapeHtml(dentistsUrl)
            );

            html.append("""
                                              ">

                                            <input
                                                type="hidden"
                                                name="action"
                                                value="addDentist">

                                            <div class="form-group">

                                                <label>
                                                    Dentist Name
                                                    <span class="required">
                                                        *
                                                    </span>
                                                </label>

                                                <input
                                                    type="text"
                                                    name="dentistName"
                                                    required
                                                    maxlength="100"
                                                    placeholder="Enter dentist name">

                                            </div>

                                            <div class="form-group">

                                                <label>
                                                    Specialization
                                                    <span class="required">
                                                        *
                                                    </span>
                                                </label>

                                                <select
                                                    name="specialization"
                                                    required>

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

                                            <div class="form-group">

                                                <label>
                                                    Contact Number
                                                    <span class="required">
                                                        *
                                                    </span>
                                                </label>

                                                <input
                                                    type="text"
                                                    name="contactNumber"
                                                    required
                                                    maxlength="20"
                                                    placeholder="Enter contact number">

                                            </div>

                                            <button
                                                type="submit"
                                                class="submit-button">

                                                Add Dentist

                                            </button>

                                        </form>

                                    </div>

                                </div>


                                <!-- REGISTERED DENTISTS -->

                                <div class="card">

                                    <div class="card-header">

                                        <h3>
                                            Registered Dentists
                                        </h3>

                                        <p>
                                            View dentists and manage
                                            their weekly schedules.
                                        </p>

                                    </div>

                                    <div class="table-wrapper">

                                        <table>

                                            <thead>

                                                <tr>

                                                    <th>
                                                        ID
                                                    </th>

                                                    <th>
                                                        Dentist
                                                    </th>

                                                    <th>
                                                        Specialization
                                                    </th>

                                                    <th>
                                                        Contact
                                                    </th>

                                                    <th>
                                                        Availability
                                                    </th>

                                                </tr>

                                            </thead>

                                            <tbody>
                    """);

            /*
             * ============================
             * DENTIST LIST
             * ============================
             */

            if (dentists == null ||
                    dentists.isEmpty()) {

                html.append("""
                                                <tr>

                                                    <td colspan="5">

                                                        <div class="empty">

                                                            <div class="empty-title">
                                                                No dentists registered
                                                            </div>

                                                            <div class="empty-text">
                                                                Add a dentist using
                                                                the form.
                                                            </div>

                                                        </div>

                                                    </td>

                                                </tr>
                        """);

            } else {

                for (Dentist dentist : dentists) {

                    html.append("""
                                                <tr>

                                                    <td>

                                                        <span class="id-badge">
                        """);

                    html.append(
                            dentist.getId()
                    );

                    html.append("""
                                                        </span>

                                                    </td>

                                                    <td>

                                                        <div class="dentist-name">
                        """);

                    html.append(
                            escapeHtml(
                                    dentist.getDentistName()
                            )
                    );

                    html.append("""
                                                        </div>

                                                    </td>

                                                    <td>

                                                        <span
                                                            class="specialization">
                        """);

                    html.append(
                            escapeHtml(
                                    dentist.getSpecialization()
                            )
                    );

                    html.append("""
                                                        </span>

                                                    </td>

                                                    <td>

                                                        <span class="contact">
                        """);

                    html.append(
                            escapeHtml(
                                    dentist.getContactNumber()
                            )
                    );

                    html.append("""
                                                        </span>

                                                    </td>

                                                    <td>

                                                        <div
                                                            class="availability-form">

                                                            <div
                                                                class="availability-title">

                                                                Add Weekly Schedule

                                                            </div>

                                                            <form method="post"
                                                                  action="
                        """);

                    html.append(
                            escapeHtml(dentistsUrl)
                    );

                    html.append("""
                                                                  ">

                                                                <input
                                                                    type="hidden"
                                                                    name="action"
                                                                    value="addAvailability">

                        """);

                    html.append(
                            "<input type=\"hidden\" " +
                                    "name=\"dentistId\" value=\"" +
                                    dentist.getId() +
                                    "\">"
                    );

                    html.append("""
                                                                <div
                                                                    class="availability-grid">

                                                                    <div class="full">

                                                                        <label>
                                                                            Day
                                                                        </label>

                                                                        <select
                                                                            name="dayOfWeek"
                                                                            required>

                                                                            <option value="">
                                                                                Select day
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

                                                                    <div>

                                                                        <label>
                                                                            Start Time
                                                                        </label>

                                                                        <input
                                                                            type="time"
                                                                            name="startTime"
                                                                            required>

                                                                    </div>

                                                                    <div>

                                                                        <label>
                                                                            End Time
                                                                        </label>

                                                                        <input
                                                                            type="time"
                                                                            name="endTime"
                                                                            required>

                                                                    </div>

                                                                </div>

                                                                <button
                                                                    type="submit"
                                                                    class="schedule-button">

                                                                    Add Schedule

                                                                </button>

                                                            </form>
                        """);

                    /*
                     * CURRENT SCHEDULE
                     */

                    List<DentistAvailability> schedules =
                            availabilityService
                                    .getByDentistId(
                                            dentist.getId()
                                    );

                    if (schedules != null &&
                            !schedules.isEmpty()) {

                        html.append("""
                                                            <div
                                                                class="current-schedule">

                                                                <div
                                                                    class="current-schedule-title">

                                                                    Current Schedule

                                                                </div>
                            """);

                        for (
                                DentistAvailability schedule :
                                schedules
                        ) {

                            html.append("""
                                                                <div
                                                                    class="schedule-item">

                                                                    <span
                                                                        class="schedule-day">
                            """);

                            html.append(
                                    escapeHtml(
                                            schedule.getDayOfWeek()
                                    )
                            );

                            html.append("""
                                                                    </span>

                                                                    <span
                                                                        class="schedule-time">
                            """);

                            html.append(
                                    schedule.getStartTime()
                            );

                            html.append(" - ");

                            html.append(
                                    schedule.getEndTime()
                            );

                            html.append("""
                                                                    </span>

                                                                </div>
                            """);
                        }

                        html.append("""
                                                            </div>
                        """);
                    }

                    html.append("""
                                                        </div>

                                                    </td>

                                                </tr>
                        """);
                }
            }

            /*
             * ============================
             * HTML END
             * ============================
             */

            html.append("""
                                            </tbody>

                                        </table>

                                    </div>

                                </div>

                            </div>

                        </main>

                        <footer>

                            © 2026 Sunrise Dental Clinic
                            Management System

                        </footer>

                    </div>

                    </div>

                    </body>

                    </html>
                    """);

            response.getWriter()
                    .write(
                            html.toString()
                    );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load dentists.",
                    e
            );
        }
    }

    /*
     * ============================
     * SIDEBAR NAV ITEM
     * ============================
     */
    private String navItem(
            String url,
            String icon,
            String text,
            boolean active) {

        String activeClass =
                active ? " active" : "";

        return
                "<a class=\"nav-item" +
                        activeClass +
                        "\" href=\"" +
                        escapeHtml(url) +
                        "\">" +

                        "<span class=\"nav-icon\">" +
                        escapeHtml(icon) +
                        "</span>" +

                        "<span>" +
                        escapeHtml(text) +
                        "</span>" +

                        "</a>";
    }

    /*
     * ============================
     * HTML ESCAPE
     * ============================
     */
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