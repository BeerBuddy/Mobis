package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import java.util.List;

import de.fh_dortmund.beerbuddy.FriendInvitation;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 30.11.2015.
 */
public abstract class FriendInvitationDAO {

    protected Context context;

    public FriendInvitationDAO(Context context){
        this.context =context;
    }

    public abstract void insertOrUpdate(FriendInvitation i);

    public abstract List<FriendInvitation> getAllFor(long personid) throws BeerBuddyException;

    public abstract List<FriendInvitation> getAllFrom(long personid) throws BeerBuddyException;
}
