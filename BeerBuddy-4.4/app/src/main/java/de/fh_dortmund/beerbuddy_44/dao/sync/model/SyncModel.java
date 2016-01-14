package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import java.io.Serializable;

import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DAO;

/**
 * Created by grimm on 11.01.2016.
 */
public abstract class SyncModel<E, T extends DAO> implements Serializable {

    protected E localEntity;
    protected transient T remoteDAO;

    public SyncModel(E localEntity, T remoteDAO) {
        this.localEntity = localEntity;
        this.remoteDAO = remoteDAO;
    }

    /**
     * Method to perform a sync on a Sync Model
     */
    public abstract void performSync( BeerBuddyActivity activity );

}
