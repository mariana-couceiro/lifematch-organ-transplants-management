package com.lifematch.controller;

import java.util.List;

import com.lifematch.dto.CancellationRequest;
import com.lifematch.dto.CandidateMatchResponse;
import com.lifematch.dto.CorrespondenceResponse;
import com.lifematch.dto.CorrespondenceSummaryResponse;

import com.lifematch.service.AuthorizationService;
import com.lifematch.service.CorrespondenceService;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.inject.Inject;

@Path("/correspondences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorrespondenceController {

        @Inject
        private CorrespondenceService service;

        private final AuthorizationService authorizationService = new AuthorizationService();

        // GET CORRESPONDENCES - USER AND ADMIN
        @GET
        public Response getCorrespondences(
                        @QueryParam("organId") Integer organId,
                        @QueryParam("candidateId") Integer candidateId,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                if (organId != null) {

                        List<CorrespondenceSummaryResponse> correspondences = service
                                        .getCorrespondenceSummariesByOrganId(
                                                        organId);

                        return Response.ok(correspondences).build();
                }

                if (candidateId != null) {

                        List<CorrespondenceSummaryResponse> correspondences = service
                                        .getCorrespondenceSummariesByCandidateId(
                                                        candidateId);

                        return Response.ok(correspondences).build();
                }

                List<CorrespondenceSummaryResponse> correspondences = service.getAllCorrespondenceSummaries();

                return Response.ok(correspondences).build();
        }

        // GET FULL HISTORY - ADMIN ONLY
        @GET
        @Path("/history")
        public Response getHistory(
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

                List<CorrespondenceResponse> correspondences = service.getAllCorrespondences();

                return Response.ok(correspondences).build();
        }

        // GET CANDIDATE RANKING - USER AND ADMIN
        @GET
        @Path("/organ/{organId}/ranking")
        public Response getRanking(
                        @PathParam("organId") int organId,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                try {

                        List<CandidateMatchResponse> ranking = service.getRanking(organId);

                        if (ranking.isEmpty()) {
                                return Response
                                                .status(Response.Status.NOT_FOUND)
                                                .entity("No eligible candidates found.")
                                                .build();
                        }

                        return Response.ok(ranking).build();

                } catch (IllegalArgumentException e) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // CREATE PROPOSAL - USER AND ADMIN
        @POST
        @Path("/organ/{organId}/proposal")
        public Response createProposal(
                        @PathParam("organId") int organId,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                try {

                        CorrespondenceResponse correspondence = service.createProposal(organId);

                        return Response
                                        .status(Response.Status.CREATED)
                                        .entity(correspondence)
                                        .build();

                } catch (IllegalArgumentException e) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // CONFIRM PROPOSAL - USER AND ADMIN
        @POST
        @Path("/{id}/confirm")
        public Response confirmProposal(
                        @PathParam("id") int id,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                try {

                        CorrespondenceResponse correspondence = service.confirmProposal(id);

                        return Response.ok(correspondence).build();

                } catch (IllegalArgumentException e) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }

        // CANCEL PROPOSAL - USER AND ADMIN
        @POST
        @Path("/{id}/cancel")
        public Response cancelProposal(
                        @PathParam("id") int id,
                        CancellationRequest cancellationRequest,
                        @Context HttpServletRequest request) {

                if (!authorizationService.isAuthenticated(request)) {
                        return Response
                                        .status(Response.Status.UNAUTHORIZED)
                                        .entity("Authentication required.")
                                        .build();
                }

                try {

                        if (cancellationRequest == null) {
                                return Response
                                                .status(Response.Status.BAD_REQUEST)
                                                .entity("Cancellation reason is required.")
                                                .build();
                        }

                        CorrespondenceResponse correspondence = service.cancelAndCreateNext(
                                        id,
                                        cancellationRequest.reason());

                        // Do not expose cancellation reasons
                        // through the operational endpoint.
                        CorrespondenceSummaryResponse summary = new CorrespondenceSummaryResponse(
                                        correspondence.id(),
                                        correspondence.candidateId(),
                                        correspondence.candidateName(),
                                        correspondence.organId(),
                                        correspondence.organType(),
                                        correspondence.compatibilityScore(),
                                        correspondence.correspondenceDate(),
                                        correspondence.status());

                        return Response.ok(summary).build();

                } catch (IllegalArgumentException e) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity(e.getMessage())
                                        .build();
                }
        }
}
