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

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Access denied."
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action =
                request.getParameter("action");

        String idParameter =
                request.getParameter("id");

        try {

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

            if ("activate".equalsIgnoreCase(action)) {

                userService.activateUser(id);

            } else if ("deactivate".equalsIgnoreCase(action)) {

                userService.deactivateUser(id);

            } else if ("delete".equalsIgnoreCase(action)) {

                userService.deleteUser(id);

            } else {

                throw new IllegalArgumentException(
                        "Invalid action."
                );
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/manage-users"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            try {

                showUsers(
                        request,
                        response,
                        userService.getAllUsers()
                );

            } catch (SQLException sqlException) {

                throw new ServletException(
                        "Unable to load users.",
                        sqlException
                );
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Database operation failed.",
                    e
            );
        }
    }

    /*
     * Check whether logged-in user is ADMIN.
     */
    private boolean isAdmin(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        Object role =
                session.getAttribute("role");

        return role != null
                && "ADMIN".equalsIgnoreCase(
                role.toString()
        );
    }

    /*
     * Display Manage Users page.
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

        StringBuilder html =
                new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html>

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

                body {
                    margin: 0;
                    background: #f4f7fb;
                    color: #1f2937;
                    font-family: Arial, sans-serif;
                }

                header {
                    background: #0f3d56;
                    color: white;
                    padding: 20px 40px;
                }

                header h1 {
                    margin: 0 0 5px 0;
                }

                header p {
                    margin: 0;
                    color: #c9e8e5;
                }

                .container {
                    max-width: 1200px;
                    margin: auto;
                    padding: 40px;
                }

                .card {
                    background: white;
                    padding: 25px;
                    border-radius: 10px;
                    box-shadow:
                        0 3px 12px
                        rgba(0,0,0,0.08);
                }

                h2 {
                    color: #0f3d56;
                }

                .error {
                    background: #fee2e2;
                    color: #991b1b;
                    padding: 12px;
                    border-radius: 6px;
                    margin-bottom: 20px;
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                }

                th,
                td {
                    padding: 12px;
                    border-bottom:
                        1px solid #ddd;
                    text-align: left;
                }

                th {
                    background: #0f3d56;
                    color: white;
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

                button {
                    border: none;
                    border-radius: 5px;
                    padding: 7px 10px;
                    margin-right: 5px;
                    cursor: pointer;
                    font-size: 13px;
                }

                .activate {
                    background: #dcfce7;
                    color: #166534;
                }

                .deactivate {
                    background: #fee2e2;
                    color: #991b1b;
                }

                .delete {
                    background: #e5e7eb;
                    color: #374151;
                }

                .back {
                    display: inline-block;
                    margin-bottom: 20px;
                    color: #159a9c;
                    font-weight: bold;
                    text-decoration: none;
                }

                .empty {
                    text-align: center;
                    padding: 30px;
                    color: #6b7280;
                }

                </style>

                </head>

                <body>

                <header>

                    <h1>
                        Sunrise Dental Clinic
                    </h1>

                    <p>
                        User Management
                    </p>

                </header>

                <div class="container">

                <a class="back"
                   href="dashboard">
                   ← Back to Dashboard
                </a>

                <div class="card">

                <h2>
                    Manage Users
                </h2>
                """);

        if (error != null) {

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

        html.append("""
                <table>

                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                """);

        if (users == null || users.isEmpty()) {

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

                String statusClass =
                        status == null
                                ? ""
                                : status.toLowerCase();

                html.append("<tr>");

                html.append("<td>")
                        .append(user.getId())
                        .append("</td>");

                html.append("<td>")
                        .append(
                                escapeHtml(
                                        user.getUsername()
                                )
                        )
                        .append("</td>");

                html.append("<td>")
                        .append(
                                escapeHtml(
                                        user.getRole()
                                )
                        )
                        .append("</td>");

                html.append(
                                "<td class=\"status "
                                        + statusClass
                                        + "\">"
                        )
                        .append(
                                escapeHtml(status)
                        )
                        .append("</td>");

                html.append("<td>");

                /*
                 * ADMIN cannot be activated,
                 * deactivated or deleted.
                 */
                if ("ADMIN".equalsIgnoreCase(
                        user.getRole())) {

                    html.append(
                            "<strong>Protected</strong>"
                    );

                } else {

                    if ("PENDING".equalsIgnoreCase(
                            status)
                            ||
                            "INACTIVE".equalsIgnoreCase(
                                    status)) {

                        html.append("""
                                <form method="post"
                                      action="manage-users"
                                      class="action-form">

                                <input type="hidden"
                                       name="action"
                                       value="activate">

                                <input type="hidden"
                                       name="id"
                                """);

                        html.append(" value=\"")
                                .append(user.getId())
                                .append("\">");

                        html.append("""
                                <button type="submit"
                                        class="activate">
                                    Activate
                                </button>

                                </form>
                                """);
                    }

                    if ("ACTIVE".equalsIgnoreCase(
                            status)) {

                        html.append("""
                                <form method="post"
                                      action="manage-users"
                                      class="action-form"
                                      onsubmit="return confirm(
                                      'Deactivate this user?'
                                      );">

                                <input type="hidden"
                                       name="action"
                                       value="deactivate">

                                <input type="hidden"
                                       name="id"
                                """);

                        html.append(" value=\"")
                                .append(user.getId())
                                .append("\">");

                        html.append("""
                                <button type="submit"
                                        class="deactivate">
                                    Deactivate
                                </button>

                                </form>
                                """);
                    }

                    html.append("""
                            <form method="post"
                                  action="manage-users"
                                  class="action-form"
                                  onsubmit="return confirm(
                                  'Delete this user permanently?'
                                  );">

                            <input type="hidden"
                                   name="action"
                                   value="delete">

                            <input type="hidden"
                                   name="id"
                            """);

                    html.append(" value=\"")
                            .append(user.getId())
                            .append("\">");

                    html.append("""
                            <button type="submit"
                                    class="delete">
                                Delete
                            </button>

                            </form>
                            """);
                }

                html.append("</td>");

                html.append("</tr>");
            }
        }

        html.append("""
                </table>

                </div>

                </div>

                </body>

                </html>
                """);

        response.getWriter()
                .write(html.toString());
    }

    private String escapeHtml(String text) {

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