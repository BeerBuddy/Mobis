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
}
