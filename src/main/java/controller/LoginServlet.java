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
     * ============================
     * SHOW LOGIN PAGE
     * ============================
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
         * User Login
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
         * If /login is opened directly,
         * show normal User Login.
         */
        showLoginPage(
                request,
                response,
                "USER"
        );
    }


    /*
     * ============================
     * PROCESS LOGIN
     * ============================
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
             * ============================
             * CHECK LOGIN TYPE
             * ============================
             *
             * Admin Login button can only
             * be used by ADMIN.
             */
            if ("ADMIN".equalsIgnoreCase(
                    loginType)) {

                if (!"ADMIN".equalsIgnoreCase(
                        user.getRole())) {

                    request.setAttribute(
                            "error",
                            "This is not an admin account."
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
             * User Login button should not
             * be used by ADMIN.
             */
            if ("USER".equalsIgnoreCase(
                    loginType)) {

                if ("ADMIN".equalsIgnoreCase(
                        user.getRole())) {

                    request.setAttribute(
                            "error",
                            "Please use Admin Login for the admin account."
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
             * ============================
             * LOGIN SUCCESS
             * ============================
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
             * Go to Dashboard.
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
     * ============================
     * LOGIN PAGE
     * ============================
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

        String title;

        String subtitle;

        if ("ADMIN".equalsIgnoreCase(
                loginType)) {

            title = "Admin Login";

            subtitle =
                    "Login to the administration panel";

        } else {

            title = "User Login";

            subtitle =
                    "Login to the dental clinic system";
        }

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
                """);

        html.append(
                escapeHtml(title)
        );

        html.append("""
                    - Sunrise Dental Clinic
                </title>

                <style>

                * {
                    box-sizing: border-box;
                    margin: 0;
                    padding: 0;
                    font-family: Arial, sans-serif;
                }

                body {

                    min-height: 100vh;

                    background: #f4f7fb;

                    display: flex;

                    align-items: center;

                    justify-content: center;

                    padding: 20px;
                }

                .container {

                    width: 100%;

                    max-width: 430px;
                }

                .card {

                    background: white;

                    padding: 40px;

                    border-radius: 12px;

                    box-shadow:
                        0 5px 20px
                        rgba(0,0,0,0.10);
                }

                .logo {

                    width: 70px;

                    height: 70px;

                    margin:
                        0 auto 20px;

                    background: #0f3d56;

                    color: white;

                    border-radius: 50%;

                    display: flex;

                    align-items: center;

                    justify-content: center;

                    font-size: 27px;

                    font-weight: bold;
                }

                .clinic-name {

                    text-align: center;

                    color: #0f3d56;

                    font-size: 26px;

                    margin-bottom: 8px;
                }

                .subtitle {

                    text-align: center;

                    color: #6b7280;

                    margin-bottom: 25px;

                    line-height: 1.5;
                }

                .login-title {

                    text-align: center;

                    color: #0f3d56;

                    margin-bottom: 25px;
                }

                label {

                    display: block;

                    margin-top: 15px;

                    margin-bottom: 7px;

                    font-weight: bold;

                    color: #1f2937;
                }

                input {

                    width: 100%;

                    padding: 12px;

                    border:
                        1px solid #d1d5db;

                    border-radius: 6px;

                    font-size: 15px;
                }

                input:focus {

                    outline: none;

                    border-color: #159a9c;

                    box-shadow:
                        0 0 0 2px
                        rgba(21,154,156,0.12);
                }

                button {

                    width: 100%;

                    margin-top: 25px;

                    padding: 13px;

                    border: none;

                    border-radius: 6px;

                    background: #159a9c;

                    color: white;

                    font-size: 16px;

                    font-weight: bold;

                    cursor: pointer;
                }

                button:hover {

                    background: #117779;
                }

                .error {

                    background: #fee2e2;

                    color: #991b1b;

                    padding: 12px;

                    border-radius: 6px;

                    margin-bottom: 20px;

                    text-align: center;

                    line-height: 1.4;
                }

                .switch-login {

                    text-align: center;

                    margin-top: 20px;

                    color: #6b7280;

                    line-height: 1.8;
                }

                .switch-login a {

                    color: #159a9c;

                    font-weight: bold;

                    text-decoration: none;
                }

                .switch-login a:hover {

                    text-decoration: underline;
                }

                .back {

                    text-align: center;

                    margin-top: 15px;
                }

                .back a {

                    color: #0f3d56;

                    text-decoration: none;

                    font-size: 14px;
                }

                .back a:hover {

                    text-decoration: underline;
                }

                </style>

                </head>

                <body>

                <div class="container">

                <div class="card">

                <div class="logo">
                    SD
                </div>

                <h1 class="clinic-name">
                    Sunrise Dental Clinic
                </h1>

                <p class="subtitle">
                """);

        html.append(
                escapeHtml(subtitle)
        );

        html.append("""
                </p>

                <h2 class="login-title">
                """);

        html.append(
                escapeHtml(title)
        );

        html.append("""
                </h2>
                """);


        /*
         * ============================
         * ERROR MESSAGE
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
         * LOGIN FORM
         * ============================
         */

        html.append("""
                <form method="post"
                      action="login">

                    <input type="hidden"
                           name="type"
                           value="
                """);

        html.append(
                escapeHtml(loginType)
        );

        html.append("""
                    ">

                    <label>
                        Username
                    </label>

                    <input
                        type="text"
                        name="username"
                        placeholder="Enter username"
                        autocomplete="username"
                        required>

                    <label>
                        Password
                    </label>

                    <input
                        type="password"
                        name="password"
                        placeholder="Enter password"
                        autocomplete="current-password"
                        required>

                    <button type="submit">
                """);

        html.append(
                escapeHtml(title)
        );

        html.append("""
                    </button>

                </form>
                """);


        /*
         * ============================
         * SWITCH LOGIN
         * ============================
         */

        if ("ADMIN".equalsIgnoreCase(
                loginType)) {

            html.append("""
                    <div class="switch-login">

                        Are you a normal user?

                        <br>

                        <a href="login?type=user">
                            User Login
                        </a>

                    </div>
                    """);

        } else {

            html.append("""
                    <div class="switch-login">

                        Are you an administrator?

                        <br>

                        <a href="login?type=admin">
                            Admin Login
                        </a>

                    </div>
                    """);
        }


        /*
         * ============================
         * BACK TO HOME
         * ============================
         */

        html.append("""
                <div class="back">

                    <a href="./">
                        ← Back to Home
                    </a>

                </div>

                </div>

                </div>

                </body>

                </html>
                """);


        /*
         * Send HTML.
         */
        response.getWriter()
                .write(
                        html.toString()
                );
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