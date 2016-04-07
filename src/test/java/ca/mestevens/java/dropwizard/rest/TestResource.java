package ca.mestevens.java.dropwizard.rest;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("")
public class TestResource {

    public TestResource() {

    }

    @GET
    @Path("endpoint/{user}")
    @RolesAllowed("user:{user}:write")
    public Response endpoint() {
        return Response.ok().build();
    }

    @GET
    @Path("endpoint2/{user}")
    @RolesAllowed("user:*:read")
    public Response endpoint2() {
        return Response.ok().build();
    }

    @GET
    @Path("endpoint3/{user}")
    @RolesAllowed("user:{usar}:read")
    public Response endpoint3() {
        return Response.ok().build();
    }

    @GET
    @Path("endpoint4/{user}")
    @PermitAll
    public Response endpoint4() {
        return Response.ok().build();
    }

    @GET
    @Path("endpoint5/{user}")
    @DenyAll
    public Response endpoint5() {
        return Response.ok().build();
    }

}
