package com.sunrise.dentalclinic.filter;

import com.sunrise.dentalclinic.entity.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;


        /*
         * ============================
         * CONTEXT PATH
         * ============================
         */

        String contextPath =
                httpRequest.getContextPath();


        /*
         * ============================
         * REQUEST PATH
         * ============================
         */

        String requestURI =
                httpRequest.getRequestURI();

        String path =
                requestURI.substring(
                        contextPath.length()
                );


        /*
         * ============================
         * PUBLIC PAGES
         * ============================
         *
         * These pages do NOT require
         * a logged-in user.
         */

        if (path.equals("/")
                || path.equals("/index.html")
                || path.equals("/login")
                || path.equals("/register")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/favicon.ico")) {

            chain.doFilter(
                    request,
                    response
            );

            return;
        }


        /*
         * ============================
         * GET EXISTING SESSION
         * ============================
         *
         * IMPORTANT:
         * false means:
         * do NOT create a new session.
         */

        HttpSession session =
                httpRequest.getSession(false);


        /*
         * ============================
         * GET LOGGED-IN USER
         * ============================
         */

        User loggedInUser = null;

        if (session != null) {

            Object userObject =
                    session.getAttribute(
                            "loggedInUser"
                    );

            if (userObject instanceof User) {

                loggedInUser =
                        (User) userObject;
            }
        }


        /*
         * ============================
         * USER NOT LOGGED IN
         * ============================
         */

        if (loggedInUser == null) {

            httpResponse.sendRedirect(
                    contextPath + "/login"
            );

            return;
        }


        /*
         * ============================
         * CHECK USER STATUS
         * ============================
         *
         * Only ACTIVE users can use
         * the system.
         */

        if (!"ACTIVE".equalsIgnoreCase(
                loggedInUser.getStatus())) {

            session.invalidate();

            httpResponse.sendRedirect(
                    contextPath + "/login"
            );

            return;
        }


        /*
         * ============================
         * KEEP SESSION INFORMATION
         * ============================
         *
         * Make sure these values remain
         * available throughout the system.
         */

        session.setAttribute(
                "loggedInUser",
                loggedInUser
        );

        session.setAttribute(
                "userId",
                loggedInUser.getId()
        );

        session.setAttribute(
                "username",
                loggedInUser.getUsername()
        );

        session.setAttribute(
                "role",
                loggedInUser.getRole()
        );


        /*
         * ============================
         * ALLOW REQUEST
         * ============================
         */

        chain.doFilter(
                request,
                response
        );
    }
}