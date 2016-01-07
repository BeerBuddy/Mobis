package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetFriendListRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetIsFriendRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveFriendListRequest;

/**
 * Created by David on 19.11.2015.
 */
public  class FriendListDAORemote extends FriendListDAO {

    public FriendListDAORemote(Context context) {
        super(context);
    }

    @Override
    public boolean isFriendFromId(long personid, long friendid) throws DataAccessException {

        try {
            GetIsFriendRequest req = new GetIsFriendRequest(personid, friendid);
            return req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed get isFriendFromId ", e);
        }
    }

    @Override
    public FriendList getFriendList(long personid) throws DataAccessException {
        try {
            GetFriendListRequest req = new GetFriendListRequest(personid);
            return req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed getFriendList ", e);
        }
    }

    @Override
    public FriendList insertOrUpdate(FriendList friendList) throws DataAccessException {
        try {
            SaveFriendListRequest req = new SaveFriendListRequest(friendList);
            req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed insertOrUpdate FriendList ", e);
        }
        return friendList;
    }
}
