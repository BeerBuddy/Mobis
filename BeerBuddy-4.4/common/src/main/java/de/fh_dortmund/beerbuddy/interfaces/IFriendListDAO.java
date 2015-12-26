package de.fh_dortmund.beerbuddy.interfaces;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public interface IFriendListDAO {
    boolean isFriendFromId(long personid, long firendid) throws BeerBuddyException;

    FriendList getFriendListId(long personid) throws BeerBuddyException;
}
