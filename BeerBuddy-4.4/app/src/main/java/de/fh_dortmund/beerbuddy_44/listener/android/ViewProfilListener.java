package de.fh_dortmund.beerbuddy_44.listener.android;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.ViewProfilActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 19.11.2015.
 */
public class ViewProfilListener implements View.OnClickListener {

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
            case R.id.action_profil_send_request:
                sendRequest();
                break;
            default:
                Log.e(TAG, "No Action specified for Button");
                break;
        }
    }

    //TODO show Dialog to enter a invitation text
    private void sendRequest() {
        try {
            FriendInvitation i = new FriendInvitation();
            i.setEingeladenerId(profil.getId());
            i.setEinladerId(DAOFactory.getCurrentPersonDAO(viewProfilActivity).getCurrentPersonId());
            DAOFactory.getFriendInvitationDAO(viewProfilActivity).insertOrUpdate(i, new RequestListener<FriendInvitation>() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    Log.e(TAG, "Error accoured during request", e);
                }

                @Override
                public void onRequestSuccess(FriendInvitation friendInvitation) {
                    Toast.makeText(viewProfilActivity, viewProfilActivity.getString(R.string.request_send), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (BeerBuddyException e) {
            Log.e(TAG, "Error accoured during request", e);
        }
    }
}
