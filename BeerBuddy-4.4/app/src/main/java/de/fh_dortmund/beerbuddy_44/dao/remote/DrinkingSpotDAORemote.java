package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetActiveForDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetAllDrinkingSpotsRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetByIDDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.JoinDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveDrinkingSpotRequest;

/**
 * Created by David on 19.11.2015.
 */
public class DrinkingSpotDAORemote extends DrinkingSpotDAO {


    public DrinkingSpotDAORemote(Context context) {
        super(context);
    }

    @Override
    public List<DrinkingSpot> getAll() throws DataAccessException {
        try {
            GetAllDrinkingSpotsRequest req = new GetAllDrinkingSpotsRequest();
            DrinkingSpot[] drinkingSpots = req.loadDataFromNetwork();
            return Arrays.asList(drinkingSpots);
        } catch (Exception e) {
            throw new DataAccessException("Failed to get All DrinkingSpot", e);
        }
    }


    @Override
    public DrinkingSpot getActiveByPersonId(long personId) throws DataAccessException {
        try {
            GetActiveForDrinkingSpotRequest req = new GetActiveForDrinkingSpotRequest(personId);
            return req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to getActiveByPersonId DrinkingSpot", e);
        }
    }

    @Override
    public DrinkingSpot insertOrUpdate(DrinkingSpot drinkingSpot) throws DataAccessException {
        try {
            SaveDrinkingSpotRequest req = new SaveDrinkingSpotRequest(drinkingSpot);
            req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to insertOrUpdate DrinkingSpot", e);
        }
        return drinkingSpot;
    }

    @Override
    public DrinkingSpot getById(long dsid) throws DataAccessException {
        try {
            GetByIDDrinkingSpotRequest req = new GetByIDDrinkingSpotRequest(dsid);
            return req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to getActiveByPersonId DrinkingSpot", e);
        }
    }

    @Override
    public void join(long dsid, long personId) throws DataAccessException {
        try {
            JoinDrinkingSpotRequest req = new JoinDrinkingSpotRequest(dsid, personId);
            req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to join DrinkingSpot", e);
        }
    }

    @Override
    public void deactivate(long dsid) throws BeerBuddyException {

    }

}
