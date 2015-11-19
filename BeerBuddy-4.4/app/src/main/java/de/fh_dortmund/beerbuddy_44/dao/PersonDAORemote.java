package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy.PersonList;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;
import de.fh_dortmund.beerbuddy_44.requests.InsertPersonRequest;
import de.fh_dortmund.beerbuddy_44.requests.UpdatePersonRequest;

/**
 * Created by David on 09.11.2015.
 */
class PersonDAORemote extends PersonDAO {

    public PersonDAORemote(Context context) {
        super(context);
    }

    @Override
    public List<Person> getAll() throws BeerBuddyException {
        try {
            GetAllPersonsRequest getAllPersonsRequest = new GetAllPersonsRequest();
            PersonList persons = getAllPersonsRequest.loadDataFromNetwork();
            return persons;
        } catch (Exception e) {
            throw new DataAccessException("Failed to get All Persons",e);
        }
    }

    @Override
    public Person getById(long id) {
        throw new NotImplementedException("");
    }

    @Override
    public Person getByEmail(String mail) {
        throw new NotImplementedException("");
    }

    @Override
    public void insertOrUpdate(Person p) throws BeerBuddyException {
        try {
            if (p.getId() == 0) {
                InsertPersonRequest req = new InsertPersonRequest(p);
                req.loadDataFromNetwork();
            } else {
                UpdatePersonRequest req = new UpdatePersonRequest(p);
                req.loadDataFromNetwork();
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update Person",e);
        }
    }
}
