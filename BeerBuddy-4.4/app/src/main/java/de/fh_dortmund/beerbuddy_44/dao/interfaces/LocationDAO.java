package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;
import android.location.Location;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by David on 26.11.2015.
 */
public abstract class LocationDAO extends DAO{

    public LocationDAO(BeerBuddyActivity context) {
        super(context);
    }

    public abstract Location getCurrentLocation() throws BeerBuddyException;
}
