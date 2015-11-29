package de.fh_dortmund.beerbuddy;

import de.fh_dortmund.beerbuddy.health.PersonHealthCheck;
import de.fh_dortmund.beerbuddy.resources.PersonService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServerApplication extends Application<ServerConfig> {
	
	public static void main(String[] args) throws Exception {
		new ServerApplication().run(args);
	}
	
	@Override
	public String getName() {
		return "#BeerBuddy Server";
	}

	@Override
	public void run(ServerConfig config, Environment env) throws Exception {
		final PersonService personService = new PersonService();
		env.jersey().register(personService);

		final PersonHealthCheck personHealthCheck = new PersonHealthCheck(config.getVersion());
		env.healthChecks().register("template", personHealthCheck);
		env.jersey().register(personHealthCheck);
	}

}
