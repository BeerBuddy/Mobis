package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import java.util.List;

import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public abstract class FriendListDAO {

    protected Context context;

    public FriendListDAO(Context context){
        this.context =context;
    }

    public abstract boolean isFriendFromId(long personid, long firendid) throws BeerBuddyException;

    public abstract FriendList getFriendListId(long personid) throws BeerBuddyException;

    public abstract void addFreindRequest(long currentPersonId, Person profil) throws BeerBuddyException;
}
