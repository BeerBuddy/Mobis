package de.fh.dortmund.beerbuddy.backend;

import org.springframework.boot.autoconfigure.data.rest.SpringBootRepositoryRestMvcConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import de.fh_dortmund.beerbuddy.Person;

/**
 * Created by seckinger on 04.09.2015.
 */
@Configuration
public class InfosysRestConfig extends SpringBootRepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Person.class);
    }

}
