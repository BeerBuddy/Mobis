package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;
import android.location.Location;

import java.util.List;

import de.fh_dortmund.beerbuddy.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 26.11.2015.
 */
public abstract class DrinkingSpotDAO {

    protected Context context;

    public DrinkingSpotDAO(Context context){
        this.context =context;
    }

    public abstract List<DrinkingSpot> getAll(Location l) throws BeerBuddyException;

    public abstract DrinkingSpot getActiveByPersonId(long currentPersonId) throws BeerBuddyException;

    public abstract void insertOrUpdate(DrinkingSpot drinkingSpot)throws BeerBuddyException;

    public abstract DrinkingSpot getById(long dsid) throws BeerBuddyException;

    public abstract void join(long dsid, long currentPersonId) throws BeerBuddyException;
}
