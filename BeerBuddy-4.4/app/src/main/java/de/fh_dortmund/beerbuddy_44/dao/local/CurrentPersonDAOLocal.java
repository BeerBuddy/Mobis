package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.CurrentPersonDAO;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.IdNotSetException;

/**
 * Created by David on 19.11.2015.
 */
public class CurrentPersonDAOLocal extends CurrentPersonDAO{

    private SharedPreferences settings;
    public CurrentPersonDAOLocal(Context context) {
        super(context);
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public long getCurrentPersonId() throws BeerBuddyException{
        return settings.getLong("userid",0);
    }

    public void insertCurrentPersonId(long person) throws BeerBuddyException{
        if(person != 0)
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong("userid", person);
            editor.commit();
        }else{
            throw new IdNotSetException("The person Id is 0");
        }

    }

    public void deleteCurrentPerson() throws BeerBuddyException{
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("userid");
        editor.commit();
    }


}
