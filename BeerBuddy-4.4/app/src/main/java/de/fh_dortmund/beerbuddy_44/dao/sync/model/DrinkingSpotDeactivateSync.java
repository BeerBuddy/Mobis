package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingSpotDeactivateSync extends SyncModel {
    public DrinkingSpotDeactivateSync(long dsid) {
        super(dsid);
    }

    @Override
    public void performSync(BeerBuddyActivity activity) {

    }
}
