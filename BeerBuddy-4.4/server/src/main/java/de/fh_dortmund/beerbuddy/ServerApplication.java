package de.fh_dortmund.beerbuddy;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.health.PersonHealthCheck;
import de.fh_dortmund.beerbuddy.persistence.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy.persistence.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy.persistence.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy.persistence.FriendListDAO;
import de.fh_dortmund.beerbuddy.persistence.PersonDAO;
import de.fh_dortmund.beerbuddy.resources.DrinkingInvitationResource;
import de.fh_dortmund.beerbuddy.resources.DrinkingSpotResource;
import de.fh_dortmund.beerbuddy.resources.FriendInvitationResource;
import de.fh_dortmund.beerbuddy.resources.FriendListResource;
import de.fh_dortmund.beerbuddy.resources.PersonResource;
import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServerApplication extends Application<ServerConfig> {

    private final HibernateBundle<ServerConfig> hibernateBundle = new HibernateBundle<ServerConfig>(de.fh_dortmund.beerbuddy.entities.Person.class,
            DrinkingInvitation.class,
            FriendInvitation.class,
            FriendList.class,
            DrinkingSpot.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(ServerConfig configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new ServerApplication().run(args);
    }

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

        final FriendListDAO friendListDAO = new FriendListDAO(hibernateBundle.getSessionFactory());
        env.jersey().register(new FriendListResource(friendListDAO));

        final FriendInvitationDAO friendInvitationDAO = new FriendInvitationDAO(hibernateBundle.getSessionFactory(), friendListDAO);
        env.jersey().register(new FriendInvitationResource(friendInvitationDAO));

        final DrinkingSpotDAO drinkingSpotDAO = new DrinkingSpotDAO(hibernateBundle.getSessionFactory(), personDAO);
        env.jersey().register(new DrinkingSpotResource(drinkingSpotDAO));

        final DrinkingInvitationDAO drinkingInvitationDAO = new DrinkingInvitationDAO(hibernateBundle.getSessionFactory(), drinkingSpotDAO);
        env.jersey().register(new DrinkingInvitationResource(drinkingInvitationDAO));

        final PersonHealthCheck personHealthCheck = new PersonHealthCheck(config.getVersion());
        env.healthChecks().register("personHealthCheck", personHealthCheck);
    }
}
