package com.lifematch.controller;

import com.lifematch.dto.LoginRequest;
import com.lifematch.dto.LoginResponse;
import com.lifematch.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.inject.Inject;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

        @Inject
        private UserService service;

        // LOGIN
        @POST
        @Path("/login")
        public Response login(
                        LoginRequest loginRequest,
                        @Context HttpServletRequest request) {

                try {

                        LoginResponse loginResponse = service.login(loginRequest);

                        // Invalidate any previous session
                        HttpSession oldSession = request.getSession(false);

                        if (oldSession != null) {
                                oldSession.invalidate();
                        }

                        // Create a new session
                        HttpSession session = request.getSession(true);

                        session.setAttribute(
                                        "userId",
                                        loginResponse.id());

                        session.setAttribute(
                                        "username",
                                        loginResponse.username());

                        session.setAttribute(
                                        "role",
                                        loginResponse.role());

                        return Response
                                        .ok(loginResponse)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // CHECK CURRENT SESSION
        @GET
        @Path("/me")
        public Response getCurrentUser(
                        @Context HttpServletRequest request) {

                HttpSession session = request.getSession(false);

                if (session == null ||
                                session.getAttribute("userId") == null) {

                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                int userId = (Integer) session.getAttribute("userId");

                String username = (String) session.getAttribute("username");

                String role = (String) session.getAttribute("role");

                LoginResponse currentUser = new LoginResponse(
                                userId,
                                username,
                                role);

                return Response
                                .ok(currentUser)
                                .build();
        }

        // LOGOUT
        @POST
        @Path("/logout")
        public Response logout(
                        @Context HttpServletRequest request) {

                HttpSession session = request.getSession(false);

                if (session == null ||
                                session.getAttribute("userId") == null) {

                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("No active session.")
                                        .build();
                }

                session.invalidate();

                return Response
                                .ok("Logout successful.")
                                .build();
        }
}
