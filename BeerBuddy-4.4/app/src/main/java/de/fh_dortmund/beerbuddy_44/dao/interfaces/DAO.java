package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by grimm on 11.01.2016.
 */
public abstract class DAO {

    protected BeerBuddyActivity context;

    public DAO(BeerBuddyActivity context) {
        this.context = context;
    }
}
