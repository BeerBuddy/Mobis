package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 */
public class FriendInvitationDAOLocal extends FriendInvitationDAO {
    private final DatabaseHelper databaseHelper;

    public FriendInvitationDAOLocal(Context context) {
        super(context);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public FriendInvitation insertOrUpdate(FriendInvitation i) throws DataAccessException {
        try {
            databaseHelper.getFriendInvitationDAO().createOrUpdate(i);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
        return i;
    }

    @Override
    public  List<FriendInvitation>getAllFor(long personid) throws DataAccessException {
        try {
           return databaseHelper.getFriendInvitationDAO().query(databaseHelper.getFriendInvitationDAO().queryBuilder().where().eq("eingeladenerId", personid).prepare());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }

    @Override
    public List<FriendInvitation> getAllFrom(long personid) throws DataAccessException {
        try {
            return databaseHelper.getFriendInvitationDAO().query(databaseHelper.getFriendInvitationDAO().queryBuilder().where().eq("einladerId", personid).prepare());
        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }

    @Override
    public void accept(FriendInvitation friendInvitation) throws DataAccessException {
        //einladung löschen und freund zur Freundesliste hinzufügen
        try {
            FriendListDAOLocal dao = new FriendListDAOLocal(context);
            PersonDAOLocal dao2 = new PersonDAOLocal(context);

            //Eingeladener FreindList den Einlader hinzufügen
            FriendList list = dao.getFriendList(friendInvitation.getEingeladenerId());
            list.getFriends().add(dao2.getById(friendInvitation.getEinladerId()));
            //und speichern
            dao.insertOrUpdate(list);

            //Einlader FreindList den Eingeladenen hinzufügen
            FriendList list1 = dao.getFriendList(friendInvitation.getEinladerId());
            list1.getFriends().add(dao2.getById(friendInvitation.getEingeladenerId()));
            //und speichern
            dao.insertOrUpdate(list1);

            //löschen der Einladung
            databaseHelper.getFriendInvitationDAO().delete(friendInvitation);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to accept FriendInvitation",e);
        }

    }

    @Override
    public void decline(FriendInvitation invitation) throws BeerBuddyException {

    }


}
