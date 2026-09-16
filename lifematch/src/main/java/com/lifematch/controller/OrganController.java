package com.lifematch.controller;

import java.util.List;

import com.lifematch.dto.OrganResponse;
import com.lifematch.model.Organ;
import com.lifematch.service.OrganService;

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

@Path("/organs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganController {

    private final OrganService service = new OrganService();

    @POST
    public Response createOrgan(Organ organ) {
        try {
            Organ createdOrgan = service.createOrgan(organ);

            return Response
                    .status(Response.Status.CREATED)
                    .entity(toResponse(createdOrgan))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    public List<OrganResponse> getAllOrgans() {
        return service.getAllOrgans()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response getOrganById(@PathParam("id") int id) {

        Organ organ = service.getOrganById(id);

        if (organ == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Organ not found.")
                    .build();
        }

        return Response.ok(toResponse(organ)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrgan(@PathParam("id") int id, Organ organ) {
        try {
            Organ updatedOrgan = service.updateOrgan(id, organ);

            if (updatedOrgan == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("Organ not found.")
                        .build();
            }

            return Response.ok(toResponse(updatedOrgan)).build();

        } catch (IllegalArgumentException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrgan(@PathParam("id") int id) {

        boolean deleted = service.deleteOrgan(id);

        if (!deleted) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Organ not found.")
                    .build();
        }

        return Response
                .ok("Organ deleted successfully.")
                .build();
    }

    private OrganResponse toResponse(Organ organ) {
        return new OrganResponse(
                organ.getId(),
                organ.getType(),
                organ.getBloodType(),
                organ.getDonorCode(),
                organ.getHospital(),
                organ.getAvailabilityDate(),
                organ.getStatus()
        );
    }
}
