package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingSpotDeactivateSync extends SyncModel<DrinkingSpot, DrinkingSpotDAORemote> {
    public DrinkingSpotDeactivateSync(DrinkingSpot dsid, DrinkingSpotDAORemote drinkingSpotDAORemote) {
        super(dsid, drinkingSpotDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity activity) {
        remoteDAO = new DrinkingSpotDAORemote(activity);
        remoteDAO.deactivate(localEntity.getId(), new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                activity.getSyncService().addSyncModel(new DrinkingSpotDeactivateSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                DrinkingSpotDAOLocal drinkingSpotDAOLocal = new DrinkingSpotDAOLocal(activity);
                drinkingSpotDAOLocal.deactivate(localEntity.getId(), new RequestListener<Void>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        activity.getSyncService().addSyncModel(new DrinkingSpotDeactivateSync(localEntity, remoteDAO));
                    }

                    @Override
                    public void onRequestSuccess(Void aVoid) {
                        // Es sind keine weiteren Schritte notwendig!
                    }
                });
            }
        });
    }
}
