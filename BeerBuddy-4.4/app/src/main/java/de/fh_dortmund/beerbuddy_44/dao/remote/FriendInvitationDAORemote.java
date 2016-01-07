package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetAllForFriendInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetAllFromFriendInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveFriendInvitationRequest;

/**
 * Created by David on 30.11.2015.
 */
public  class FriendInvitationDAORemote extends FriendInvitationDAO {

    public FriendInvitationDAORemote(Context context) {
        super(context);
    }

    @Override
    public FriendInvitation insertOrUpdate(FriendInvitation i) throws DataAccessException {
        try {
            SaveFriendInvitationRequest req = new SaveFriendInvitationRequest(i);
            req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
        return i;
    }

    @Override
    public  List<FriendInvitation>getAllFor(long personid) throws DataAccessException {
        try {
            GetAllForFriendInvitationRequest req = new GetAllForFriendInvitationRequest(personid);
            FriendInvitation[] friendInvitations = req.loadDataFromNetwork();
            return Arrays.asList(friendInvitations);
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }

    @Override
    public List<FriendInvitation> getAllFrom(long personid) throws DataAccessException {
        try {
            GetAllFromFriendInvitationRequest req = new GetAllFromFriendInvitationRequest(personid);
            FriendInvitation[] friendInvitations = req.loadDataFromNetwork();
            return Arrays.asList(friendInvitations);
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }

    @Override
    public void accept(FriendInvitation friendInvitation) throws DataAccessException {
        //einladung löschen und freund zur Freundesliste hinzufügen
        try {
            FriendListDAORemote dao = new FriendListDAORemote(context);
            PersonDAORemote dao2 = new PersonDAORemote(context);

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

            //FIXME wirklich löschen
            //löschen der Einladung
            friendInvitation.setEingeladenerId(0l);
            friendInvitation.setEinladerId(0l);
            SaveFriendInvitationRequest req = new SaveFriendInvitationRequest(friendInvitation);
            req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to accept FriendInvitation",e);
        }

    }

    @Override
    public void decline(FriendInvitation invitation) throws BeerBuddyException {

    }


}
