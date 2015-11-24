package de.fh_dortmund.beerbuddy_44.exceptions;

/**
 * Created by David on 19.11.2015.
 */
public class MissingParameterExcetion extends BeerBuddyException {
    public MissingParameterExcetion(String s) {
        super(s);
    }

    public MissingParameterExcetion(String s, Throwable t) {
        super(s,t);
    }
}
