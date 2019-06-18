package org.redhat.emeapc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/quarkus")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());

    private static final List<Answer> answers = new ArrayList<>(1);

    @Inject
    Validator validator;

    public GreetingResource() {
        answers.add(new Answer("hello", "a typical answer"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Answer> hello() {
        LOGGER.warning("Hello was invoked().");
        return answers;
    }

    @PUT
    @Path("/{name}/{description}")
    public void addAnswer(@PathParam(value = "name") String name,@PathParam(value = "description") String description) {
        LOGGER.warning("add Answer was invoked() with:" + name + "/" + description);
        Answer newAnswer = new Answer(name, description);
        Set<ConstraintViolation<Answer>> violations = validator.validate(newAnswer);
        if ( ! violations.isEmpty() )
            throw new IllegalArgumentException(violations.toString());
        answers.clear();
        LOGGER.warning("answers list cleared.");
        answers.add(newAnswer);
        LOGGER.warning("answers list updated.");
    }
}
