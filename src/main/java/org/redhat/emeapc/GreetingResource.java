package org.redhat.emeapc;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/quarkus")
public class GreetingResource {

	private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
    	LOGGER.warning("Hello was invoked().");
        return "hello";
    }
}