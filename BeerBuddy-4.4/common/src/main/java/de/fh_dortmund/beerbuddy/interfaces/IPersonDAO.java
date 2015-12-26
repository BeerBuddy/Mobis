package de.fh_dortmund.beerbuddy.interfaces;


import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 09.11.2015.
 */
public interface IPersonDAO {

    List<Person> getAll() throws BeerBuddyException;

    Person getById(long id) throws BeerBuddyException;

    Person getByEmail(String mail) throws BeerBuddyException;

    void insertOrUpdate(Person p) throws BeerBuddyException;
}
