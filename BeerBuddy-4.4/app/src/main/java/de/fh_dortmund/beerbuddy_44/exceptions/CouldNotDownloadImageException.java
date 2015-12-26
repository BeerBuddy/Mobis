package de.fh_dortmund.beerbuddy_44.exceptions;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public class CouldNotDownloadImageException extends BeerBuddyException {
    public CouldNotDownloadImageException(String msg) {
        super(msg);
    }

    public CouldNotDownloadImageException(String msg, Throwable t)
    {
        super(msg,t);
    }
}
