package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IInvitationDAO;

/**
 * Created by David on 30.11.2015.
 */
public abstract class DrinkingInvitationDAO implements IInvitationDAO<DrinkingInvitation> {

    protected Context context;

    public DrinkingInvitationDAO(Context context) {
        this.context = context;
    }

    public abstract void insertOrUpdate(DrinkingInvitation i);

    public abstract List<DrinkingInvitation> getAllFor(long personid) throws BeerBuddyException;

    public abstract List<DrinkingInvitation> getAllFrom(long personid) throws BeerBuddyException;

    public abstract void accept(DrinkingInvitation friendInvitation) throws BeerBuddyException;
}
