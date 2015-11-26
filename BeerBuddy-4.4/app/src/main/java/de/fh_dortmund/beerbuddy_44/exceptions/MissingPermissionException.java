package de.fh_dortmund.beerbuddy_44.exceptions;

/**
 * Created by David on 26.11.2015.
 */
public class MissingPermissionException  extends BeerBuddyException{
    public MissingPermissionException(String msg) {
        super(msg);
    }

    public MissingPermissionException(String msg, Throwable t)
    {
        super(msg,t);
    }
}

