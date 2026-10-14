
package com.lifematch.controller;

import java.util.List;

import com.lifematch.model.OrganType;
import com.lifematch.service.OrganTypeService;
import com.lifematch.service.AuthorizationService;

import jakarta.servlet.http.HttpServletRequest;

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

@Path("/organ-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganTypeController {

        @Inject
        private OrganTypeService service;

        private final AuthorizationService authorizationService = new AuthorizationService();

        // CREATE - ADMIN ONLY
        @POST
        public Response createOrganType(
                        OrganType organType,
                        @Context HttpServletRequest request) {

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

                try {

                        OrganType createdOrganType = service.createOrganType(organType);

                        return Response
                                        .status(Response.Status.CREATED)
                                        .entity(createdOrganType)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // GET ALL - AUTHENTICATED USERS
        @GET
        public Response getAllOrganTypes(
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                List<OrganType> organTypes = service.getAllOrganTypes();

                return Response
                                .ok(organTypes)
                                .build();
        }

        // GET BY ID - AUTHENTICATED USERS
        @GET
        @Path("/{id}")
        public Response getOrganTypeById(
                        @PathParam("id") int id,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                OrganType organType = service.getOrganTypeById(id);

                if (organType == null) {
                        return Response
                                        .status(Response.Status.NOT_FOUND)
                                        .entity("Organ type not found.")
                                        .build();
                }

                return Response
                                .ok(organType)
                                .build();
        }

        // UPDATE - ADMIN ONLY
        @PUT
        @Path("/{id}")
        public Response updateOrganType(
                        @PathParam("id") int id,
                        OrganType organType,
                        @Context HttpServletRequest request) {

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

                try {

                        OrganType updatedOrganType = service.updateOrganType(id, organType);

                        if (updatedOrganType == null) {
                                return Response
                                                .status(Response.Status.NOT_FOUND)
                                                .entity("Organ type not found.")
                                                .build();
                        }

                        return Response
                                        .ok(updatedOrganType)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // DELETE - ADMIN ONLY
        @DELETE
        @Path("/{id}")
        public Response deleteOrganType(
                        @PathParam("id") int id,
                        @Context HttpServletRequest request) {

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

                boolean deleted = service.deleteOrganType(id);

                if (!deleted) {
                        return Response
                                        .status(Response.Status.NOT_FOUND)
                                        .entity("Organ type not found.")
                                        .build();
                }

                return Response
                                .ok("Organ type deleted successfully.")
                                .build();
        }
}
