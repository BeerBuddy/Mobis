package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;

/**
 * Created by grimm on 11.01.2016.
 */
public class DrinkingInvitationDeclineSync extends SyncModel<DrinkingInvitation, DrinkingInvitationDAORemote>{
    public DrinkingInvitationDeclineSync(DrinkingInvitation id, DrinkingInvitationDAORemote drinkingInvitationDAORemote) {
        super(id, drinkingInvitationDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity context) {
        remoteDAO = new DrinkingInvitationDAORemote(context);
        remoteDAO.decline(localEntity, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                context.getSyncService().addSyncModel(new DrinkingInvitationDeclineSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                // Es sind keine weiteren Schritte notwendig!
            }
        });
    }

}
