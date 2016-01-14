package de.fh_dortmund.beerbuddy_44.dao;

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
import de.fh_dortmund.beerbuddy_44.dao.sync.DrinkingInvitationDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.DrinkingSpotDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.FriendInvitationDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.FriendListDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.PersonDAOSync;


/**
 * Created by David on 11.11.2015.
 */
public final class DAOFactory {

    //singelton
    private static LocationDAO locationDAO = null;


    private DAOFactory() {
        //prevent Objects
    }

    /*
    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    */


    public static LocationDAO getLocationDAO(BeerBuddyActivity context) throws BeerBuddyException {
        if (locationDAO == null) {
            locationDAO = new LocationDAOLocal(context);
        }
        return locationDAO;
    }


    public static PersonDAO getPersonDAO(BeerBuddyActivity context) {
        return new PersonDAOSync(context);
    }

    public static CurrentPersonDAO getCurrentPersonDAO(BeerBuddyActivity context) {
        // we have no current person DAO on the server. It's not necessary at all, so we always use the local dao!
        return new CurrentPersonDAOLocal(context);
    }

    public static FriendListDAO getFriendlistDAO(BeerBuddyActivity context) {
        return new FriendListDAOSync(context);
    }

    public static DrinkingSpotDAO getDrinkingSpotDAO(BeerBuddyActivity context) {
        return new DrinkingSpotDAOSync(context);
    }

    public static FriendInvitationDAO getFriendInvitationDAO(BeerBuddyActivity context) {
        return new FriendInvitationDAOSync(context);
    }

    public static DrinkingInvitationDAO getDrinkingInvitationDAO(BeerBuddyActivity context) {
        return new DrinkingInvitationDAOSync(context);
    }
}
