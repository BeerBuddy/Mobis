package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingSpotUpdateSync extends SyncModel<DrinkingSpot, DrinkingSpotDAORemote>{
    public DrinkingSpotUpdateSync(DrinkingSpot id, DrinkingSpotDAORemote drinkingSpotDAORemote) {
        super(id, drinkingSpotDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity activity) {
        remoteDAO = new DrinkingSpotDAORemote(activity);
        remoteDAO.insertOrUpdate(localEntity, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                activity.getSyncService().addSyncModel(new DrinkingSpotUpdateSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                // Es sind keine weiteren Schritte notwendig!
            }
        });

    }
}
