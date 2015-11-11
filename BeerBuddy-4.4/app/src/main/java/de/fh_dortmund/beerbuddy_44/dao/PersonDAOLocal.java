package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;


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
    public List<Person> getAll() throws SQLException {
        return databaseHelper.getPersonDao().queryForAll();
    }

    @Override
    public Person getById(long id) throws SQLException {
        return databaseHelper.getPersonDao().queryForId(id);
    }

    @Override
    public Person getByEmail(String mail) throws SQLException {
        return databaseHelper.getPersonDao().queryForFirst(databaseHelper.getPersonDao().queryBuilder().where().eq("email", mail).prepare());
    }

    @Override
    public void insertOrUpdate(Person p) throws SQLException {
            databaseHelper.getPersonDao().createOrUpdate(p);
    }

}
