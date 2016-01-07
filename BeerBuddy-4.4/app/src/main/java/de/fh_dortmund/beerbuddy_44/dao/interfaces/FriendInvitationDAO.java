package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IInvitationDAO;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by David on 30.11.2015.
 */
public abstract class FriendInvitationDAO {

    protected BeerBuddyActivity context;

    public FriendInvitationDAO(BeerBuddyActivity context) {
        this.context = context;
    }

    public abstract void insertOrUpdate(FriendInvitation i, RequestListener<FriendInvitation> listener);

    public abstract void getAllFor(long personid, RequestListener<FriendInvitation[]> listener);

    public abstract void getAllFrom(long personid, RequestListener<FriendInvitation[]> listener);

    public abstract void accept(FriendInvitation friendInvitation, RequestListener<Void> listener);

    public abstract void decline(FriendInvitation invitation, RequestListener<Void> listener);
}
