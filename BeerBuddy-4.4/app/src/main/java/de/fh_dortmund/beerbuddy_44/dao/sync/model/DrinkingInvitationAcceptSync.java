package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingInvitationAcceptSync extends SyncModel<DrinkingInvitation, DrinkingInvitationDAORemote> {
    public DrinkingInvitationAcceptSync(DrinkingInvitation id, DrinkingInvitationDAORemote drinkingInvitationDAORemote) {
        super(id, drinkingInvitationDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity context) {
        remoteDAO = new DrinkingInvitationDAORemote(context);
        remoteDAO.accept(localEntity, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                context.getSyncService().addSyncModel(new DrinkingInvitationAcceptSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                DrinkingSpotDAORemote drinkingSpotDAORemote = new DrinkingSpotDAORemote(context);
                final DrinkingSpotDAOLocal drinkingSpotDAOLocal = new DrinkingSpotDAOLocal(context);
                drinkingSpotDAORemote.getById(localEntity.getDrinkingSpotId(), new RequestListener<DrinkingSpot>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        context.getSyncService().addSyncModel(new DrinkingInvitationAcceptSync(localEntity, remoteDAO));
                    }

                    @Override
                    public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                        try {
                            // sollten sich z.b. personen ode rä
                            drinkingSpotDAOLocal.update(drinkingSpot);
                        } catch (DataAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
