package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingInvitationDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingInvitationInsertSync extends SyncModel<DrinkingInvitation, DrinkingInvitationDAORemote> {
    public DrinkingInvitationInsertSync(DrinkingInvitation id, DrinkingInvitationDAORemote daoRemote) {
        super(id, daoRemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity context) {
        remoteDAO = new DrinkingInvitationDAORemote(context);
        final long oldId = localEntity.getId();
        localEntity.setId(0);
        remoteDAO.insertOrUpdate(localEntity, new RequestListener<DrinkingInvitation>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                context.getSyncService().addSyncModel(new DrinkingInvitationInsertSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(final DrinkingInvitation drinkingInvitation) {
                DrinkingInvitationDAOLocal drinkingInvitationDAOLocal = new DrinkingInvitationDAOLocal(context);
                localEntity.setId(oldId);
                try {
                    drinkingInvitationDAOLocal.delete(localEntity);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
                drinkingInvitationDAOLocal.insertOrUpdate(drinkingInvitation, new RequestListener<DrinkingInvitation>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        context.getSyncService().addSyncModel(new DrinkingInvitationInsertSync(localEntity, remoteDAO));
                    }

                    @Override
                    public void onRequestSuccess(DrinkingInvitation drinkingInvitation) {
                        // Es sind keine weiteren Schritte notwendig!
                    }
                });
            }
        });
    }
}