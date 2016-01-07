package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetAllForDrinkingInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetAllFromDrinkingInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.JoinDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveDrinkingInvitationRequest;

/**
 * Created by David on 30.11.2015.
 */
public class DrinkingInvitationDAORemote extends DrinkingInvitationDAO {

    public DrinkingInvitationDAORemote(Context context) {
        super(context);
    }

    @Override
    public DrinkingInvitation insertOrUpdate(DrinkingInvitation i) throws DataAccessException {
        try {
            SaveDrinkingInvitationRequest req = new SaveDrinkingInvitationRequest(i);
            req.loadDataFromNetwork();
        } catch (Exception e) {
            throw new DataAccessException("Failed to insertOrUpdate DrinkingInvitation",e);
        }
        return i;
    }

    @Override
    public List<DrinkingInvitation> getAllFor(long personid) throws DataAccessException {
        try {
            GetAllForDrinkingInvitationRequest req = new GetAllForDrinkingInvitationRequest(personid);
            DrinkingInvitation[] drinkingInvitations = req.loadDataFromNetwork();
            return Arrays.asList(drinkingInvitations);
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }


    @Override
    public List<DrinkingInvitation> getAllFrom(long personid) throws DataAccessException {
        try {
            GetAllFromDrinkingInvitationRequest req = new GetAllFromDrinkingInvitationRequest(personid);
            DrinkingInvitation[] drinkingInvitations = req.loadDataFromNetwork();
            return Arrays.asList(drinkingInvitations);   } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update FriendInvitation",e);
        }
    }

    @Override
    public void accept(DrinkingInvitation friendInvitation) throws DataAccessException {
        try {
            //Eingeladener joined dem drinking Spot
            JoinDrinkingSpotRequest req = new JoinDrinkingSpotRequest(friendInvitation.getEingeladenerId(), friendInvitation.getDrinkingSpotId());
            req.loadDataFromNetwork();

            //FIXME wirklich löschen
            //löschen der Einladung
            friendInvitation.setEingeladenerId(0l);
            friendInvitation.setDrinkingSpotId(0l);
            friendInvitation.setEinladerId(0l);
            insertOrUpdate(friendInvitation);
        } catch (Exception e) {
            throw new DataAccessException("Failed to accept DrinkingInvitation",e);
        }

    }

    @Override
    public void decline(DrinkingInvitation invitation) throws BeerBuddyException {

    }


}
