package com.lifematch.controller;

import java.util.List;

import com.lifematch.dto.CancellationRequest;
import com.lifematch.dto.CandidateMatchResponse;
import com.lifematch.dto.CorrespondenceResponse;
import com.lifematch.service.CorrespondenceService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/correspondences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorrespondenceController {

        private final CorrespondenceService service = new CorrespondenceService();

        // GET CORRESPONDENCES

        @GET
        public Response getCorrespondences(
                        @QueryParam("organId") Integer organId,

                        @QueryParam("candidateId") Integer candidateId) {

                // FILTER BY ORGAN
                if (organId != null) {

                        List<CorrespondenceResponse> correspondences = service
                                        .getCorrespondencesByOrganId(
                                                        organId);

                        return Response
                                        .ok(correspondences)
                                        .build();
                }

                // FILTER BY CANDIDATE
                if (candidateId != null) {

                        List<CorrespondenceResponse> correspondences = service
                                        .getCorrespondencesByCandidateId(
                                                        candidateId);

                        return Response
                                        .ok(correspondences)
                                        .build();
                }

                // NO FILTER -> GET ALL
                List<CorrespondenceResponse> correspondences = service
                                .getAllCorrespondences();

                return Response
                                .ok(correspondences)
                                .build();
        }

        // GET CANDIDATE RANKING FOR ORGAN

        @GET
        @Path("/organ/{organId}/ranking")
        public Response getRanking(
                        @PathParam("organId") int organId) {

                try {

                        List<CandidateMatchResponse> ranking = service.getRanking(
                                        organId);

                        if (ranking.isEmpty()) {

                                return Response
                                                .status(
                                                                Response.Status.NOT_FOUND)
                                                .entity(
                                                                "No eligible candidates found.")
                                                .build();
                        }

                        return Response
                                        .ok(ranking)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(
                                                        Response.Status.BAD_REQUEST)
                                        .entity(
                                                        e.getMessage())
                                        .build();
                }
        }

        // CREATE PROPOSAL

        @POST
        @Path("/organ/{organId}/proposal")
        public Response createProposal(
                        @PathParam("organId") int organId) {

                try {

                        CorrespondenceResponse correspondence = service.createProposal(
                                        organId);

                        return Response
                                        .status(
                                                        Response.Status.CREATED)
                                        .entity(
                                                        correspondence)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(
                                                        Response.Status.BAD_REQUEST)
                                        .entity(
                                                        e.getMessage())
                                        .build();
                }
        }

        // CONFIRM PROPOSAL

        @POST
        @Path("/{id}/confirm")
        public Response confirmProposal(
                        @PathParam("id") int id) {

                try {

                        CorrespondenceResponse correspondence = service.confirmProposal(
                                        id);

                        return Response
                                        .ok(correspondence)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(
                                                        Response.Status.BAD_REQUEST)
                                        .entity(
                                                        e.getMessage())
                                        .build();
                }
        }

        // CANCEL PROPOSAL

        @POST
        @Path("/{id}/cancel")
        public Response cancelProposal(
                        @PathParam("id") int id,
                        CancellationRequest request) {

                try {

                        if (request == null) {

                                return Response
                                                .status(
                                                                Response.Status.BAD_REQUEST)
                                                .entity(
                                                                "Cancellation reason is required.")
                                                .build();
                        }

                        CorrespondenceResponse correspondence = service.cancelAndCreateNext(
                                        id,
                                        request.reason());

                        return Response
                                        .ok(correspondence)
                                        .build();

                } catch (IllegalArgumentException e) {

                        return Response
                                        .status(
                                                        Response.Status.BAD_REQUEST)
                                        .entity(
                                                        e.getMessage())
                                        .build();
                }
        }
}
