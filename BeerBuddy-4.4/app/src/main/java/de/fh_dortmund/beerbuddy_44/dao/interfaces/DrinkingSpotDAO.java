package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;
import android.location.Location;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IDrinkingSpotDAO;

/**
 * Created by David on 26.11.2015.
 */
public abstract class DrinkingSpotDAO implements IDrinkingSpotDAO {

    protected Context context;

    public DrinkingSpotDAO(Context context) {
        this.context = context;
    }

}
