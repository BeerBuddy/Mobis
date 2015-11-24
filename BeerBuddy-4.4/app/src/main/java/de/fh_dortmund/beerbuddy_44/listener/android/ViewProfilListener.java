package de.fh_dortmund.beerbuddy_44.listener.android;

import android.util.Log;
import android.view.View;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.ViewProfilActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public class ViewProfilListener implements  View.OnClickListener {

    private static final String TAG = "ViewProfilListener";
    private ViewProfilActivity viewProfilActivity;
    private Person profil;

    public ViewProfilListener(ViewProfilActivity viewProfilActivity, Person id) {
        this.profil = id;
        this.viewProfilActivity = viewProfilActivity;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_profil_send_request: sendRequest();
                break;
            default:
                Log.e(TAG, "No Action specified for Button");
                break;
        }
    }

    private void sendRequest() {
        try {
            Person p =DAOFactory.getPersonDAO(viewProfilActivity).getById(DAOFactory.getCurrentPersonDAO(viewProfilActivity).getCurrentPersonId());
            DAOFactory.getFreindlistDAO(viewProfilActivity).addFreindRequest(profil.getId(), p);
        } catch (BeerBuddyException e) {
            Log.e(TAG, "Error accoured during request", e);
        }
    }
}
