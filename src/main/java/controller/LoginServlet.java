package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {


    private final UserService userService =
            new UserService();

    /*
     * ============================================================
     * SHOW LOGIN PAGE
     * ============================================================
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String type =
                request.getParameter("type");

        /*
         * Admin Login
         */
        if ("admin".equalsIgnoreCase(type)) {

            showLoginPage(
                    request,
                    response,
                    "ADMIN"
            );

            return;
        }

        /*
         * Dentist Login
         */
        if ("dentist".equalsIgnoreCase(type)) {

            showLoginPage(
                    request,
                    response,
                    "DENTIST"
            );

            return;
        }

        /*
         * User / Staff Login
         */
        if ("user".equalsIgnoreCase(type)) {

            showLoginPage(
                    request,
                    response,
                    "USER"
            );

            return;
        }

        /*
         * Default = User Login
         */
        showLoginPage(
                request,
                response,
                "USER"
        );
    }


    /*
     * ============================================================
     * PROCESS LOGIN
     * ============================================================
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username =
                request.getParameter("username");

        String password =
                request.getParameter("password");

        String loginType =
                request.getParameter("type");

        /*
         * Default login type.
         */
        if (loginType == null ||
                loginType.isBlank()) {

            loginType = "USER";
        }

        try {

            /*
             * Authenticate user.
             */
            User user =
                    userService.login(
                            username,
                            password
                    );


            /*
             * ====================================================
             * ADMIN LOGIN CHECK
             * ====================================================
             */
            if ("ADMIN".equalsIgnoreCase(
                    loginType)) {

                if (!"ADMIN".equalsIgnoreCase(
                        user.getRole())) {

                    request.setAttribute(
                            "error",
                            "This account does not have administrator access."
                    );

                    showLoginPage(
                            request,
                            response,
                            "ADMIN"
                    );

                    return;
                }
            }


            /*
             * ====================================================
             * DENTIST LOGIN CHECK
             * ====================================================
             */
            if ("DENTIST".equalsIgnoreCase(
                    loginType)) {

                if (!"DENTIST".equalsIgnoreCase(
                        user.getRole())) {

                    request.setAttribute(
                            "error",
                            "This account does not have dentist access."
                    );

                    showLoginPage(
                            request,
                            response,
                            "DENTIST"
                    );

                    return;
                }

                /*
                 * Dentist account must be linked
                 * to a dentist record.
                 */
                if (user.getDentistId() == null ||
                        user.getDentistId() <= 0) {

                    request.setAttribute(
                            "error",
                            "This dentist account is not linked to a dentist profile."
                    );

                    showLoginPage(
                            request,
                            response,
                            "DENTIST"
                    );

                    return;
                }
            }


            /*
             * ====================================================
             * USER / STAFF LOGIN CHECK
             * ====================================================
             */
            if ("USER".equalsIgnoreCase(
                    loginType)) {

                if ("ADMIN".equalsIgnoreCase(
                        user.getRole())) {

                    request.setAttribute(
                            "error",
                            "Please use Admin Login for the administrator account."
                    );

                    showLoginPage(
                            request,
                            response,
                            "USER"
                    );

                    return;
                }

                if ("DENTIST".equalsIgnoreCase(
                        user.getRole())) {

                    request.setAttribute(
                            "error",
                            "Please use Dentist Login for the dentist account."
                    );

                    showLoginPage(
                            request,
                            response,
                            "USER"
                    );

                    return;
                }
            }


            /*
             * ====================================================
             * LOGIN SUCCESS
             * ====================================================
             */

            HttpSession session =
                    request.getSession(true);

            session.setAttribute(
                    "loggedInUser",
                    user
            );

            session.setAttribute(
                    "userId",
                    user.getId()
            );

            session.setAttribute(
                    "username",
                    user.getUsername()
            );

            session.setAttribute(
                    "role",
                    user.getRole()
            );


            /*
             * Save Dentist ID for Dentist accounts.
             */
            if ("DENTIST".equalsIgnoreCase(
                    user.getRole())) {

                session.setAttribute(
                        "dentistId",
                        user.getDentistId()
                );
            }


            /*
             * ====================================================
             * REDIRECT BASED ON ROLE
             * ====================================================
             */

            if ("ADMIN".equalsIgnoreCase(
                    user.getRole())) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/dashboard"
                );

                return;
            }


            if ("DENTIST".equalsIgnoreCase(
                    user.getRole())) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/dentist-dashboard"
                );

                return;
            }


            /*
             * Normal Staff / User
             */
            response.sendRedirect(
                    request.getContextPath()
                            + "/dashboard"
            );


        } catch (IllegalArgumentException e) {

            /*
             * Login failed.
             */
            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            showLoginPage(
                    request,
                    response,
                    loginType
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
        }
    }


    /*
     * ============================================================
     * LOGIN PAGE
     * ============================================================
     */
    private void showLoginPage(
            HttpServletRequest request,
            HttpServletResponse response,
            String loginType)
            throws IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        String error =
                (String) request.getAttribute(
                        "error"
                );

        boolean isAdmin =
                "ADMIN".equalsIgnoreCase(
                        loginType
                );

        boolean isDentist =
                "DENTIST".equalsIgnoreCase(
                        loginType
                );


        /*
         * ============================================================
         * PAGE INFORMATION
         * ============================================================
         */

        String title;

        String subtitle;

        String welcomeTitle;

        String welcomeText;

        String buttonText;

        if (isAdmin) {

            title =
                    "Administrator Login";

            subtitle =
                    "Secure access to the administration panel";

            welcomeTitle =
                    "Manage Your Clinic";

            welcomeText =
                    "Access patients, dentists, appointments, treatments and billing from one secure dashboard.";

            buttonText =
                    "Sign In as Administrator";

        } else if (isDentist) {

            title =
                    "Dentist Login";

            subtitle =
                    "Sign in to manage your appointments and profile";

            welcomeTitle =
                    "Welcome, Doctor";

            welcomeText =
                    "View your appointments, manage your daily schedule and access your dentist profile securely.";

            buttonText =
                    "Sign In as Dentist";

        } else {

            title =
                    "Staff Login";

            subtitle =
                    "Sign in to access the dental clinic system";

            welcomeTitle =
                    "Welcome to Sunrise";

            welcomeText =
                    "A simple and secure way to manage your daily dental clinic operations.";

            buttonText =
                    "Sign In";
        }


        StringBuilder html =
                new StringBuilder();


        /*
         * ============================================================
         * HTML START
         * ============================================================
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
            """);

        html.append(
                escapeHtml(title)
        );

        html.append("""
                - Sunrise Dental Clinic
            </title>

            <style>

            /* ==================================================
               GLOBAL
               ================================================== */

            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }

            body {

                min-height: 100vh;

                font-family:
                    "Segoe UI",
                    Arial,
                    sans-serif;

                background:
                    linear-gradient(
                        135deg,
                        #eef8f8 0%,
                        #f5f8fc 50%,
                        #eaf3f7 100%
                    );

                color: #172b3a;

                display: flex;

                align-items: center;

                justify-content: center;

                padding: 30px;
            }


            /* ==================================================
               MAIN LOGIN WRAPPER
               ================================================== */

            .login-wrapper {

                width: 100%;

                max-width: 1050px;

                min-height: 650px;

                background: #ffffff;

                border-radius: 24px;

                overflow: hidden;

                display: grid;

                grid-template-columns:
                    45% 55%;

                box-shadow:
                    0 25px 70px
                    rgba(15, 61, 86, 0.15);

                border:
                    1px solid
                    rgba(255,255,255,0.8);
            }


            /* ==================================================
               BRANDING PANEL
               ================================================== */

            .branding-panel {

                position: relative;

                background:
                    linear-gradient(
                        145deg,
                        #0b354d,
                        #0f5364 55%,
                        #159a9c
                    );

                color: white;

                padding: 55px 45px;

                display: flex;

                flex-direction: column;

                justify-content: space-between;

                overflow: hidden;
            }


            .branding-panel::before {

                content: "";

                position: absolute;

                width: 300px;

                height: 300px;

                border-radius: 50%;

                background:
                    rgba(255,255,255,0.06);

                top: -120px;

                right: -100px;
            }


            .branding-panel::after {

                content: "";

                position: absolute;

                width: 220px;

                height: 220px;

                border-radius: 50%;

                background:
                    rgba(255,255,255,0.05);

                bottom: -100px;

                left: -80px;
            }


            .brand-content,
            .brand-bottom {

                position: relative;

                z-index: 2;
            }


            /* ==================================================
               LOGO
               ================================================== */

            .brand-logo {

                width: 74px;

                height: 74px;

                border-radius: 20px;

                background:
                    rgba(255,255,255,0.14);

                border:
                    1px solid
                    rgba(255,255,255,0.22);

                display: flex;

                align-items: center;

                justify-content: center;

                font-size: 23px;

                font-weight: 800;

                letter-spacing: 1px;

                margin-bottom: 30px;

                backdrop-filter: blur(10px);
            }


            .brand-name {

                font-size: 29px;

                font-weight: 750;

                letter-spacing: -0.5px;

                margin-bottom: 15px;

                line-height: 1.2;
            }


            .brand-tagline {

                color:
                    rgba(255,255,255,0.78);

                font-size: 15px;

                line-height: 1.7;

                max-width: 360px;
            }


            /* ==================================================
               FEATURES
               ================================================== */

            .feature-list {

                margin-top: 45px;

                display: flex;

                flex-direction: column;

                gap: 17px;
            }


            .feature {

                display: flex;

                align-items: center;

                gap: 13px;

                color:
                    rgba(255,255,255,0.88);

                font-size: 14px;
            }


            .feature-icon {

                width: 34px;

                height: 34px;

                border-radius: 10px;

                background:
                    rgba(255,255,255,0.12);

                display: flex;

                align-items: center;

                justify-content: center;

                font-size: 15px;
            }


            .brand-bottom {

                color:
                    rgba(255,255,255,0.55);

                font-size: 12px;

                line-height: 1.6;
            }


            /* ==================================================
               LOGIN PANEL
               ================================================== */

            .login-panel {

                padding:
                    55px 65px;

                display: flex;

                flex-direction: column;

                justify-content: center;
            }


            .mobile-brand {

                display: none;

                text-align: center;

                margin-bottom: 30px;
            }


            .mobile-logo {

                width: 64px;

                height: 64px;

                margin:
                    0 auto 15px;

                border-radius: 18px;

                background: #0f3d56;

                color: white;

                display: flex;

                align-items: center;

                justify-content: center;

                font-weight: 800;

                font-size: 20px;
            }


            /* ==================================================
               LOGIN HEADER
               ================================================== */

            .login-header {

                margin-bottom: 32px;
            }


            .login-badge {

                display: inline-flex;

                align-items: center;

                gap: 7px;

                padding:
                    7px 11px;

                background: #e8f7f7;

                color: #117779;

                border-radius: 30px;

                font-size: 12px;

                font-weight: 700;

                margin-bottom: 15px;
            }


            .badge-dot {

                width: 7px;

                height: 7px;

                background: #159a9c;

                border-radius: 50%;
            }


            .login-title {

                color: #102f42;

                font-size: 30px;

                font-weight: 750;

                letter-spacing: -0.7px;

                margin-bottom: 9px;
            }


            .login-subtitle {

                color: #718096;

                font-size: 14px;

                line-height: 1.6;
            }


            /* ==================================================
               ERROR
               ================================================== */

            .error {

                display: flex;

                align-items: flex-start;

                gap: 10px;

                background: #fff3f3;

                color: #a12a2a;

                border:
                    1px solid
                    #ffd5d5;

                padding: 13px 14px;

                border-radius: 10px;

                margin-bottom: 22px;

                font-size: 13px;

                line-height: 1.5;
            }


            .error-icon {

                flex-shrink: 0;

                font-weight: bold;

                width: 20px;

                height: 20px;

                border-radius: 50%;

                background: #e05252;

                color: white;

                display: flex;

                align-items: center;

                justify-content: center;

                font-size: 12px;
            }


            /* ==================================================
               FORM
               ================================================== */

            .form-group {

                margin-bottom: 21px;
            }


            label {

                display: block;

                color: #263b4a;

                font-size: 13px;

                font-weight: 700;

                margin-bottom: 8px;
            }


            .input-wrapper {

                position: relative;
            }


            .input-icon {

                position: absolute;

                left: 15px;

                top: 50%;

                transform:
                    translateY(-50%);

                color: #8a9aaa;

                font-size: 15px;

                pointer-events: none;
            }


            input {

                width: 100%;

                height: 52px;

                padding:
                    0 45px;

                border:
                    1px solid
                    #dce3e8;

                border-radius: 11px;

                background: #fbfcfd;

                color: #172b3a;

                font-size: 14px;

                outline: none;

                transition:
                    all 0.2s ease;
            }


            input::placeholder {

                color: #a6b1ba;
            }


            input:hover {

                border-color:
                    #bdcbd3;

                background: #ffffff;
            }


            input:focus {

                border-color:
                    #159a9c;

                background: #ffffff;

                box-shadow:
                    0 0 0 4px
                    rgba(21,154,156,0.10);
            }


            /* ==================================================
               PASSWORD TOGGLE
               ================================================== */

            .password-toggle {

                position: absolute;

                right: 14px;

                top: 50%;

                transform:
                    translateY(-50%);

                border: none;

                background: transparent;

                color: #81909c;

                cursor: pointer;

                padding: 5px;

                font-size: 14px;
            }


            .password-toggle:hover {

                color: #159a9c;
            }


            /* ==================================================
               LOGIN BUTTON
               ================================================== */

            .login-button {

                width: 100%;

                height: 53px;

                border: none;

                border-radius: 11px;

                background:
                    linear-gradient(
                        135deg,
                        #159a9c,
                        #117779
                    );

                color: white;

                font-size: 14px;

                font-weight: 750;

                letter-spacing: 0.1px;

                cursor: pointer;

                margin-top: 5px;

                box-shadow:
                    0 8px 18px
                    rgba(21,154,156,0.20);

                transition:
                    all 0.2s ease;
            }


            .login-button:hover {

                transform:
                    translateY(-1px);

                box-shadow:
                    0 11px 23px
                    rgba(21,154,156,0.28);
            }


            .login-button:active {

                transform:
                    translateY(0);
            }


            /* ==================================================
               SWITCH LOGIN
               ================================================== */

            .switch-login {

                text-align: center;

                margin-top: 24px;

                color: #87939d;

                font-size: 13px;
            }


            .switch-login a {

                display: inline-block;

                margin-top: 7px;

                color: #117779;

                font-weight: 700;

                text-decoration: none;
            }


            .switch-login a:hover {

                text-decoration: underline;
            }


            /* ==================================================
               BACK TO HOME
               ================================================== */

            .back-home {

                text-align: center;

                margin-top: 22px;

                padding-top: 20px;

                border-top:
                    1px solid
                    #edf0f2;
            }


            .back-home a {

                color: #718096;

                text-decoration: none;

                font-size: 12px;

                font-weight: 600;

                transition: color 0.2s;
            }


            .back-home a:hover {

                color: #159a9c;
            }


            /* ==================================================
               SECURITY TEXT
               ================================================== */

            .security {

                display: flex;

                align-items: center;

                justify-content: center;

                gap: 7px;

                margin-top: 18px;

                color: #a0aab2;

                font-size: 11px;
            }


            .security-icon {

                font-size: 12px;
            }


            /* ==================================================
               MOBILE
               ================================================== */

            @media (max-width: 850px) {

                body {
                    padding: 20px;
                }

                .login-wrapper {

                    max-width: 520px;

                    min-height: auto;

                    display: block;

                    border-radius: 20px;
                }

                .branding-panel {

                    display: none;
                }

                .login-panel {

                    padding:
                        40px 35px;
                }

                .mobile-brand {

                    display: block;
                }
            }


            @media (max-width: 480px) {

                body {
                    padding: 12px;
                }

                .login-wrapper {
                    border-radius: 16px;
                }

                .login-panel {
                    padding:
                        32px 22px;
                }

                .login-title {
                    font-size: 26px;
                }

                .login-subtitle {
                    font-size: 13px;
                }

                input {
                    height: 50px;
                }

                .login-button {
                    height: 51px;
                }
            }

            </style>

            </head>


            <body>


            <div class="login-wrapper">


            <!-- =================================================
                 LEFT BRANDING PANEL
                 ================================================= -->

            <div class="branding-panel">

                <div class="brand-content">

                    <div class="brand-logo">
                        SD
                    </div>

                    <div class="brand-name">
                        Sunrise Dental Clinic
                    </div>

                    <div class="brand-tagline">
            """);

        html.append(
                escapeHtml(welcomeText)
        );

        html.append("""
                    </div>


                    <div class="feature-list">

                        <div class="feature">

                            <div class="feature-icon">
                                +
                            </div>

                            <span>
                                Patient Management
                            </span>

                        </div>


                        <div class="feature">

                            <div class="feature-icon">
                                +
                            </div>

                            <span>
                                Appointment Management
                            </span>

                        </div>


                        <div class="feature">

                            <div class="feature-icon">
                                +
                            </div>

                            <span>
                                Treatment & Billing
                            </span>

                        </div>


                        <div class="feature">

                            <div class="feature-icon">
                                +
                            </div>

                            <span>
                                Secure Clinic Records
                            </span>

                        </div>

                    </div>

                </div>


                <div class="brand-bottom">

                    Sunrise Dental Clinic<br>

                    Patient Management System

                </div>

            </div>


            <!-- =================================================
                 LOGIN PANEL
                 ================================================= -->

            <div class="login-panel">


                <!-- Mobile Branding -->

                <div class="mobile-brand">

                    <div class="mobile-logo">
                        SD
                    </div>

                    <strong>
                        Sunrise Dental Clinic
                    </strong>

                </div>


                <!-- Login Header -->

                <div class="login-header">

                    <div class="login-badge">

                        <span class="badge-dot"></span>

            """);


        if (isAdmin) {

            html.append("""
                        Administrator Access
                """);

        } else if (isDentist) {

            html.append("""
                        Dentist Access
                """);

        } else {

            html.append("""
                        Staff Access
                """);
        }


        html.append("""
                    </div>


                    <h1 class="login-title">
            """);

        html.append(
                escapeHtml(welcomeTitle)
        );

        html.append("""
                    </h1>


                    <p class="login-subtitle">
            """);

        html.append(
                escapeHtml(subtitle)
        );

        html.append("""
                    </p>

                </div>
            """);


        /*
         * ============================================================
         * ERROR MESSAGE
         * ============================================================
         */

        if (error != null &&
                !error.isBlank()) {

            html.append("""
                <div class="error">

                    <div class="error-icon">
                        !
                    </div>

                    <div>
                """);

            html.append(
                    escapeHtml(error)
            );

            html.append("""
                    </div>

                </div>
                """);
        }


        /*
         * ============================================================
         * LOGIN FORM
         * ============================================================
         */

        html.append("""
                <form method="post"
                      action="login">

                    <input
                        type="hidden"
                        name="type"
                        value="
            """);

        html.append(
                escapeHtml(loginType)
        );

        html.append("""
                    ">


                    <!-- USERNAME -->

                    <div class="form-group">

                        <label for="username">
                            Username
                        </label>

                        <div class="input-wrapper">

                            <span class="input-icon">
                                @
                            </span>

                            <input
                                type="text"
                                id="username"
                                name="username"
                                placeholder="Enter your username"
                                autocomplete="username"
                                required>

                        </div>

                    </div>


                    <!-- PASSWORD -->

                    <div class="form-group">

                        <label for="password">
                            Password
                        </label>

                        <div class="input-wrapper">

                            <span class="input-icon">
                                *
                            </span>

                            <input
                                type="password"
                                id="password"
                                name="password"
                                placeholder="Enter your password"
                                autocomplete="current-password"
                                required>

                            <button
                                type="button"
                                class="password-toggle"
                                onclick="togglePassword()"
                                id="passwordToggle">

                                Show

                            </button>

                        </div>

                    </div>


                    <!-- LOGIN BUTTON -->

                    <button
                        type="submit"
                        class="login-button">
            """);

        html.append(
                escapeHtml(buttonText)
        );

        html.append("""
                    </button>

                </form>
            """);


        /*
         * ============================================================
         * SWITCH LOGIN
         * ============================================================
         */

        if (isAdmin) {

            html.append("""
                <div class="switch-login">

                    Need staff access?

                    <br>

                    <a href="login?type=user">
                        Sign in as Staff
                    </a>

                    <br>

                    <a href="login?type=dentist">
                        Sign in as Dentist
                    </a>

                </div>
                """);

        } else if (isDentist) {

            html.append("""
                <div class="switch-login">

                    Need staff access?

                    <br>

                    <a href="login?type=user">
                        Sign in as Staff
                    </a>

                    <br>

                    <a href="login?type=admin">
                        Administrator Login
                    </a>

                </div>
                """);

        } else {

            html.append("""
                <div class="switch-login">

                    Are you an administrator?

                    <br>

                    <a href="login?type=admin">
                        Administrator Login
                    </a>

                    <br>

                    <a href="login?type=dentist">
                        Sign in as Dentist
                    </a>

                </div>
                """);
        }


        /*
         * ============================================================
         * BACK TO HOME
         * ============================================================
         */

        html.append("""
                <div class="back-home">

                    <a href="./">
                        ← Back to Home
                    </a>

                </div>


                <div class="security">

                    <span class="security-icon">
                        ●
                    </span>

                    Secure clinic system

                </div>


            </div>

            </div>


            <!-- =================================================
                 PASSWORD SCRIPT
                 ================================================= -->

            <script>

            function togglePassword() {

                const password =
                    document.getElementById("password");

                const button =
                    document.getElementById(
                        "passwordToggle"
                    );

                if (password.type === "password") {

                    password.type = "text";

                    button.innerText = "Hide";

                } else {

                    password.type = "password";

                    button.innerText = "Show";
                }
            }

            </script>


            </body>

            </html>
            """);


        /*
         * ============================================================
         * SEND HTML
         * ============================================================
         */

        response.getWriter()
                .write(
                        html.toString()
                );
    }


    /*
     * ============================================================
     * HTML ESCAPE
     * ============================================================
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
