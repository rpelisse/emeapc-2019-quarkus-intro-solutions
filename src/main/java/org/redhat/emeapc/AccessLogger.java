package org.redhat.emeapc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessLogger {

	
	private static final Path LOGFILE = Paths.get("/tmp/access.log");
	
	@PostConstruct
	public void createAccessLogfileIfNeeded() throws IOException {
		if ( Files.notExists(LOGFILE, LinkOption.NOFOLLOW_LINKS) )
			Files.createFile(LOGFILE);		
	}

	@PreDestroy
	public void closeLogfile() {
		// Nothing to do
	}
	
	public void logRequest() {
		try {
			Files.write(LOGFILE, "hello() was invoked\n".getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
