package de.fh_dortmund.beerbuddy_44.exceptions;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public class IdNotSetException extends BeerBuddyException {
    public IdNotSetException(String s) {
        super(s);
    }
}
