package org.redhat.emeapc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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

    @GET
    @Path("/dscheck")
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
