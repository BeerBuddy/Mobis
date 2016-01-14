package de.fh_dortmund.beerbuddy_44.dao.sync;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.FriendInvitationDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.FriendInvitationAcceptSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.FriendInvitationDeclineSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.FriendInvitationInsertSync;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 */
public class FriendInvitationDAOSync extends FriendInvitationDAO {

    private final FriendInvitationDAORemote remote;
    private final FriendInvitationDAOLocal local;

    public FriendInvitationDAOSync(BeerBuddyActivity context) {
        super(context);
        this.remote = new FriendInvitationDAORemote(context);
        this.local = new FriendInvitationDAOLocal(context);
    }

    @Override
    public void insertOrUpdate(final FriendInvitation i, final RequestListener<FriendInvitation> listener) {
        remote.insertOrUpdate(i, new RequestListener<FriendInvitation>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                try {
                    if (i.getId() == 0) {
                        FriendInvitation newInvit = local.insert(i);
                        context.getSyncService().addSyncModel(new FriendInvitationInsertSync(newInvit, remote));
                        listener.onRequestSuccess(newInvit);
                    }
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    listener.onRequestFailure(new SpiceException(e));
                }
            }

            @Override
            public void onRequestSuccess(FriendInvitation friendInvitation) {
                onRequestSuccess(friendInvitation);
                try {
                    local.delete(friendInvitation);
                    local.insert(friendInvitation);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getAllFor(final long personid, final RequestListener<FriendInvitation[]> listener) {
        remote.getAllFor(personid, new RequestListener<FriendInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getAllFor(personid, listener);
            }

            @Override
            public void onRequestSuccess(FriendInvitation[] drinkingInvitations) {
                listener.onRequestSuccess(drinkingInvitations);
                for (FriendInvitation di : drinkingInvitations) {
                    try {
                        local.delete(di);
                        local.insert(di);
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void getAllFrom(final long personid, final RequestListener<FriendInvitation[]> listener) {
        remote.getAllFrom(personid, new RequestListener<FriendInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getAllFrom(personid, listener);
            }

            @Override
            public void onRequestSuccess(FriendInvitation[] friendInvitations) {
                listener.onRequestSuccess(friendInvitations);
                for (FriendInvitation di : friendInvitations) {
                    try {
                        local.delete(di);
                        local.insert(di);
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void accept(final FriendInvitation friendInvitation, final RequestListener<Void> listener) {
        remote.accept(friendInvitation, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                try {
                    local.accept(friendInvitation);
                    context.getSyncService().addSyncModel(new FriendInvitationAcceptSync(friendInvitation, remote));
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    listener.onRequestFailure(new SpiceException(e));
                }
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                listener.onRequestSuccess(null);
                try {
                    local.accept(friendInvitation);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void decline(final FriendInvitation invitation, final RequestListener<Void> listener) {
        remote.decline(invitation, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                try {
                    local.decline(invitation);
                    context.getSyncService().addSyncModel(new FriendInvitationDeclineSync(invitation,remote));
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    listener.onRequestFailure(new SpiceException(e));
                }
            }

            @Override
            public void onRequestSuccess(Void aVoid) {
                listener.onRequestSuccess(null);
                try {
                    local.decline(invitation);
                } catch (DataAccessException e) {
                }
            }
        });
    }


}
