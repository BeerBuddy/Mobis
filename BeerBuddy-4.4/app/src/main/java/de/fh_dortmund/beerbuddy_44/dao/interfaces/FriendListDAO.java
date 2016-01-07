package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IFriendListDAO;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by David on 19.11.2015.
 */
public abstract class FriendListDAO {

    protected BeerBuddyActivity context;

    public FriendListDAO(BeerBuddyActivity context) {
        this.context = context;
    }

    public abstract void isFriendFromId(long personid, long friendid, RequestListener<Boolean> listener);

    public abstract void getFriendList(long personid, RequestListener<FriendList> listener);

    public abstract void insertOrUpdate(FriendList friendList, RequestListener<FriendList> listener);
}
