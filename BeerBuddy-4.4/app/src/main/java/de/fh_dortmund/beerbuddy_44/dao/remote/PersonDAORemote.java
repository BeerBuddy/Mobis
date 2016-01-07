package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetByEmailPersonRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetByIDPersonRequest;
import de.fh_dortmund.beerbuddy_44.requests.SavePersonRequest;

/**
 * Created by David on 09.11.2015.
 */
public class PersonDAORemote extends PersonDAO {

    public PersonDAORemote(Context context) {
        super(context);
    }

    @Override
    public List<Person> getAll() throws DataAccessException {
        try {
            GetAllPersonsRequest getAllPersonsRequest = new GetAllPersonsRequest();
            Person[] persons = getAllPersonsRequest.loadDataFromNetwork();
            return Arrays.asList(persons);
        } catch (Exception e) {
            throw new DataAccessException("Failed to get All Persons", e);
        }
    }

    @Override
    public Person getById(long id) throws DataAccessException {
        try {
            GetByIDPersonRequest req = new GetByIDPersonRequest(id);
            return req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to getById Person", e);
        }
    }

    @Override
    public Person getByEmail(String mail) throws DataAccessException {
        try {
            GetByEmailPersonRequest req = new GetByEmailPersonRequest(mail);
            return req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to getByEmail Person", e);
        }
    }

    @Override
    public Person insertOrUpdate(Person p) throws DataAccessException {
        try {
                SavePersonRequest req = new SavePersonRequest(p);
                req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update Person", e);
        }
        return p;
    }
}
