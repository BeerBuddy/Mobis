package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.ViewProfilActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.CurrentPersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.LocationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

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


    public static PersonDAO getPersonDAO(Context context){
        if(isOnline(context))
        {
            return new PersonDAOLocal(context);
        }else
        {
            return new PersonDAORemote(context);
        }
    }

    public static CurrentPersonDAO getCurrentPersonDAO(Context context){
        return new CurrentPersonDAOLocal(context);
    }

    public static FriendListDAO getFreindlistDAO(Context context) {
            return new FriendListDAOLocal(context);
    }

    public static DrinkingSpotDAO getDrinkingSpotDAO(Context context) {
        return new DrinkingSpotDAOMock(context);
    }


}
