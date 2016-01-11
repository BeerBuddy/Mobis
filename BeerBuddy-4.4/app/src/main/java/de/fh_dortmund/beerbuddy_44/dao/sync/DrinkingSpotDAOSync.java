package de.fh_dortmund.beerbuddy_44.dao.sync;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingInvitationDeclineSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingSpotDeactivateSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingSpotInsertSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingSpotJoinSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.DrinkingSpotUpdateSync;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.DeactivateDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetActiveForDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetAllDrinkingSpotsRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetByIDDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.JoinDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveDrinkingSpotRequest;

/**
 * Created by David on 19.11.2015.
 */
public class DrinkingSpotDAOSync extends DrinkingSpotDAO {


    private final DrinkingSpotDAORemote remote;
    private final DrinkingSpotDAOLocal local;

    public DrinkingSpotDAOSync(BeerBuddyActivity context) {
        super(context);
        this.local = new DrinkingSpotDAOLocal(context);
        this.remote = new DrinkingSpotDAORemote(context);
    }

    @Override
    public void getAll(final RequestListener<DrinkingSpot[]> listener) {
        remote.getAll(new RequestListener<DrinkingSpot[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getAll(listener);
            }

            @Override
            public void onRequestSuccess(DrinkingSpot[] drinkingSpots) {
                listener.onRequestSuccess(drinkingSpots);
                for (DrinkingSpot ds : drinkingSpots) {
                    try {
                        local.delete(ds);
                        local.insert(ds);
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override

    public void getActiveByPersonId(final long personId, final RequestListener<DrinkingSpot> listener) {
        remote.getActiveByPersonId(personId, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getActiveByPersonId(personId, listener);
            }

            @Override
            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                listener.onRequestSuccess(drinkingSpot);
                try {
                    local.delete(drinkingSpot);
                    local.insert(drinkingSpot);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void insertOrUpdate(final DrinkingSpot drinkingSpot, final RequestListener<DrinkingSpot> listener) {
        remote.insertOrUpdate(drinkingSpot, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                try {
                    if (drinkingSpot.getId() == 0) {
                        DrinkingSpot insert = local.insert(drinkingSpot);
                        context.getSyncService().addSyncModel(new DrinkingSpotInsertSync(insert.getId()));
                        listener.onRequestSuccess(insert);

                    } else {
                        listener.onRequestSuccess(local.update(drinkingSpot));
                        context.getSyncService().addSyncModel(new DrinkingSpotUpdateSync(drinkingSpot.getId()));
                    }

                } catch (DataAccessException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onRequestSuccess(DrinkingSpot ds) {
                listener.onRequestSuccess(ds);

                try {
                    local.delete(ds);
                    local.insert(ds);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getById(final long dsid, final RequestListener<DrinkingSpot> listener) {
        remote.getById(dsid, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getById(dsid, listener);
            }

            @Override
            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                listener.onRequestSuccess(drinkingSpot);
                try {
                    local.delete(drinkingSpot);
                    local.insert(drinkingSpot);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void join(final long dsid, final long personId, final RequestListener<Void> listener) {
        remote.join(dsid, personId, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.join(dsid, personId, listener);
                context.getSyncService().addSyncModel(new DrinkingSpotJoinSync(dsid, personId));
            }

            @Override
            public void onRequestSuccess(Void drinkingSpot) {
                listener.onRequestSuccess(null);
                try {
                    local.join(dsid, personId);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deactivate(final long dsid, final RequestListener<Void> listener) {
        remote.deactivate(dsid, new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.deactivate(dsid, listener);
                context.getSyncService().addSyncModel(new DrinkingSpotDeactivateSync(dsid));
            }

            @Override
            public void onRequestSuccess(Void drinkingSpot) {
                listener.onRequestSuccess(null);
                try {
                    local.deactivate(dsid);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
