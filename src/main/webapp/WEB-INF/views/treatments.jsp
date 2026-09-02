<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.sunrise.dentalclinic.entity.User" %>
<%@ page import="com.sunrise.dentalclinic.entity.Treatment" %>

<%@ page import="java.util.List" %>
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

    List<Treatment> treatments =
            (List<Treatment>)
                    request.getAttribute("treatments");

    String error =
            (String) request.getAttribute("error");


    if (treatments == null) {

        treatments =
                new ArrayList<>();
    }


    // =========================================================
    // URL
    // =========================================================

    String contextPath =
            request.getContextPath();

    String dashboardUrl =
            contextPath + "/dashboard";

    String treatmentsUrl =
            contextPath + "/treatments";
%>


<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width,
                   initial-scale=1.0">

    <title>
        Treatments - Sunrise Dental Clinic
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
           MAIN
        ===================================================== */

        .main {

            margin-left: 250px;

            min-height: 100vh;

            padding: 28px 32px;
        }


        /* =====================================================
           TOPBAR
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


        .page-badge svg {

            width: 15px;

            height: 15px;

            stroke: currentColor;

            fill: none;

            stroke-width: 1.8;

            stroke-linecap: round;

            stroke-linejoin: round;
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


        .form-input {

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


        .form-input:focus {

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


        /* =====================================================
           ADD BUTTON
        ===================================================== */

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
           TREATMENT LIST
        ===================================================== */

        .treatment-list {

            display: flex;

            flex-direction: column;

            gap: 10px;
        }


        .treatment-row {

            display: grid;

            grid-template-columns:
                    75px
                    minmax(0, 1fr)
                    130px;

            align-items: center;

            gap: 15px;

            padding:
                    14px 15px;

            border:
                    1px solid #e6ecef;

            border-radius: 9px;

            transition:
                    border-color 0.2s ease,
                    box-shadow 0.2s ease;
        }


        .treatment-row:hover {

            border-color: #cbdfe1;

            box-shadow:
                    0 3px 10px
                    rgba(
                            20,
                            70,
                            80,
                            0.05
                    );
        }


        .treatment-id {

            display: inline-flex;

            align-items: center;

            justify-content: center;

            width: fit-content;

            background: #e5f6f5;

            color: #137d80;

            padding:
                    6px 9px;

            border-radius: 6px;

            font-size: 11px;

            font-weight: 700;
        }


        .treatment-name {

            font-size: 14px;

            font-weight: 600;

            color: #304754;

            word-break: break-word;
        }


        .treatment-cost {

            text-align: right;

            font-size: 14px;

            font-weight: 700;

            color: #183b4d;

            white-space: nowrap;
        }


        .list-header {

            display: grid;

            grid-template-columns:
                    75px
                    minmax(0, 1fr)
                    130px;

            gap: 15px;

            padding:
                    0 15px 10px;

            border-bottom:
                    1px solid #edf1f3;

            margin-bottom: 10px;
        }


        .list-header span {

            font-size: 10px;

            font-weight: 700;

            color: #84929a;

            text-transform: uppercase;

            letter-spacing: 0.5px;
        }


        .list-header span:last-child {

            text-align: right;
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

        @media (max-width: 950px) {

            .main {

                margin-left: 250px;

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

            .treatment-row {

                grid-template-columns:
                        65px
                        minmax(0, 1fr);

                gap: 10px;
            }

            .treatment-cost {

                grid-column: 2;

                text-align: left;
            }

            .list-header {

                display: none;
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
     MAIN
========================================================= -->

<main class="main">


    <!-- TOPBAR -->

    <div class="topbar">


        <div>

            <h1 class="page-title">
                Treatment Management
            </h1>

            <p class="page-subtitle">
                Manage clinic treatments and pricing
            </p>

        </div>


        <div class="page-badge">

            <svg viewBox="0 0 24 24">

                <rect x="5"
                      y="5"
                      width="14"
                      height="14"
                      rx="2"></rect>

                <path d="M12 8v8"></path>

                <path d="M8 12h8"></path>

            </svg>

            Treatment Management

        </div>


    </div>



    <!-- BACK -->

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



    <!-- CONTENT -->

    <div class="content-grid">


        <!-- =================================================
             ADD TREATMENT
        ================================================== -->

        <section class="card">


            <div class="card-header">

                <div class="card-title">
                    Add New Treatment
                </div>

                <div class="card-description">
                    Add a treatment and its standard cost.
                </div>

            </div>


            <div class="card-body">


                <form method="post"
                      action="<%= treatmentsUrl %>">


                    <!-- Treatment Name -->

                    <div class="form-group">

                        <label class="form-label">
                            Treatment Name
                        </label>

                        <input
                                type="text"
                                name="treatmentName"
                                class="form-input"
                                placeholder="e.g. Dental Cleaning"
                                required
                        >

                    </div>


                    <!-- Treatment Cost -->

                    <div class="form-group">

                        <label class="form-label">
                            Treatment Cost
                        </label>

                        <input
                                type="number"
                                name="treatmentCost"
                                class="form-input"
                                placeholder="Enter treatment cost"
                                min="0"
                                step="0.01"
                                required
                        >

                    </div>


                    <!-- Submit -->

                    <button
                            type="submit"
                            class="submit-btn">

                        Add Treatment

                    </button>


                </form>


            </div>


        </section>



        <!-- =================================================
             TREATMENT LIST
        ================================================== -->

        <section class="card">


            <div class="card-header">

                <div class="card-title">
                    Available Treatments
                </div>

                <div class="card-description">

                    <%= treatments.size() %>
                    treatment(s) registered

                </div>

            </div>


            <div class="card-body">


                <% if (treatments.isEmpty()) { %>


                    <!-- EMPTY STATE -->

                    <div class="empty-state">


                        <div class="empty-icon">

                            <svg viewBox="0 0 24 24">

                                <rect x="5"
                                      y="5"
                                      width="14"
                                      height="14"
                                      rx="2"></rect>

                                <path d="M12 8v8"></path>

                                <path d="M8 12h8"></path>

                            </svg>

                        </div>


                        <div class="empty-title">

                            No Treatments Found

                        </div>


                        <div class="empty-text">

                            Add a treatment using the form.

                        </div>


                    </div>


                <% } else { %>


                    <!-- TABLE HEADER -->

                    <div class="list-header">

                        <span>
                            ID
                        </span>

                        <span>
                            Treatment
                        </span>

                        <span>
                            Cost
                        </span>

                    </div>


                    <!-- LIST -->

                    <div class="treatment-list">


                        <% for (
                                Treatment treatment
                                : treatments
                        ) { %>


                            <div class="treatment-row">


                                <!-- ID -->

                                <div>

                                    <span class="treatment-id">

                                        #<%= treatment.getId() %>

                                    </span>

                                </div>


                                <!-- NAME -->

                                <div class="treatment-name">

                                    <%= treatment.getTreatmentName() %>

                                </div>


                                <!-- COST -->

                                <div class="treatment-cost">

                                    Rs.
                                    <%= treatment.getTreatmentCost() %>

                                </div>


                            </div>


                        <% } %>


                    </div>


                <% } %>


            </div>


        </section>


    </div>



    <!-- FOOTER -->

    <div class="page-footer">

        © 2026 Sunrise Dental Clinic Management System

    </div>


</main>


</body>

</html>