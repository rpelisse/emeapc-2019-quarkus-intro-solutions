package org.redhat.emeapc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.agroal.api.AgroalDataSource;

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

    @GET
    @Path("/dscheck")
    public String checkDS() throws SQLException {
    	return defaultDataSource.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/country/{countryName}")
    public Country retrieveCountryByName(@PathParam("countryName") String countryName ) {
        LOGGER.warning("retrieveCountryByName was invoked.");
    	return cacheCountries.containsKey(countryName) ? cacheCountries.get(countryName) : invokedRemoteService(countryName);
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
