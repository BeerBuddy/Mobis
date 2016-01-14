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
public class FriendInvitationInsertSync extends SyncModel<FriendInvitation, FriendInvitationDAORemote> {
    public FriendInvitationInsertSync(FriendInvitation id, FriendInvitationDAORemote friendInvitationDAORemote) {
        super(id, friendInvitationDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity activity) {
        remoteDAO = new FriendInvitationDAORemote(activity);
        final long oldId = localEntity.getId();
        localEntity.setId(0);
        remoteDAO.insertOrUpdate(localEntity, new RequestListener<FriendInvitation>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                activity.getSyncService().addSyncModel(new FriendInvitationInsertSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(FriendInvitation friendInvitation) {
                localEntity.setId(oldId);
                FriendInvitationDAOLocal friendInvitationDAOLocal = new FriendInvitationDAOLocal(activity);
                try {
                    friendInvitationDAOLocal.delete(localEntity);
                    friendInvitationDAOLocal.insert(friendInvitation);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
