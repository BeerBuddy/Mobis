package de.fh.dortmund.beerbuddy.backend.entity.person;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by seckinger on 14.09.2015.
 */
public interface PersonRepository extends PagingAndSortingRepository<Person, String> {
}
