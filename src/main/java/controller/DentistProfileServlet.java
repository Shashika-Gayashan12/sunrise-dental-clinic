package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.Dentist;
import com.sunrise.dentalclinic.entity.User;
import com.sunrise.dentalclinic.service.DentistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dentist-profile")
public class DentistProfileServlet extends HttpServlet {


    private final DentistService dentistService =
            new DentistService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );
            return;
        }

        Object userObject =
                session.getAttribute("loggedInUser");

        if (!(userObject instanceof User)) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );
            return;
        }

        User user = (User) userObject;

        if (!"DENTIST".equalsIgnoreCase(user.getRole()) ||
                !"ACTIVE".equalsIgnoreCase(user.getStatus())) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?type=dentist"
            );
            return;
        }

        Long dentistId =
                user.getDentistId();

        if (dentistId == null ||
                dentistId <= 0) {

            request.setAttribute(
                    "error",
                    "Your account is not linked to a dentist profile."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-profile.jsp"
            ).forward(request, response);

            return;
        }

        try {

            Dentist dentist =
                    dentistService.getDentistById(
                            dentistId
                    );

            if (dentist == null) {

                request.setAttribute(
                        "error",
                        "Dentist profile not found."
                );

            } else {

                request.setAttribute(
                        "dentist",
                        dentist
                );
            }

            request.setAttribute(
                    "loggedInUser",
                    user
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-profile.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to load your profile."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/dentist-profile.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }


}
