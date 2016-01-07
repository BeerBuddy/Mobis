package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.DeactivateDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetActiveForDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetAllDrinkingSpotsRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetByIDDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.JoinDrinkingSpotRequest;
import de.fh_dortmund.beerbuddy_44.requests.SaveDrinkingSpotRequest;

/**
 * Created by David on 19.11.2015.
 */
public class DrinkingSpotDAORemote extends DrinkingSpotDAO {



    public DrinkingSpotDAORemote(BeerBuddyActivity context) {
      super(context);
    }

    @Override
    public void getAll(RequestListener<DrinkingSpot[]> listener)  {
            GetAllDrinkingSpotsRequest req = new GetAllDrinkingSpotsRequest();
            context.getSpiceManager().execute(req, listener);
    }

    @Override

    public void getActiveByPersonId(long personId,RequestListener<DrinkingSpot> listener)  {
            GetActiveForDrinkingSpotRequest req = new GetActiveForDrinkingSpotRequest(personId);
            context.getSpiceManager().execute(req, listener);
    }
    @Override
    public void insertOrUpdate(DrinkingSpot drinkingSpot,RequestListener<DrinkingSpot> listener)  {
            SaveDrinkingSpotRequest req = new SaveDrinkingSpotRequest(drinkingSpot);
            context.getSpiceManager().execute(req, listener);
    }

    @Override
    public void getById(long dsid,RequestListener<DrinkingSpot> listener)  {
            GetByIDDrinkingSpotRequest req = new GetByIDDrinkingSpotRequest(dsid);
            context.getSpiceManager().execute(req, listener);
    }


    @Override
    public void join(long dsid, long personId,RequestListener<Void> listener)  {
            JoinDrinkingSpotRequest req = new JoinDrinkingSpotRequest(dsid, personId);
            context.getSpiceManager().execute(req, listener);
    }

    @Override
    public void deactivate(long dsid,RequestListener<Void> listener)  {
        DeactivateDrinkingSpotRequest req = new DeactivateDrinkingSpotRequest(dsid);
        context.getSpiceManager().execute(req, listener);
    }

}
