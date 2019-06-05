package org.redhat.emeapc;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@Path("/quarkus")
@ApplicationScoped
public class GreetingResource {

	private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());
	
	void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
    	LOGGER.warning("Hello was invoked().");
        return "hello";
    }
}