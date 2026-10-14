
package com.lifematch.controller;

import java.util.List;

import com.lifematch.model.Candidate;
import com.lifematch.service.CandidateService;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.inject.Inject;

@Path("/candidates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CandidateController {

        @Inject
        private CandidateService service;

        private final AuthorizationService authorizationService = new AuthorizationService();

        // CREATE CANDIDATE
        @POST
        public Response createCandidate(
                        Candidate candidate,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                try {

                        Candidate createdCandidate = service.createCandidate(candidate);

                        return Response
                                        .status(Response.Status.CREATED)
                                        .entity(createdCandidate)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // GET ALL CANDIDATES OR SEARCH BY NAME
        @GET
        public Response getCandidates(
                        @QueryParam("name") String name,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                if (name != null && !name.isBlank()) {

                        List<Candidate> candidates = service.getCandidatesByName(name);

                        if (candidates.isEmpty()) {

                                return Response
                                                .status(Response.Status.NOT_FOUND)
                                                .entity("No candidates found.")
                                                .build();
                        }

                        return Response
                                        .ok(candidates)
                                        .build();
                }

                return Response
                                .ok(service.getAllCandidates())
                                .build();
        }

        // GET CANDIDATE BY ID
        @GET
        @Path("/{id}")
        public Response getCandidateById(
                        @PathParam("id") int id,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                Candidate candidate = service.getCandidateById(id);

                if (candidate == null) {

                        return Response
                                        .status(Response.Status.NOT_FOUND)
                                        .entity("Candidate not found.")
                                        .build();
                }

                return Response
                                .ok(candidate)
                                .build();
        }

        // UPDATE CANDIDATE
        @PUT
        @Path("/{id}")
        public Response updateCandidate(
                        @PathParam("id") int id,
                        Candidate candidate,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                try {

                        Candidate updatedCandidate = service.updateCandidate(id, candidate);

                        if (updatedCandidate == null) {

                                return Response
                                                .status(Response.Status.NOT_FOUND)
                                                .entity("Candidate not found.")
                                                .build();
                        }

                        return Response
                                        .ok(updatedCandidate)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // DELETE CANDIDATE - ADMIN ONLY
        @DELETE
        @Path("/{id}")
        public Response deleteCandidate(
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

                boolean deleted = service.deleteCandidate(id);

                if (!deleted) {

                        return Response
                                        .status(Response.Status.NOT_FOUND)
                                        .entity("Candidate not found.")
                                        .build();
                }

                return Response
                                .ok("Candidate deleted successfully.")
                                .build();
        }
}
