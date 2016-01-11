package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;
import android.location.Location;

import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IDrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 26.11.2015.
 */
public abstract class DrinkingSpotDAO extends DAO {


    public DrinkingSpotDAO(BeerBuddyActivity context) {
        super(context);
    }

    public abstract void getAll(RequestListener<DrinkingSpot[]> listener);

    public abstract void getActiveByPersonId(long personId, RequestListener<DrinkingSpot> listener);

    public abstract void insertOrUpdate(DrinkingSpot drinkingSpot, RequestListener<DrinkingSpot> listener);

    public abstract void getById(long dsid, RequestListener<DrinkingSpot> listener);

    public abstract void join(long dsid, long personId, RequestListener<Void> listener);

    public abstract void deactivate(long dsid, RequestListener<Void> listener);
}
