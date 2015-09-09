package com.tomitribe.io.www;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/projects")
@Produces("application/json")
public class RestProjects {

    @Inject
    ServiceProjects serviceProjects;

    @GET
    public List<DtoProject> list() {
        return serviceProjects.getProjects();
    }
}
