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

        String contextPath =
                httpRequest.getContextPath();

        String requestURI =
                httpRequest.getRequestURI();

        String path =
                requestURI.substring(
                        contextPath.length()
                );

        /*
         * Public pages.
         *
         * These pages can be accessed
         * without logging in.
         */
        if (path.equals("/login")
                || path.equals("/register")
                || path.equals("/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")) {

            chain.doFilter(
                    request,
                    response
            );

            return;
        }

        /*
         * Check session.
         */
        HttpSession session =
                httpRequest.getSession(false);

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
         * User is not logged in.
         */
        if (loggedInUser == null) {

            httpResponse.sendRedirect(
                    contextPath + "/login"
            );

            return;
        }

        /*
         * User must still be ACTIVE.
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
         * Continue to requested page.
         */
        chain.doFilter(
                request,
                response
        );
    }
}