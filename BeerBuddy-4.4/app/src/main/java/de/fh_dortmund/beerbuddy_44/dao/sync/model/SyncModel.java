package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import java.io.Serializable;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DAO;

/**
 * Created by grimm on 11.01.2016.
 */
public abstract class SyncModel implements Serializable{

    protected long id;
    public SyncModel(long id){
        this.id =id;
    }
    public abstract void performSync(BeerBuddyActivity activity);

}
