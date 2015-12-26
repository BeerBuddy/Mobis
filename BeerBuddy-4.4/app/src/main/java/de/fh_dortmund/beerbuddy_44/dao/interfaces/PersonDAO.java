package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IPersonDAO;

/**
 * Created by David on 09.11.2015.
 */
public abstract class PersonDAO implements IPersonDAO {

    protected Context context;

    public PersonDAO(Context context) {
        this.context = context;
    }

    public abstract List<Person> getAll() throws BeerBuddyException;

    public abstract Person getById(long id) throws BeerBuddyException;

    public abstract Person getByEmail(String mail) throws BeerBuddyException;

    public abstract void insertOrUpdate(Person p) throws BeerBuddyException;
}
