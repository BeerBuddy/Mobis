package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by David on 11.11.2015.
 */
public abstract class DAOFactory {

    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    public static PersonDAO getPersonDAO(Context context){

        return new PersonDAOMock(context);
        //FIXME bisher ist nur der Mock eingehängt
       /* if(isOnline(context))
        {
            return new PersonDAOLocal(context);
        }else
        {
            return new PersonDAORemote(context);
        }*/
    }
}
