package de.fh_dortmund.beerbuddy_44.exceptions;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public class CouldNotParseDateException extends BeerBuddyException {
    public CouldNotParseDateException(String s) {
        super(s);
    }

    public CouldNotParseDateException(String msg, Throwable t)
    {
        super(msg,t);
    }
}
