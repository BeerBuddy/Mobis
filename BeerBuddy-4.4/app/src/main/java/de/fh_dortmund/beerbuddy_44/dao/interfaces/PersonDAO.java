package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 09.11.2015.
 */
public abstract class PersonDAO {

    protected Context context;

    public PersonDAO(Context context){
        this.context =context;
    }

    public abstract List<Person> getAll() throws BeerBuddyException;
    public abstract Person getById(long id) throws BeerBuddyException;
    public abstract Person getByEmail(String mail) throws BeerBuddyException;
    public abstract void insertOrUpdate(Person p) throws BeerBuddyException;
}
