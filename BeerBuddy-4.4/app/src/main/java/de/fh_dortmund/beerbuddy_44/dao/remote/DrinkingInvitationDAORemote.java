package de.fh_dortmund.beerbuddy_44.dao.remote;

import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.requests.AcceptDrinkingInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.DeclineDrinkingInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetAllForDrinkingInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetAllFromDrinkingInvitationRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveDrinkingInvitationRequest;

/**
 * Created by David on 30.11.2015.
 */
public class DrinkingInvitationDAORemote extends DrinkingInvitationDAO {

    public DrinkingInvitationDAORemote(BeerBuddyActivity context) {
        super(context);
    }

    @Override
    public void insertOrUpdate(DrinkingInvitation i, RequestListener<DrinkingInvitation> listener)  {
            SaveDrinkingInvitationRequest req = new SaveDrinkingInvitationRequest(i);
            context.getSpiceManager().execute(req, listener);
    }

    @Override
    public void getAllFor(long personid, RequestListener<DrinkingInvitation[]> listener)  {
            GetAllForDrinkingInvitationRequest req = new GetAllForDrinkingInvitationRequest(personid);
            context.getSpiceManager().execute(req, listener);
    }

    @Override
    public void getAllFrom(long personid, RequestListener<DrinkingInvitation[]> listener)  {
            GetAllFromDrinkingInvitationRequest req = new GetAllFromDrinkingInvitationRequest(personid);
            context.getSpiceManager().execute(req, listener);
    }

    @Override
    public void accept(DrinkingInvitation friendInvitation, RequestListener<Void> listener)  {
       AcceptDrinkingInvitationRequest req = new AcceptDrinkingInvitationRequest(friendInvitation);
        context.getSpiceManager().execute(req, listener);
    }

    @Override
    public void decline(DrinkingInvitation invitation, RequestListener<Void> listener) {
        DeclineDrinkingInvitationRequest req = new DeclineDrinkingInvitationRequest(invitation);
        context.getSpiceManager().execute(req, listener);
    }


}
