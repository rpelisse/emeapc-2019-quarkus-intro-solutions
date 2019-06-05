package org.redhat.emeapc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/quarkus")
public class GreetingResource {

	private static final java.nio.file.Path LOGFILE = Paths.get("/tmp/access.log");
	
	private static void logRequest() {
		try {
			if ( Files.notExists(LOGFILE, LinkOption.NOFOLLOW_LINKS) )
				Files.createFile(LOGFILE);
			Files.write(LOGFILE, "hello() was invoked\n".getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
    	logRequest();
        return "hello";
    }
}