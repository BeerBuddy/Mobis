package de.fh_dortmund.beerbuddy_44.dao.sync;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.FriendListDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendListDAORemote;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
public class FriendListDAOSync extends FriendListDAO {


    private final FriendListDAOLocal local;
    private final FriendListDAORemote remote;

    public FriendListDAOSync(BeerBuddyActivity context) {
        super(context);
        this.remote = new FriendListDAORemote(context);
        this.local = new FriendListDAOLocal(context);
    }

    @Override
    public void isFriendFromId(final long personid, final long friendid, final RequestListener<Boolean> listener) {
        remote.isFriendFromId(personid, friendid, new RequestListener<Boolean>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.isFriendFromId(personid, friendid, listener);
            }

            @Override
            public void onRequestSuccess(Boolean aBoolean) {
                listener.onRequestSuccess(aBoolean);
            }
        });
    }

    @Override
    public void getFriendList(final long personid, final RequestListener<FriendList> listener) {
        remote.getFriendList(personid, new RequestListener<FriendList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getFriendList(personid, listener);
            }

            @Override
            public void onRequestSuccess(FriendList friendList) {
                listener.onRequestSuccess(friendList);
                try {
                    local.delete(friendList);
                    local.insert(friendList);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void insertOrUpdate(final FriendList friendList, final RequestListener<FriendList> listener) {
        // NOP
    }
}