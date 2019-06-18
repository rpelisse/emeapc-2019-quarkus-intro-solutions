package org.redhat.emeapc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/quarkus")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

	private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Answer> hello() {
    	LOGGER.warning("Hello was invoked().");
        List<Answer> answers = new ArrayList<>(1);
        answers.add(new Answer("hello", "a typical answer"));
        return answers;
    }
}
