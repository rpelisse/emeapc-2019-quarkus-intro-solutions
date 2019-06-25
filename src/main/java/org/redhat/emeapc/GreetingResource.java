package org.redhat.emeapc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.agroal.api.AgroalDataSource;
import io.quarkus.panache.common.Sort;

@Path("/quarkus")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());

    private static final Map<String,Country> cacheCountries = new HashMap<String,Country>();

    @Inject
    @RestClient
    CountriesService countriesService;

    @Inject
    Validator validator;

    @Inject
    AgroalDataSource defaultDataSource;

    @Inject 
    UserTransaction transaction;

    
    @GET
    @Path("/country/{name}")
    @RolesAllowed("user")
    public String getCountry(@PathParam("name") String name) throws SQLException {
        Country c = Country.findByName(name);
        if ( c == null ) {
            LOGGER.warning("No country " + name + " found in local database. Invoking remote service...");
            c = lookupAndAddMissingCountry(name);
            LOGGER.warning("... Query result:" + c);
        }
        return c.getAlpha2Code().toLowerCase();
    }

    private Country lookupAndAddMissingCountry(String name) {
    	final Country c = invokedRemoteService(name);
    	LOGGER.warning("... Query result:" + c);
    	if ( c != null ) {
    		LOGGER.warning(c.getAlpha2Code() + ":" + c.getName());
            try {
                transaction.begin();
	    		c.persist();
	            transaction.commit();
	        }
	        catch(Exception e) {
	        	throw new IllegalStateException(e);
	        }
    		LOGGER.warning("Saved under id:" + c.id);
    		LOGGER.warning("Persistent:" + c.isPersistent());
    	} else
    		new IllegalArgumentException("No such country:" + name);
    	return c;
    }

    @GET
    @Path("/dscheck")
    @RolesAllowed("admin")
    public String checkDS() throws SQLException {
		List<Country> countries = Country.listAll(Sort.by("name"));
		LOGGER.warning("FOUND:" + countries.size());
		String result = "[";
		for ( Country c : countries )
			result += c.getAlpha2Code() + ",";
		result += "]";
		return result.replaceAll("\\,\\]", "]");
    }

	private Country invokedRemoteService(String countryName) {
		Set<Country> res = countriesService.getByName(countryName);
		if ( res == null || res.isEmpty() )
			throw new IllegalArgumentException("No such country :" + countryName);
		return cacheCountry(countryName, res.iterator().next());
	}

	private Country cacheCountry(String countryName, Country country) {
		cacheCountries.put(countryName, country);
		return country;
	}
}
