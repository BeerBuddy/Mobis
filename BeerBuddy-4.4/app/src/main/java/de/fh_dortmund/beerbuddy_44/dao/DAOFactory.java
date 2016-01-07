package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.CurrentPersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.LocationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.CurrentPersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.local.LocationDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendListDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;


/**
 * Created by David on 11.11.2015.
 */
public final class DAOFactory {

    //singelton
    private static LocationDAO locationDAO= null;


    private DAOFactory(){
        //prevent Objects
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static LocationDAO getLocationDAO(Context context) throws BeerBuddyException
    {
        if(locationDAO == null)
        {
            locationDAO =  new LocationDAOLocal(context);
        }
        return locationDAO;
    }


    public static PersonDAO getPersonDAO(BeerBuddyActivity context){
        return new PersonDAORemote(context);

    public static CurrentPersonDAO getCurrentPersonDAO(Context context){
        return new CurrentPersonDAOLocal(context);
    }

    public static FriendListDAO getFriendlistDAO(BeerBuddyActivity context) {
            return new FriendListDAORemote(context);
    }

    public static DrinkingSpotDAO getDrinkingSpotDAO(BeerBuddyActivity context) {
        return new DrinkingSpotDAORemote(context);
    }
    public static FriendInvitationDAO getFriendInvitationDAO(BeerBuddyActivity context) {
        return new FriendInvitationDAORemote(context);
    }

    public static DrinkingInvitationDAO getDrinkingInvitationDAO(BeerBuddyActivity context) {
        return new DrinkingInvitationDAORemote(context);
    }
}
