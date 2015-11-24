package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public abstract class CurrentPersonDAO {

    protected Context context;

    public CurrentPersonDAO(Context context){
        this.context =context;
    }


    /**
     * Gets the currentPerson id the currentPerson is the Person who is logged in.
     * @return the logged in person id
     * @throws BeerBuddyException
     */
    public abstract long getCurrentPersonId() throws BeerBuddyException;

    /**
     * Sets a new currentPerson id and overrides the one who was logged in last
     * @param person the new currentPerson id
     * @throws BeerBuddyException
     */
    public abstract void insertCurrentPersonId(long person) throws BeerBuddyException;

    /**
     * Deletes the currentPerson id
     * @throws BeerBuddyException
     */
    public abstract void deleteCurrentPerson() throws BeerBuddyException;
}
