package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IInvitationDAO;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by David on 30.11.2015.
 */
public abstract class DrinkingInvitationDAO {

    protected BeerBuddyActivity context;

    public DrinkingInvitationDAO(BeerBuddyActivity context) {
        this.context = context;
    }

    public abstract void insertOrUpdate(DrinkingInvitation i, RequestListener<DrinkingInvitation> listener);

    public abstract void getAllFor(long personid, RequestListener<DrinkingInvitation[]> listener);

    public abstract void getAllFrom(long personid, RequestListener<DrinkingInvitation[]> listener);

    public abstract void accept(DrinkingInvitation friendInvitation, RequestListener<Void> listener);

    public abstract void decline(DrinkingInvitation invitation, RequestListener<Void> listener) ;
}
