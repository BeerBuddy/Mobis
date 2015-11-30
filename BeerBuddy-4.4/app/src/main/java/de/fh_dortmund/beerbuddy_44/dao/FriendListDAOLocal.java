package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
class FriendListDAOLocal extends FriendListDAO {

    private final DatabaseHelper databaseHelper;


    public FriendListDAOLocal(Context context) {
        super(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public boolean isFriendFromId(long personid, long firendid) throws DataAccessException {
        Person p = new Person();
        p.setId(firendid);
        return getFriendListId(personid).getFriends().contains(p);
    }

    @Override
    public FriendList getFriendListId(long personid) throws DataAccessException {
        try {
            Dao<FriendList, Long> freindListDao = databaseHelper.getFreindListDao();
            return freindListDao.queryBuilder().where().eq("personid", personid).queryForFirst();
        } catch (SQLException e) {
            throw new DataAccessException("Error accured isFriendFromId ", e);
        }
    }


}
