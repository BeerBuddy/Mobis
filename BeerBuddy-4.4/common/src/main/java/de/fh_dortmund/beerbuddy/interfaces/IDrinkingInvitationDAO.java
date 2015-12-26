package de.fh_dortmund.beerbuddy.interfaces;


import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 30.11.2015.
 */
public interface IDrinkingInvitationDAO {

    void insertOrUpdate(DrinkingInvitation i);

    List<DrinkingInvitation> getAllFor(long personid) throws BeerBuddyException;

    List<DrinkingInvitation> getAllFrom(long personid) throws BeerBuddyException;

    void accept(DrinkingInvitation friendInvitation) throws BeerBuddyException;
}
