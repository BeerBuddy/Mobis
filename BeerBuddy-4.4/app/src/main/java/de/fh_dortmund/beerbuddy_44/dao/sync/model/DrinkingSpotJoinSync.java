package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingSpotJoinSync extends SyncModel{
    private final long personid;

    public DrinkingSpotJoinSync(long id, long personid) {
        super(id);
        this.personid = personid;
    }

    @Override
    public void performSync(BeerBuddyActivity activity) {

    }
}
