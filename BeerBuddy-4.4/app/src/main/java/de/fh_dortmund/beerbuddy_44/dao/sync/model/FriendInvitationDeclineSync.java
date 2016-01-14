package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.FriendInvitationDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by grimm on 11.01.2016.
 */
public class FriendInvitationDeclineSync extends SyncModel<FriendInvitation, FriendInvitationDAORemote> {
    public FriendInvitationDeclineSync(FriendInvitation id, FriendInvitationDAORemote friendInvitationDAORemote) {
        super(id, friendInvitationDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity activity) {
        remoteDAO = new FriendInvitationDAORemote(activity);
        remoteDAO.decline(localEntity, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                activity.getSyncService().addSyncModel(new FriendInvitationDeclineSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                FriendInvitationDAOLocal friendInvitationDAOLocal = new FriendInvitationDAOLocal(activity);
                try {
                    friendInvitationDAOLocal.decline(localEntity);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
