package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
public class FriendListDAOLocal extends FriendListDAO {

    private final DatabaseHelper databaseHelper;


    public FriendListDAOLocal(Context context) {
        super(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public boolean isFriendFromId(long personid, long friendid) throws DataAccessException {
        Person p = new Person();
        p.setId(friendid);
        return getFriendList(personid).getFriends().contains(p);
    }

    @Override
    public FriendList getFriendList(long personid) throws DataAccessException {
        try {
            Dao<FriendList, Long> friendListDao = databaseHelper.getFriendListDao();
            return friendListDao.queryBuilder().where().eq("personid", personid).queryForFirst();
        } catch (SQLException e) {
            throw new DataAccessException("Error accured isFriendFromId ", e);
        }
    }

    @Override
    public FriendList insertOrUpdate(FriendList friendList) throws DataAccessException {
        try {
            databaseHelper.getFriendListDao().createOrUpdate(friendList);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert or update friendList",e);
        }
        return friendList;
    }
}
