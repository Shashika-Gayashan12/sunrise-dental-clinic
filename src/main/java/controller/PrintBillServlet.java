package com.sunrise.dentalclinic.controller;

import com.sunrise.dentalclinic.entity.BillDetails;
import com.sunrise.dentalclinic.service.BillService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/printBill")
public class PrintBillServlet extends HttpServlet {

    private final BillService billService =
            new BillService();

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

        String billIdText =
                request.getParameter("id");

        if (billIdText == null ||
                billIdText.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Bill ID is required."
            );

            return;
        }

        Long billId;

        try {

            billId = Long.parseLong(
                    billIdText.trim()
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid bill ID."
            );

            return;
        }

        if (billId <= 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid bill ID."
            );

            return;
        }

        try {

            BillDetails billDetails =
                    billService.getBillDetails(
                            billId
                    );

            if (billDetails == null) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Bill not found."
                );

                return;
            }

            request.setAttribute(
                    "billDetails",
                    billDetails
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/print-bill.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load bill details.",
                    e
            );
        }
    }

    private boolean isLoggedIn(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        return session != null &&
                session.getAttribute(
                        "loggedInUser"
                ) != null;
    }


}
