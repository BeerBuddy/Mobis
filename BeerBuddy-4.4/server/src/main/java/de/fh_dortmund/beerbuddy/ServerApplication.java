package de.fh_dortmund.beerbuddy;

import de.fh_dortmund.beerbuddy.health.PersonHealthCheck;
import de.fh_dortmund.beerbuddy.persistence.PersonDAO;
import de.fh_dortmund.beerbuddy.resources.PersonResource;
import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServerApplication extends Application<ServerConfig> {
	
	public static void main(String[] args) throws Exception {
		new ServerApplication().run(args);
	}

	private final HibernateBundle<ServerConfig> hibernateBundle = new HibernateBundle<ServerConfig>(Person.class) {
		@Override
		public PooledDataSourceFactory getDataSourceFactory(ServerConfig configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public void initialize(Bootstrap<ServerConfig> bootstrap) {
		bootstrap.addBundle(new MigrationsBundle<ServerConfig>() {
			@Override
			public PooledDataSourceFactory getDataSourceFactory(ServerConfig configuration) {
				return configuration.getDataSourceFactory();
			}
		});
		bootstrap.addBundle(hibernateBundle);
	}

	@Override
	public String getName() {
		return "#BeerBuddy Server";
	}

	@Override
	public void run(ServerConfig config, Environment env) throws Exception {
		final PersonDAO personDAO = new PersonDAO(hibernateBundle.getSessionFactory());
		env.jersey().register(new PersonResource(personDAO));

		final PersonHealthCheck personHealthCheck = new PersonHealthCheck(config.getVersion());
		env.healthChecks().register("personHealthCheck", personHealthCheck);
	}
}
