package com.lifematch.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthorizationService {

        // CHECK IF USER IS LOGGED IN
        public boolean isAuthenticated(
                        HttpServletRequest request) {

                HttpSession session = request.getSession(false);

                return session != null &&
                                session.getAttribute("userId") != null;
        }

        // CHECK IF USER IS ADMIN
        public boolean isAdmin(
                        HttpServletRequest request) {

                HttpSession session = request.getSession(false);

                if (session == null) {
                        return false;
                }

                String role = (String) session.getAttribute("role");

                return role != null &&
                                role.equalsIgnoreCase("ADMIN");
        }
}
