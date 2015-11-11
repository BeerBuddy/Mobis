package de.fh.dortmund.beerbuddy.backend.entity.person;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.fh_dortmund.beerbuddy.Person;

/**
 * Created by seckinger on 14.09.2015.
 */

//TODO secure the Repos look at: https://jaxenter.com/rest-api-spring-java-8-112289.html
public interface PersonRepository extends PagingAndSortingRepository<Person, String> {
}
