```jsp
<%@ page import="java.util.List" %>
<%@ page import="com.sunrise.dentalclinic.entity.User" %>

<%
    List<User> users =
            (List<User>) request.getAttribute("users");

    String error =
            (String) request.getAttribute("error");

    String success =
            (String) request.getAttribute("success");

    User loggedInUser =
            (User) session.getAttribute("loggedInUser");

    String username =
            loggedInUser != null
                    ? loggedInUser.getUsername()
                    : "Admin";
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Manage Users - Sunrise Dental Clinic</title>

    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, Helvetica, sans-serif;
            background: #f5f7fb;
            color: #1f2937;
        }

        .main-content {
            margin-left: 260px;
            min-height: 100vh;
            padding: 25px 30px;
        }

        /* =========================
           TOP BAR
           ========================= */

        .topbar {
            background: white;
            border-radius: 14px;
            padding: 18px 25px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .page-title h1 {
            font-size: 25px;
            margin-bottom: 5px;
        }

        .page-title p {
            color: #6b7280;
            font-size: 14px;
        }

        .admin-info {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .admin-avatar {
            width: 42px;
            height: 42px;
            border-radius: 50%;
            background: #2563eb;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
        }

        .admin-name {
            font-size: 14px;
            font-weight: 600;
        }

        .admin-role {
            font-size: 12px;
            color: #6b7280;
            margin-top: 3px;
        }

        /* =========================
           MESSAGES
           ========================= */

        .error-message {
            background: #fee2e2;
            color: #b91c1c;
            padding: 14px 18px;
            border-radius: 10px;
            margin-bottom: 20px;
            border: 1px solid #fecaca;
            font-size: 14px;
        }

        .success-message {
            background: #dcfce7;
            color: #15803d;
            padding: 14px 18px;
            border-radius: 10px;
            margin-bottom: 20px;
            border: 1px solid #bbf7d0;
            font-size: 14px;
        }

        /* =========================
           CARDS
           ========================= */

        .card {
            background: white;
            border-radius: 14px;
            padding: 25px;
            margin-bottom: 25px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .card-header {
            margin-bottom: 20px;
        }

        .card-header h2 {
            font-size: 20px;
            margin-bottom: 5px;
        }

        .card-header p {
            color: #6b7280;
            font-size: 13px;
        }

        /* =========================
           CREATE FORMS
           ========================= */

        .forms-container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 25px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group label {
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .input-wrapper {
            position: relative;
        }

        .form-control {
            width: 100%;
            height: 42px;
            padding: 0 45px 0 13px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            outline: none;
            font-size: 14px;
            background: white;
        }

        .form-control:focus {
            border-color: #2563eb;
            box-shadow: 0 0 0 2px rgba(37,99,235,0.08);
        }

        .password-toggle {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            border: none;
            background: transparent;
            cursor: pointer;
            color: #64748b;
            width: 28px;
            height: 28px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .password-toggle:hover {
            color: #2563eb;
        }

        .create-btn {
            height: 42px;
            padding: 0 20px;
            border: none;
            border-radius: 8px;
            background: #2563eb;
            color: white;
            font-weight: 600;
            cursor: pointer;
            margin-top: 18px;
            width: 100%;
        }

        .create-btn:hover {
            background: #1d4ed8;
        }

        .admin-create-btn {
            background: #111827;
        }

        .admin-create-btn:hover {
            background: #030712;
        }

        /* =========================
           TABLE
           ========================= */

        .table-container {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 750px;
        }

        th {
            background: #f8fafc;
            color: #475569;
            font-size: 13px;
            font-weight: 600;
            text-align: left;
            padding: 14px 12px;
            border-bottom: 1px solid #e5e7eb;
        }

        td {
            padding: 15px 12px;
            border-bottom: 1px solid #eef0f3;
            font-size: 14px;
            vertical-align: middle;
        }

        tr:hover {
            background: #fafafa;
        }

        /* =========================
           BADGES
           ========================= */

        .role-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
            background: #e0e7ff;
            color: #3730a3;
        }

        .admin-role-badge {
            background: #e5e7eb;
            color: #111827;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
        }

        .status-active {
            background: #dcfce7;
            color: #15803d;
        }

        .status-pending {
            background: #fef3c7;
            color: #b45309;
        }

        .status-inactive {
            background: #fee2e2;
            color: #b91c1c;
        }

        /* =========================
           ACTIONS
           ========================= */

        .actions {
            display: flex;
            gap: 7px;
            flex-wrap: wrap;
        }

        .action-btn {
            border: none;
            padding: 7px 11px;
            border-radius: 6px;
            font-size: 11px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
        }

        .password-btn {
            background: #e0e7ff;
            color: #3730a3;
        }

        .password-btn:hover {
            background: #c7d2fe;
        }

        .activate-btn {
            background: #dcfce7;
            color: #15803d;
        }

        .activate-btn:hover {
            background: #bbf7d0;
        }

        .deactivate-btn {
            background: #fef3c7;
            color: #b45309;
        }

        .deactivate-btn:hover {
            background: #fde68a;
        }

        .delete-btn {
            background: #fee2e2;
            color: #b91c1c;
        }

        .delete-btn:hover {
            background: #fecaca;
        }

        .protected {
            color: #64748b;
            font-size: 12px;
            font-style: italic;
        }

        /* =========================
           PASSWORD MODAL
           ========================= */

        .modal {
            display: none;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background: rgba(15,23,42,0.55);
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .modal.show {
            display: flex;
        }

        .modal-content {
            background: white;
            width: 100%;
            max-width: 430px;
            border-radius: 14px;
            padding: 25px;
            box-shadow: 0 15px 40px rgba(0,0,0,0.2);
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .modal-header h3 {
            font-size: 20px;
        }

        .close-modal {
            border: none;
            background: transparent;
            font-size: 22px;
            cursor: pointer;
            color: #64748b;
        }

        .close-modal:hover {
            color: #111827;
        }

        .modal-form-group {
            margin-bottom: 18px;
        }

        .modal-form-group label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .modal-input-wrapper {
            position: relative;
        }

        .modal-input {
            width: 100%;
            height: 42px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            padding: 0 45px 0 13px;
            outline: none;
            font-size: 14px;
        }

        .modal-input:focus {
            border-color: #2563eb;
        }

        .modal-password-toggle {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            border: none;
            background: transparent;
            cursor: pointer;
            color: #64748b;
        }

        .update-password-btn {
            width: 100%;
            height: 42px;
            border: none;
            border-radius: 8px;
            background: #2563eb;
            color: white;
            font-weight: 600;
            cursor: pointer;
        }

        .update-password-btn:hover {
            background: #1d4ed8;
        }

        .empty-state {
            text-align: center;
            padding: 35px;
            color: #6b7280;
            font-size: 14px;
        }

        /* =========================
           RESPONSIVE
           ========================= */

        @media (max-width: 1000px) {

            .forms-container {
                grid-template-columns: 1fr;
            }

        }

        @media (max-width: 900px) {

            .main-content {
                margin-left: 0;
                padding: 20px;
            }

        }

        @media (max-width: 600px) {

            .topbar {
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }

            .card {
                padding: 18px;
            }

            .form-row {
                grid-template-columns: 1fr;
            }

            .page-title h1 {
                font-size: 22px;
            }

        }

    </style>

</head>


<body>


<!-- EXISTING SIDEBAR -->

<jsp:include page="sidebar.jsp" />


<div class="main-content">


    <!-- =========================
         TOP BAR
         ========================= -->

    <div class="topbar">

        <div class="page-title">

            <h1>
                Manage Users
            </h1>

            <p>
                Create and manage system user accounts
            </p>

        </div>


        <div class="admin-info">

            <div class="admin-avatar">

                <%= username.substring(0, 1).toUpperCase() %>

            </div>

            <div>

                <div class="admin-name">
                    <%= username %>
                </div>

                <div class="admin-role">
                    Administrator
                </div>

            </div>

        </div>

    </div>


    <!-- =========================
         MESSAGES
         ========================= -->

    <% if (error != null && !error.isBlank()) { %>

        <div class="error-message">

            <i class="fa-solid fa-circle-exclamation"></i>

            &nbsp;

            <%= error %>

        </div>

    <% } %>


    <% if (success != null && !success.isBlank()) { %>

        <div class="success-message">

            <i class="fa-solid fa-circle-check"></i>

            &nbsp;

            <%= success %>

        </div>

    <% } %>


    <!-- =========================
         CREATE USER + ADMIN
         ========================= -->

    <div class="forms-container">


        <!-- CREATE USER -->

        <div class="card">

            <div class="card-header">

                <h2>
                    Create New User
                </h2>

                <p>
                    Create a regular system user account
                </p>

            </div>


            <form
                    method="post"
                    action="<%= request.getContextPath() %>/manage-users">


                <input
                        type="hidden"
                        name="action"
                        value="create">


                <div class="form-row">


                    <div class="form-group">

                        <label for="userUsername">
                            Username
                        </label>

                        <input
                                type="text"
                                id="userUsername"
                                name="username"
                                class="form-control"
                                placeholder="Enter username"
                                required>

                    </div>


                    <div class="form-group">

                        <label for="userPassword">
                            Password
                        </label>

                        <div class="input-wrapper">

                            <input
                                    type="password"
                                    id="userPassword"
                                    name="password"
                                    class="form-control"
                                    placeholder="Enter password"
                                    required>

                            <button
                                    type="button"
                                    class="password-toggle"
                                    onclick="togglePassword(
                                        'userPassword',
                                        'userEye'
                                    )">

                                <i
                                        id="userEye"
                                        class="fa-solid fa-eye">
                                </i>

                            </button>

                        </div>

                    </div>


                </div>


                <button
                        type="submit"
                        class="create-btn">

                    <i class="fa-solid fa-user-plus"></i>

                    &nbsp;

                    Create User

                </button>


            </form>

        </div>


        <!-- CREATE ADMIN -->

        <div class="card">

            <div class="card-header">

                <h2>
                    Create New Admin
                </h2>

                <p>
                    Create a new administrator account
                </p>

            </div>


            <form
                    method="post"
                    action="<%= request.getContextPath() %>/manage-users">


                <input
                        type="hidden"
                        name="action"
                        value="create-admin">


                <div class="form-row">


                    <div class="form-group">

                        <label for="adminUsername">
                            Username
                        </label>

                        <input
                                type="text"
                                id="adminUsername"
                                name="username"
                                class="form-control"
                                placeholder="Enter admin username"
                                required>

                    </div>


                    <div class="form-group">

                        <label for="adminPassword">
                            Password
                        </label>

                        <div class="input-wrapper">

                            <input
                                    type="password"
                                    id="adminPassword"
                                    name="password"
                                    class="form-control"
                                    placeholder="Enter password"
                                    required>

                            <button
                                    type="button"
                                    class="password-toggle"
                                    onclick="togglePassword(
                                        'adminPassword',
                                        'adminEye'
                                    )">

                                <i
                                        id="adminEye"
                                        class="fa-solid fa-eye">
                                </i>

                            </button>

                        </div>

                    </div>


                </div>


                <button
                        type="submit"
                        class="create-btn admin-create-btn">

                    <i class="fa-solid fa-user-shield"></i>

                    &nbsp;

                    Create Admin

                </button>


            </form>

        </div>


    </div>


    <!-- =========================
         SYSTEM USERS
         ========================= -->

    <div class="card">


        <div class="card-header">

            <h2>
                System Users
            </h2>

            <p>
                Manage existing system accounts
            </p>

        </div>


        <div class="table-container">


            <table>


                <thead>

                <tr>

                    <th>
                        ID
                    </th>

                    <th>
                        Username
                    </th>

                    <th>
                        Role
                    </th>

                    <th>
                        Status
                    </th>

                    <th>
                        Actions
                    </th>

                </tr>

                </thead>


                <tbody>


                <%

                    if (users != null && !users.isEmpty()) {

                        for (User user : users) {

                            String status =
                                    user.getStatus() != null
                                            ? user.getStatus()
                                            : "";

                            String role =
                                    user.getRole() != null
                                            ? user.getRole()
                                            : "";

                            String statusClass;

                            if ("ACTIVE".equalsIgnoreCase(status)) {

                                statusClass =
                                        "status-active";

                            } else if ("PENDING".equalsIgnoreCase(status)) {

                                statusClass =
                                        "status-pending";

                            } else {

                                statusClass =
                                        "status-inactive";
                            }

                            String roleClass =
                                    "ADMIN".equalsIgnoreCase(role)
                                            ? "admin-role-badge"
                                            : "";

                %>


                <tr>


                    <!-- ID -->

                    <td>

                        <%= user.getId() %>

                    </td>


                    <!-- USERNAME -->

                    <td>

                        <strong>

                            <%= user.getUsername() %>

                        </strong>

                    </td>


                    <!-- ROLE -->

                    <td>

                        <span
                                class="role-badge <%= roleClass %>">

                            <%= role %>

                        </span>

                    </td>


                    <!-- STATUS -->

                    <td>

                        <span
                                class="status-badge <%= statusClass %>">

                            <%= status %>

                        </span>

                    </td>


                    <!-- ACTIONS -->

                    <td>

                        <div class="actions">


                            <% if ("ADMIN".equalsIgnoreCase(role)) { %>


                                <!-- ADMIN PASSWORD -->

                                <button
                                        type="button"
                                        class="action-btn password-btn"
                                        onclick="openPasswordModal(
                                            <%= user.getId() %>,
                                            '<%= user.getUsername().replace("'", "\\'") %>'
                                        )">

                                    <i class="fa-solid fa-key"></i>

                                    &nbsp;

                                    Edit Password

                                </button>


                            <% } else { %>


                                <!-- ACTIVATE -->

                                <form
                                        method="post"
                                        action="<%= request.getContextPath() %>/manage-users">


                                    <input
                                            type="hidden"
                                            name="action"
                                            value="activate">


                                    <input
                                            type="hidden"
                                            name="id"
                                            value="<%= user.getId() %>">


                                    <button
                                            type="submit"
                                            class="action-btn activate-btn">

                                        <i class="fa-solid fa-check"></i>

                                        &nbsp;

                                        Activate

                                    </button>


                                </form>


                                <!-- DEACTIVATE -->

                                <form
                                        method="post"
                                        action="<%= request.getContextPath() %>/manage-users">


                                    <input
                                            type="hidden"
                                            name="action"
                                            value="deactivate">


                                    <input
                                            type="hidden"
                                            name="id"
                                            value="<%= user.getId() %>">


                                    <button
                                            type="submit"
                                            class="action-btn deactivate-btn">

                                        <i class="fa-solid fa-ban"></i>

                                        &nbsp;

                                        Deactivate

                                    </button>


                                </form>


                                <!-- DELETE -->

                                <form
                                        method="post"
                                        action="<%= request.getContextPath() %>/manage-users"
                                        onsubmit="return confirm(
                                            'Are you sure you want to delete this user?'
                                        );">


                                    <input
                                            type="hidden"
                                            name="action"
                                            value="delete">


                                    <input
                                            type="hidden"
                                            name="id"
                                            value="<%= user.getId() %>">


                                    <button
                                            type="submit"
                                            class="action-btn delete-btn">

                                        <i class="fa-solid fa-trash"></i>

                                        &nbsp;

                                        Delete

                                    </button>


                                </form>


                            <% } %>


                        </div>

                    </td>


                </tr>


                <%

                        }

                    } else {

                %>


                <tr>

                    <td colspan="5">

                        <div class="empty-state">

                            No users found.

                        </div>

                    </td>

                </tr>


                <%

                    }

                %>


                </tbody>

            </table>

        </div>

    </div>


</div>


<!-- =========================
     EDIT ADMIN PASSWORD MODAL
     ========================= -->

<div
        id="passwordModal"
        class="modal">


    <div class="modal-content">


        <div class="modal-header">

            <h3>
                Edit Admin Password
            </h3>

            <button
                    type="button"
                    class="close-modal"
                    onclick="closePasswordModal()">

                <i class="fa-solid fa-xmark"></i>

            </button>

        </div>


        <form
                method="post"
                action="<%= request.getContextPath() %>/manage-users">


            <input
                    type="hidden"
                    name="action"
                    value="update-password">


            <input
                    type="hidden"
                    id="passwordUserId"
                    name="id">


            <div class="modal-form-group">

                <label>
                    Admin Username
                </label>

                <input
                        type="text"
                        id="passwordUsername"
                        class="modal-input"
                        readonly>

            </div>


            <div class="modal-form-group">

                <label for="newPassword">
                    New Password
                </label>


                <div class="modal-input-wrapper">

                    <input
                            type="password"
                            id="newPassword"
                            name="password"
                            class="modal-input"
                            placeholder="Enter new password"
                            required>


                    <button
                            type="button"
                            class="modal-password-toggle"
                            onclick="togglePassword(
                                'newPassword',
                                'newPasswordEye'
                            )">

                        <i
                                id="newPasswordEye"
                                class="fa-solid fa-eye">
                        </i>

                    </button>

                </div>

            </div>


            <button
                    type="submit"
                    class="update-password-btn">

                <i class="fa-solid fa-key"></i>

                &nbsp;

                Update Password

            </button>


        </form>


    </div>

</div>


<script>


    /* =========================
       PASSWORD SHOW / HIDE
       ========================= */

    function togglePassword(
            inputId,
            iconId) {

        const input =
                document.getElementById(inputId);

        const icon =
                document.getElementById(iconId);


        if (input.type === "password") {

            input.type = "text";

            icon.classList.remove(
                    "fa-eye"
            );

            icon.classList.add(
                    "fa-eye-slash"
            );

        } else {

            input.type = "password";

            icon.classList.remove(
                    "fa-eye-slash"
            );

            icon.classList.add(
                    "fa-eye"
            );
        }

    }


    /* =========================
       OPEN PASSWORD MODAL
       ========================= */

    function openPasswordModal(
            id,
            username) {

        document.getElementById(
                "passwordUserId"
        ).value = id;


        document.getElementById(
                "passwordUsername"
        ).value = username;


        document.getElementById(
                "newPassword"
        ).value = "";


        document.getElementById(
                "passwordModal"
        ).classList.add("show");

    }


    /* =========================
       CLOSE PASSWORD MODAL
       ========================= */

    function closePasswordModal() {

        document.getElementById(
                "passwordModal"
        ).classList.remove("show");

    }


    /* =========================
       CLOSE WHEN CLICK OUTSIDE
       ========================= */

    window.onclick = function(event) {

        const modal =
                document.getElementById(
                        "passwordModal"
                );

        if (event.target === modal) {

            closePasswordModal();

        }

    };


</script>


</body>

</html>
```
