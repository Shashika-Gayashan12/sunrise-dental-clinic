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
import java.util.List;

@WebServlet("/manage-users")
public class UserManagementServlet extends HttpServlet {

    private final UserService userService =
            new UserService();

    /*
     * ============================
     * SHOW MANAGE USERS PAGE
     * ============================
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Only ADMIN can access this page.
         */
        if (!isAdmin(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied. Admin access required."
            );

            return;
        }

        try {

            List<User> users =
                    userService.getAllUsers();

            showUsers(
                    request,
                    response,
                    users
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load users.",
                    e
            );
        }
    }


    /*
     * ============================
     * CREATE / ACTIVATE /
     * DEACTIVATE / DELETE USER
     * ============================
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Only ADMIN can perform
         * user management actions.
         */
        if (!isAdmin(request)) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied. Admin access required."
            );

            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");


        try {

            /*
             * ============================
             * CREATE USER
             * ============================
             */

            if ("create".equalsIgnoreCase(action)) {

                String username =
                        request.getParameter("username");

                String password =
                        request.getParameter("password");

                userService.createUser(
                        username,
                        password
                );

                /*
                 * After creating the user,
                 * return to Manage Users page.
                 */
                response.sendRedirect(
                        request.getContextPath()
                                + "/manage-users"
                );

                return;
            }


            /*
             * ============================
             * OTHER ACTIONS REQUIRE ID
             * ============================
             */

            String idParameter =
                    request.getParameter("id");

            if (idParameter == null ||
                    idParameter.isBlank()) {

                throw new IllegalArgumentException(
                        "User ID is required."
                );
            }

            Long id =
                    Long.parseLong(
                            idParameter.trim()
                    );


            /*
             * ============================
             * ACTIVATE
             * ============================
             */

            if ("activate".equalsIgnoreCase(action)) {

                userService.activateUser(id);
            }


            /*
             * ============================
             * DEACTIVATE
             * ============================
             */

            else if ("deactivate".equalsIgnoreCase(action)) {

                userService.deactivateUser(id);
            }


            /*
             * ============================
             * DELETE
             * ============================
             */

            else if ("delete".equalsIgnoreCase(action)) {

                userService.deleteUser(id);
            }


            /*
             * ============================
             * INVALID ACTION
             * ============================
             */

            else {

                throw new IllegalArgumentException(
                        "Invalid action."
                );
            }


            /*
             * Return to Manage Users page.
             */
            response.sendRedirect(
                    request.getContextPath()
                            + "/manage-users"
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error",
                    "Invalid user ID."
            );

            reloadUsers(
                    request,
                    response
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            reloadUsers(
                    request,
                    response
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
     * CHECK ADMIN
     * ============================
     */
    private boolean isAdmin(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        Object userObject =
                session.getAttribute(
                        "loggedInUser"
                );

        /*
         * Make sure logged-in user
         * is actually a User object.
         */
        if (!(userObject instanceof User)) {
            return false;
        }

        User user =
                (User) userObject;

        /*
         * User must be ACTIVE.
         */
        if (!"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            return false;
        }

        /*
         * User must be ADMIN.
         */
        return "ADMIN".equalsIgnoreCase(
                user.getRole()
        );
    }


    /*
     * ============================
     * RELOAD USERS AFTER ERROR
     * ============================
     */
    private void reloadUsers(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<User> users =
                    userService.getAllUsers();

            showUsers(
                    request,
                    response,
                    users
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load users.",
                    e
            );
        }
    }


    /*
     * ============================
     * SHOW USERS PAGE
     * ============================
     */
    private void showUsers(
            HttpServletRequest request,
            HttpServletResponse response,
            List<User> users)
            throws IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        String error =
                (String) request.getAttribute(
                        "error"
                );

        String contextPath =
                request.getContextPath();

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
                    Manage Users -
                    Sunrise Dental Clinic
                </title>

                <style>

                * {
                    box-sizing: border-box;
                    margin: 0;
                    padding: 0;
                    font-family: Arial, sans-serif;
                }

                body {
                    background: #f4f7fb;
                    color: #1f2937;
                    min-height: 100vh;
                }

                header {
                    background: #0f3d56;
                    color: white;
                    padding: 20px 40px;

                    display: flex;
                    justify-content: space-between;
                    align-items: center;

                    gap: 20px;
                }

                header h1 {
                    margin-bottom: 5px;
                }

                header p {
                    color: #c9e8e5;
                    font-size: 14px;
                }

                .logout {
                    color: white;
                    text-decoration: none;
                    background: #dc2626;
                    padding: 10px 16px;
                    border-radius: 6px;
                    font-weight: bold;
                }

                .logout:hover {
                    background: #b91c1c;
                }

                .container {
                    max-width: 1200px;
                    margin: auto;
                    padding: 40px;
                }

                .back {
                    display: inline-block;
                    margin-bottom: 20px;
                    color: #159a9c;
                    font-weight: bold;
                    text-decoration: none;
                }

                .back:hover {
                    text-decoration: underline;
                }

                .card {
                    background: white;
                    padding: 30px;
                    border-radius: 10px;

                    box-shadow:
                        0 3px 12px
                        rgba(0,0,0,0.08);

                    margin-bottom: 25px;
                }

                h2 {
                    color: #0f3d56;
                    margin-bottom: 20px;
                }

                .create-form {
                    display: grid;

                    grid-template-columns:
                        1fr 1fr auto;

                    gap: 12px;

                    align-items: end;
                }

                .form-group label {
                    display: block;

                    margin-bottom: 7px;

                    font-weight: bold;

                    color: #374151;
                }

                .form-group input {
                    width: 100%;

                    padding: 11px;

                    border:
                        1px solid #d1d5db;

                    border-radius: 6px;

                    font-size: 14px;
                }

                .form-group input:focus {
                    outline: none;

                    border-color: #159a9c;

                    box-shadow:
                        0 0 0 2px
                        rgba(21,154,156,0.12);
                }

                .create-button {
                    border: none;

                    padding: 11px 18px;

                    border-radius: 6px;

                    background: #159a9c;

                    color: white;

                    font-weight: bold;

                    cursor: pointer;

                    font-size: 14px;
                }

                .create-button:hover {
                    background: #117779;
                }

                .error {
                    background: #fee2e2;

                    color: #991b1b;

                    padding: 12px;

                    border-radius: 6px;

                    margin-bottom: 20px;

                    text-align: center;
                }

                table {
                    width: 100%;

                    border-collapse: collapse;
                }

                th,
                td {
                    padding: 13px;

                    border-bottom:
                        1px solid #e5e7eb;

                    text-align: left;
                }

                th {
                    background: #0f3d56;

                    color: white;
                }

                tr:hover {
                    background: #f9fafb;
                }

                .status {
                    font-weight: bold;
                }

                .active {
                    color: #166534;
                }

                .pending {
                    color: #b45309;
                }

                .inactive {
                    color: #991b1b;
                }

                .action-form {
                    display: inline;
                }

                button.action {
                    border: none;

                    border-radius: 5px;

                    padding: 7px 10px;

                    margin-right: 5px;

                    cursor: pointer;

                    font-size: 13px;

                    font-weight: bold;
                }

                .activate {
                    background: #dcfce7;
                    color: #166534;
                }

                .activate:hover {
                    background: #bbf7d0;
                }

                .deactivate {
                    background: #fee2e2;
                    color: #991b1b;
                }

                .deactivate:hover {
                    background: #fecaca;
                }

                .delete {
                    background: #e5e7eb;
                    color: #374151;
                }

                .delete:hover {
                    background: #d1d5db;
                }

                .protected {
                    color: #0f3d56;
                    font-weight: bold;
                }

                .empty {
                    text-align: center;

                    padding: 30px;

                    color: #6b7280;
                }

                .info {
                    color: #6b7280;

                    font-size: 13px;

                    margin-top: 10px;
                }

                footer {
                    text-align: center;

                    color: #6b7280;

                    font-size: 13px;

                    padding: 25px;
                }

                @media (max-width: 800px) {

                    header {
                        padding: 20px;
                        flex-direction: column;
                        align-items: flex-start;
                    }

                    .container {
                        padding: 25px 15px;
                    }

                    .create-form {
                        grid-template-columns: 1fr;
                    }

                    .create-button {
                        width: 100%;
                    }

                    .card {
                        padding: 20px;
                        overflow-x: auto;
                    }

                    table {
                        min-width: 750px;
                    }
                }

                </style>

                </head>

                <body>

                <header>

                    <div>

                        <h1>
                            Sunrise Dental Clinic
                        </h1>

                        <p>
                            User Management
                        </p>

                    </div>

                    <a class="logout"
                       href="
                """);

        html.append(contextPath);

        html.append("""
                            /logout">
                        Logout
                    </a>

                </header>


                <div class="container">

                <a class="back"
                   href="
                """);

        html.append(contextPath);

        html.append("""
                            /dashboard">
                    ← Back to Dashboard
                </a>


                <!-- =========================
                     ERROR
                     ========================= -->

                """);

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
         * CREATE USER CARD
         * ============================
         */

        html.append("""
                <div class="card">

                    <h2>
                        Create New User
                    </h2>

                    <form method="post"
                          action="
                """);

        html.append(contextPath);

        html.append("""
                            /manage-users"
                          class="create-form">

                        <input type="hidden"
                               name="action"
                               value="create">

                        <div class="form-group">

                            <label>
                                Username
                            </label>

                            <input
                                type="text"
                                name="username"
                                placeholder="Enter username"
                                autocomplete="off"
                                required>

                        </div>


                        <div class="form-group">

                            <label>
                                Password
                            </label>

                            <input
                                type="password"
                                name="password"
                                placeholder="Enter password"
                                autocomplete="new-password"
                                required>

                        </div>


                        <button
                            type="submit"
                            class="create-button">

                            Create User

                        </button>

                    </form>

                    <p class="info">
                        New users will be created as
                        <strong>PENDING</strong>.
                        They can log in only after you
                        activate their account.
                    </p>

                </div>


                <!-- =========================
                     USER LIST
                     ========================= -->

                <div class="card">

                    <h2>
                        System Users
                    </h2>

                    <table>

                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                """);


        /*
         * ============================
         * USER LIST
         * ============================
         */

        if (users == null ||
                users.isEmpty()) {

            html.append("""
                        <tr>

                            <td colspan="5"
                                class="empty">

                                No users found.

                            </td>

                        </tr>
                    """);

        } else {

            for (User user : users) {

                String status =
                        user.getStatus();

                String statusClass = "";

                if (status != null) {

                    statusClass =
                            status.toLowerCase();
                }

                html.append("<tr>");


                /*
                 * ID
                 */
                html.append("<td>")
                        .append(user.getId())
                        .append("</td>");


                /*
                 * Username
                 */
                html.append("<td>")
                        .append(
                                escapeHtml(
                                        user.getUsername()
                                )
                        )
                        .append("</td>");


                /*
                 * Role
                 */
                html.append("<td>")
                        .append(
                                escapeHtml(
                                        user.getRole()
                                )
                        )
                        .append("</td>");


                /*
                 * Status
                 */
                html.append(
                        "<td class=\"status "
                                + statusClass
                                + "\">"
                );

                html.append(
                        escapeHtml(status)
                );

                html.append("</td>");


                /*
                 * Actions
                 */
                html.append("<td>");


                /*
                 * ADMIN account is protected.
                 */
                if ("ADMIN".equalsIgnoreCase(
                        user.getRole())) {

                    html.append("""
                            <span class="protected">
                                Protected Admin
                            </span>
                            """);

                } else {


                    /*
                     * ========================
                     * ACTIVATE
                     * ========================
                     */

                    if ("PENDING".equalsIgnoreCase(
                            status)
                            ||
                            "INACTIVE".equalsIgnoreCase(
                                    status)) {

                        html.append("""
                                <form method="post"
                                      action="
                                """);

                        html.append(contextPath);

                        html.append("""
                                            /manage-users"
                                      class="action-form">

                                    <input type="hidden"
                                           name="action"
                                           value="activate">

                                    <input type="hidden"
                                           name="id"
                                           value="
                                """);

                        html.append(user.getId());

                        html.append("""
                                    ">

                                    <button
                                        type="submit"
                                        class="action activate">

                                        Activate

                                    </button>

                                </form>
                                """);
                    }


                    /*
                     * ========================
                     * DEACTIVATE
                     * ========================
                     */

                    if ("ACTIVE".equalsIgnoreCase(
                            status)) {

                        html.append("""
                                <form method="post"
                                      action="
                                """);

                        html.append(contextPath);

                        html.append("""
                                            /manage-users"
                                      class="action-form"
                                      onsubmit="return confirm(
                                      'Deactivate this user?'
                                      );">

                                    <input type="hidden"
                                           name="action"
                                           value="deactivate">

                                    <input type="hidden"
                                           name="id"
                                           value="
                                """);

                        html.append(user.getId());

                        html.append("""
                                    ">

                                    <button
                                        type="submit"
                                        class="action deactivate">

                                        Deactivate

                                    </button>

                                </form>
                                """);
                    }


                    /*
                     * ========================
                     * DELETE
                     * ========================
                     */

                    html.append("""
                            <form method="post"
                                  action="
                        """);

                    html.append(contextPath);

                    html.append("""
                                        /manage-users"
                                  class="action-form"
                                  onsubmit="return confirm(
                                  'Delete this user permanently?'
                                  );">

                                <input type="hidden"
                                       name="action"
                                       value="delete">

                                <input type="hidden"
                                       name="id"
                                       value="
                        """);

                    html.append(user.getId());

                    html.append("""
                                ">

                                <button
                                    type="submit"
                                    class="action delete">

                                    Delete

                                </button>

                            </form>
                            """);
                }


                html.append("</td>");

                html.append("</tr>");
            }
        }


        /*
         * ============================
         * HTML END
         * ============================
         */

        html.append("""
                    </table>

                </div>

                </div>


                <footer>

                    © 2026 Sunrise Dental Clinic
                    Management System

                </footer>


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