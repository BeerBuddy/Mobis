package de.fh_dortmund.beerbuddy.interfaces;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public interface IFriendListDAO {
    boolean isFriendFromId(long personid, long friendid) throws BeerBuddyException;

    FriendList getFriendList(long personid) throws BeerBuddyException;

    void insertOrUpdate(FriendList friendList) throws BeerBuddyException;
}