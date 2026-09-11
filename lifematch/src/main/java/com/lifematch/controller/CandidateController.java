package com.lifematch.controller;

import java.util.List;

import com.lifematch.model.Candidate;
import com.lifematch.service.CandidateService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/candidates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CandidateController {

    private final CandidateService service = new CandidateService();

    @POST
    public Response createCandidate(Candidate candidate) {
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

    @GET
    public List<Candidate> getAllCandidates() {
        return service.getAllCandidates();
    }

    @GET
    @Path("/{id}")
    public Response getCandidateById(@PathParam("id") int id) {

        Candidate candidate = service.getCandidateById(id);

        if (candidate == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Candidate not found.")
                    .build();
        }

        return Response.ok(candidate).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCandidate(
            @PathParam("id") int id,
            Candidate candidate) {

        try {

            Candidate updatedCandidate =
                    service.updateCandidate(id, candidate);

            if (updatedCandidate == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("Candidate not found.")
                        .build();
            }

            return Response.ok(updatedCandidate).build();

        } catch (IllegalArgumentException e) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCandidate(@PathParam("id") int id) {

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
