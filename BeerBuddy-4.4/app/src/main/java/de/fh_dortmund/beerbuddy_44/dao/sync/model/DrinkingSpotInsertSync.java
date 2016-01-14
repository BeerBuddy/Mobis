package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingSpotInsertSync extends SyncModel<DrinkingSpot, DrinkingSpotDAORemote>{
    public DrinkingSpotInsertSync(DrinkingSpot id, DrinkingSpotDAORemote drinkingSpotDAORemote) {
        super(id, drinkingSpotDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity activity) {
        remoteDAO = new DrinkingSpotDAORemote(activity);
        final long oldId = localEntity.getId();
        localEntity.setId(0);
        remoteDAO.insertOrUpdate(localEntity, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                activity.getSyncService().addSyncModel(new DrinkingSpotInsertSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                DrinkingSpotDAOLocal drinkingSpotDAOLocal = new DrinkingSpotDAOLocal(activity);
                localEntity.setId(oldId);
                try {
                    drinkingSpotDAOLocal.delete(localEntity);
                    drinkingSpotDAOLocal.insert(drinkingSpot);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
