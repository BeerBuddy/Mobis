package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.DrinkingInvitationAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 01.12.2015.
 */
public class DrinkingInvitationActivity extends BeerBuddyActivity {
    public DrinkingInvitationActivity() {
        super(R.layout.drinkinginvitations_activity_main, true);
    }

    private static final String TAG = "DrinkingInvitationAct";


    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {
        try {
            DAOFactory.getDrinkingInvitationDAO(this).getAllFor(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId(), new RequestListener<DrinkingInvitation[]>() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    Log.e(TAG, "Error accured during get DrinkingInvitations", e);
                }

                @Override
                public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                    setValues(Arrays.asList(drinkingInvitations));
                }
            });
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during get DrinkingInvitations", e);
        }
    }

    public void setValues(List<DrinkingInvitation> drinkingInvitations) {
        DrinkingInvitationAdapter drinkingInvitationAdapter = new DrinkingInvitationAdapter(this, R.layout.buddy_list_row_layout, drinkingInvitations.toArray(new DrinkingInvitation[]{}));
        ((ListView) findViewById(R.id.drinkinginvitation_invitations)).setAdapter(drinkingInvitationAdapter);


    }

}
