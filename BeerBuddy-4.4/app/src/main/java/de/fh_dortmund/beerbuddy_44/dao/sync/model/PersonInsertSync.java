package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DAO;

/**
 * Created by grimm on 11.01.2016.
 */
public class PersonInsertSync extends SyncModel{
    public PersonInsertSync(long id) {
        super(id);
    }

    @Override
    public void performSync(BeerBuddyActivity activity) {

    }
}
