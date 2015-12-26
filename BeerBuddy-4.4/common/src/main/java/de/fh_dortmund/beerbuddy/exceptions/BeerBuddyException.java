package de.fh_dortmund.beerbuddy.exceptions;

/**
 * Created by David on 19.11.2015.
 */
public abstract class BeerBuddyException extends Exception{

    public BeerBuddyException(String msg)
    {
        super(msg);
    }

    public BeerBuddyException(String msg, Throwable t)
    {
        super(msg,t);
    }
}
