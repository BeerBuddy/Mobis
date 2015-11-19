package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.fh_dortmund.beerbuddy_44.acitvitys.ViewProfilActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.CurrentPersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by David on 11.11.2015.
 */
public final class DAOFactory {

    private DAOFactory(){
        //prevent Objects
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
}
