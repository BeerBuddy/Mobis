package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;


/**
 * Created by David on 09.11.2015.
 */
class PersonDAOLocal extends PersonDAO {

    private final  DatabaseHelper databaseHelper;


    public PersonDAOLocal(Context context) {
        super(context);
        this.databaseHelper = new DatabaseHelper(context);
    }


    @Override
    public List<Person> getAll() throws BeerBuddyException {
        try {
            return databaseHelper.getPersonDao().queryForAll();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get All Persons",e);
        }
    }

    @Override
    public Person getById(long id) throws BeerBuddyException {
        try {
            return databaseHelper.getPersonDao().queryForId(id);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get Person by Id",e);
        }
    }

    @Override
    public Person getByEmail(String mail) throws BeerBuddyException {
        try {
            return databaseHelper.getPersonDao().queryForFirst(databaseHelper.getPersonDao().queryBuilder().where().eq("email", mail).prepare());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get Person by email",e);
        }
    }

    @Override
    public void insertOrUpdate(Person p) throws BeerBuddyException {
        try {
            databaseHelper.getPersonDao().createOrUpdate(p);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert or update Person",e);
        }
    }


}
