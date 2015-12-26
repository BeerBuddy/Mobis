package de.fh_dortmund.beerbuddy.interfaces;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 30.11.2015.
 */
public interface IFriendInvitationDAO {

    void insertOrUpdate(FriendInvitation i);

    List<FriendInvitation> getAllFor(long personid) throws BeerBuddyException;

    List<FriendInvitation> getAllFrom(long personid) throws BeerBuddyException;

    void accept(FriendInvitation friendInvitation) throws BeerBuddyException;
}
