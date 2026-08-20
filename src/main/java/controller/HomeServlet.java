package com.sunrise.dentalclinic.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.getWriter().println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Sunrise Dental Clinic</title>
                </head>
                <body>
                    <h1>Sunrise Dental Clinic</h1>
                    <p>Servlet is working successfully.</p>
                </body>
                </html>
                """);
    }
}