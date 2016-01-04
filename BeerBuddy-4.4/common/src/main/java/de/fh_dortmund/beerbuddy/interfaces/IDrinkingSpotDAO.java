package de.fh_dortmund.beerbuddy.interfaces;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by Andreas on 04.01.2016.
 */
public interface IDrinkingSpotDAO {

    List<DrinkingSpot> getAll() throws BeerBuddyException;

    DrinkingSpot getActiveByPersonId(long personId) throws BeerBuddyException;

    void insertOrUpdate(DrinkingSpot drinkingSpot) throws BeerBuddyException;

    DrinkingSpot getById(long dsid) throws BeerBuddyException;

    void join(long dsid, long personId) throws BeerBuddyException;
}
