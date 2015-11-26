package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;
import android.location.Location;

import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 26.11.2015.
 */
public abstract class LocationDAO {

    protected Context context;

    public LocationDAO(Context context){
        this.context =context;
    }

    public abstract Location getCurrentLocation() throws BeerBuddyException;
}
