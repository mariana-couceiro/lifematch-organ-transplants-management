package com.lifematch.controller;

import java.util.ArrayList;
import java.util.List;

import com.lifematch.dto.OrganEditResponse;
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

    private final OrganService service =
            new OrganService();


    // CREATE ORGAN
    @POST
    public Response createOrgan(Organ organ) {

        try {

            Organ createdOrgan =
                    service.createOrgan(organ);

            OrganResponse response =
                    toResponse(createdOrgan);

            return Response
                    .status(Response.Status.CREATED)
                    .entity(response)
                    .build();

        } catch (IllegalArgumentException e) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }


    // GET ALL ORGANS
    @GET
    public Response getAllOrgans() {

        List<Organ> organs =
                service.getAllOrgans();

        List<OrganResponse> responses =
                new ArrayList<>();

        for (Organ organ : organs) {

            responses.add(
                    toResponse(organ)
            );
        }

        return Response
                .ok(responses)
                .build();
    }


    // GET ORGAN BY ID
    @GET
    @Path("/{id}")
    public Response getOrganById(
            @PathParam("id") int id) {

        Organ organ =
                service.getOrganById(id);

        if (organ == null) {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Organ not found.")
                    .build();
        }

        OrganResponse response =
                toResponse(organ);

        return Response
                .ok(response)
                .build();
    }


    // GET ORGAN DATA FOR EDIT
    @GET
    @Path("/{id}/edit")
    public Response getOrganForEdit(
            @PathParam("id") int id) {

        Organ organ =
                service.getOrganById(id);

        if (organ == null) {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Organ not found.")
                    .build();
        }

        OrganEditResponse response =
                service.toEditResponse(organ);

        return Response
                .ok(response)
                .build();
    }


    // UPDATE ORGAN
    @PUT
    @Path("/{id}")
    public Response updateOrgan(
            @PathParam("id") int id,
            Organ organ) {

        try {

            Organ updatedOrgan =
                    service.updateOrgan(
                            id,
                            organ
                    );

            if (updatedOrgan == null) {

                return Response
                        .status(Response.Status.NOT_FOUND)
                        .entity("Organ not found.")
                        .build();
            }

            OrganResponse response =
                    toResponse(updatedOrgan);

            return Response
                    .ok(response)
                    .build();

        } catch (IllegalArgumentException e) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }


    // DELETE ORGAN
    @DELETE
    @Path("/{id}")
    public Response deleteOrgan(
            @PathParam("id") int id) {

        boolean deleted =
                service.deleteOrgan(id);

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


    // CONVERT ORGAN TO ORGAN RESPONSE
    private OrganResponse toResponse(
            Organ organ) {

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
