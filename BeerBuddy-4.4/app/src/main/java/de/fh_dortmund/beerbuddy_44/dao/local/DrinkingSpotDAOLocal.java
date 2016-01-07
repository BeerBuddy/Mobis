package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
public class DrinkingSpotDAOLocal extends DrinkingSpotDAO {

    private final DatabaseHelper databaseHelper;

    public DrinkingSpotDAOLocal(Context context) {
        super(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public List<DrinkingSpot> getAll() throws DataAccessException {
        try {
            return  databaseHelper.getDrinkingSpotDao().queryForAll();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get All DrinkingSpot",e);
        }
    }


    @Override
    public DrinkingSpot getActiveByPersonId(long personId) throws DataAccessException {
        try {
            return  databaseHelper.getDrinkingSpotDao().queryForFirst(databaseHelper.getDrinkingSpotDao().queryBuilder().where().eq("creator.id",personId).and().eq("active",false).prepare());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to getActiveByPersonId DrinkingSpot",e);
        }
    }

    @Override
    public DrinkingSpot insertOrUpdate(DrinkingSpot drinkingSpot) throws DataAccessException {
        try {
            databaseHelper.getDrinkingSpotDao().createOrUpdate(drinkingSpot);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insertOrUpdate DrinkingSpot",e);
        }
        return drinkingSpot;
    }

    @Override
    public DrinkingSpot getById(long dsid) throws DataAccessException {
        try {
            return  databaseHelper.getDrinkingSpotDao().queryForId(dsid);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to getActiveByPersonId DrinkingSpot",e);
        }
    }

    @Override
    public void join(long dsid, long personId) throws DataAccessException {
            DrinkingSpot drinkingSpot = getById(dsid);
            drinkingSpot.getPersons().add(new PersonDAOLocal(context).getById(personId));
            insertOrUpdate(drinkingSpot);
    }

    @Override
    public void deactivate(long dsid) throws BeerBuddyException {

    }

}
