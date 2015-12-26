package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IFriendListDAO;

/**
 * Created by David on 19.11.2015.
 */
public abstract class FriendListDAO implements IFriendListDAO {

    protected Context context;

    public FriendListDAO(Context context) {
        this.context = context;
    }

    public abstract boolean isFriendFromId(long personid, long firendid) throws BeerBuddyException;

    public abstract FriendList getFriendListId(long personid) throws BeerBuddyException;

}
