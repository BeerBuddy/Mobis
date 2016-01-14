package de.fh_dortmund.beerbuddy_44.dao.sync;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingInvitationDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingInvitationAcceptSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingInvitationDeclineSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingInvitationInsertSync;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 */
public class DrinkingInvitationDAOSync extends DrinkingInvitationDAO {

    private final DrinkingInvitationDAORemote remote;
    private final DrinkingInvitationDAOLocal local;

    public DrinkingInvitationDAOSync(BeerBuddyActivity context) {
        super(context);
        this.remote = new DrinkingInvitationDAORemote(context);
        this.local = new DrinkingInvitationDAOLocal(context);
    }

    @Override
    public void insertOrUpdate(final DrinkingInvitation i, final RequestListener<DrinkingInvitation> listener) {
        final DrinkingInvitationDAOSync that = this;
        remote.insertOrUpdate(i, new RequestListener<DrinkingInvitation>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                try {
                    if (i.getId() == 0) {
                        DrinkingInvitation newInvit = local.insert(i);
                        context.getSyncService().addSyncModel(new DrinkingInvitationInsertSync(newInvit, remote));
                        listener.onRequestSuccess(newInvit);
                    }
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    listener.onRequestFailure(new SpiceException(e));
                }
            }

            @Override
            public void onRequestSuccess(DrinkingInvitation drinkingInvitation) {
                listener.onRequestSuccess(drinkingInvitation);
                try {
                    local.delete(drinkingInvitation);
                    local.insert(drinkingInvitation);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getAllFor(final long personid, final RequestListener<DrinkingInvitation[]> listener) {
        remote.getAllFor(personid, new RequestListener<DrinkingInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getAllFor(personid, listener);
            }

            @Override
            public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                listener.onRequestSuccess(drinkingInvitations);
                for (DrinkingInvitation di : drinkingInvitations) {
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
    public void getAllFrom(final long personid, final RequestListener<DrinkingInvitation[]> listener) {
        remote.getAllFrom(personid, new RequestListener<DrinkingInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getAllFrom(personid, listener);
            }

            @Override
            public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                listener.onRequestSuccess(drinkingInvitations);
                for (DrinkingInvitation di : drinkingInvitations) {
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
    public void accept(final DrinkingInvitation friendInvitation, final RequestListener<Void> listener) {
        remote.accept(friendInvitation, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                try {
                    local.accept(friendInvitation);
                    context.getSyncService().addSyncModel(new DrinkingInvitationAcceptSync(friendInvitation, remote));
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
    public void decline(final DrinkingInvitation invitation, final RequestListener<Void> listener) {
        remote.decline(invitation, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                try {
                    local.decline(invitation);
                    context.getSyncService().addSyncModel(new DrinkingInvitationDeclineSync(invitation, remote));
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
