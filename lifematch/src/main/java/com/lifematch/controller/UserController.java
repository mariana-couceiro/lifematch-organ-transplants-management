package com.lifematch.controller;

import java.util.List;

import com.lifematch.dto.UserResponse;
import com.lifematch.model.User;
import com.lifematch.service.AuthorizationService;
import com.lifematch.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.inject.Inject;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

        @Inject
        private UserService service;

        private final AuthorizationService authorizationService =
                new AuthorizationService();


        // CHECK ADMIN ACCESS
        private Response checkAdminAccess(
                        HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {

                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                if (!authorizationService.isAdmin(request)) {

                        return Response
                                        .status(Response.Status.FORBIDDEN)
                                        .entity("Admin access required.")
                                        .build();
                }

                return null;
        }

        // GET CURRENT USER ID
        private int getCurrentUserId(
                        HttpServletRequest request) {

                HttpSession session = request.getSession(false);

                return (Integer) session.getAttribute("userId");
        }

        // CREATE USER
        @POST
        public Response createUser(
                        User user,
                        @Context HttpServletRequest request) {

                Response accessError = checkAdminAccess(request);

                if (accessError != null) {
                        return accessError;
                }

                try {

                        UserResponse createdUser = service.createUser(user);

                        return Response
                                        .status(Response.Status.CREATED)
                                        .entity(createdUser)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // GET ALL USERS
        @GET
        public Response getAllUsers(
                        @Context HttpServletRequest request) {

                Response accessError = checkAdminAccess(request);

                if (accessError != null) {
                        return accessError;
                }

                List<UserResponse> users = service.getAllUsers();

                return Response
                                .ok(users)
                                .build();
        }

        // GET USER BY ID
        @GET
        @Path("/{id}")
        public Response getUserById(
                        @PathParam("id") int id,
                        @Context HttpServletRequest request) {

                Response accessError = checkAdminAccess(request);

                if (accessError != null) {
                        return accessError;
                }

                UserResponse user = service.getUserById(id);

                if (user == null) {

                        return Response
                                        .status(Response.Status.NOT_FOUND)
                                        .entity("User not found.")
                                        .build();
                }

                return Response
                                .ok(user)
                                .build();
        }

        // UPDATE USER
        @PUT
        @Path("/{id}")
        public Response updateUser(
                        @PathParam("id") int id,
                        User user,
                        @Context HttpServletRequest request) {

                Response accessError = checkAdminAccess(request);

                if (accessError != null) {
                        return accessError;
                }

                int currentUserId = getCurrentUserId(request);

                try {

                        UserResponse updatedUser = service.updateUser(
                                        id,
                                        user,
                                        currentUserId);

                        if (updatedUser == null) {

                                return Response
                                                .status(Response.Status.NOT_FOUND)
                                                .entity("User not found.")
                                                .build();
                        }

                        return Response
                                        .ok(updatedUser)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // DELETE USER
        @DELETE
        @Path("/{id}")
        public Response deleteUser(
                        @PathParam("id") int id,
                        @Context HttpServletRequest request) {

                Response accessError = checkAdminAccess(request);

                if (accessError != null) {
                        return accessError;
                }

                int currentUserId = getCurrentUserId(request);

                try {

                        boolean deleted = service.deleteUser(
                                        id,
                                        currentUserId);

                        if (!deleted) {

                                return Response
                                                .status(Response.Status.NOT_FOUND)
                                                .entity("User not found.")
                                                .build();
                        }

                        return Response
                                        .ok("User deleted successfully.")
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }
}
