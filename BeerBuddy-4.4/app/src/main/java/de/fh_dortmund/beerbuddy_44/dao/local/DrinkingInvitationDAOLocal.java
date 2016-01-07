package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 */
public class DrinkingInvitationDAOLocal extends DrinkingInvitationDAO {
    private final DatabaseHelper databaseHelper;

    public DrinkingInvitationDAOLocal(Context context) {
        super(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public void insertOrUpdate(DrinkingInvitation i) throws DataAccessException {
        try {
            databaseHelper.getDrinkingInvitationDao().createOrUpdate(i);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insertOrUpdate DrinkingInvitation",e);
        }
    }

    @Override
    public List<DrinkingInvitation> getAllFor(long personid) throws DataAccessException {
        try {
            return databaseHelper.getDrinkingInvitationDao().query(databaseHelper.getDrinkingInvitationDao().queryBuilder().where().eq("eingeladenerId", personid).prepare());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }


    @Override
    public List<DrinkingInvitation> getAllFrom(long personid) throws DataAccessException {
        try {
            return databaseHelper.getDrinkingInvitationDao().query(databaseHelper.getDrinkingInvitationDao().queryBuilder().where().eq("einladerId", personid).prepare());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }

    @Override
    public void accept(DrinkingInvitation friendInvitation) throws DataAccessException {
        try {
            //Eingeladener joined dem drinking Spot
            DrinkingSpotDAOLocal dao = new DrinkingSpotDAOLocal(context);
            dao.join(friendInvitation.getEingeladenerId(), friendInvitation.getDrinkingSpotId());

            //löschen der Einladung
            databaseHelper.getDrinkingInvitationDao().delete(friendInvitation);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to accept DrinkingInvitation",e);
        }

    }


}
