package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/help")
public class HelpServlet extends HttpServlet {

    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // =====================================================
        // CHECK LOGIN
        // =====================================================

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }

        // =====================================================
        // GET LOGGED-IN USER
        // =====================================================

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }

        User user =
                (User) userObject;

        // =====================================================
        // CHECK USER STATUS
        // =====================================================

        if (user.getStatus() == null ||
                !"ACTIVE".equalsIgnoreCase(
                        user.getStatus()
                )) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath()
                            + "/login"
            );

            return;
        }

        // =====================================================
        // SEND USER TO JSP
        // =====================================================

        request.setAttribute(
                "loggedInUser",
                user
        );

        // =====================================================
        // OPEN HELP JSP
        // =====================================================

        request.getRequestDispatcher(
                "/WEB-INF/views/help.jsp"
        ).forward(
                request,
                response
        );
    }
}