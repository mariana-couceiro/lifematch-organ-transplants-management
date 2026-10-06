package com.lifematch.controller;

import java.util.List;

import com.lifematch.model.OrganType;
import com.lifematch.service.OrganTypeService;

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


@Path("/organ-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganTypeController {

    private final OrganTypeService service =
            new OrganTypeService();


    // CREATE
    @POST
    public Response createOrganType(
            OrganType organType) {

        try {

            OrganType createdOrganType =
                    service.createOrganType(
                            organType
                    );

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


    // GET ALL
    @GET
    public Response getAllOrganTypes() {

        List<OrganType> organTypes =
                service.getAllOrganTypes();

        return Response
                .ok(organTypes)
                .build();
    }


    // GET BY ID
    @GET
    @Path("/{id}")
    public Response getOrganTypeById(
            @PathParam("id") int id) {

        OrganType organType =
                service.getOrganTypeById(id);

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


    // UPDATE
    @PUT
    @Path("/{id}")
    public Response updateOrganType(
            @PathParam("id") int id,
            OrganType organType) {

        try {

            OrganType updatedOrganType =
                    service.updateOrganType(
                            id,
                            organType
                    );

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


    // DELETE
    @DELETE
    @Path("/{id}")
    public Response deleteOrganType(
            @PathParam("id") int id) {

        boolean deleted =
                service.deleteOrganType(id);

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
