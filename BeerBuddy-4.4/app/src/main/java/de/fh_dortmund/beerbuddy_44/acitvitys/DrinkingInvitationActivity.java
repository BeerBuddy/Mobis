package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import de.fh_dortmund.beerbuddy.DrinkingInvitation;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.DrinkingInvitationAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

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
        setValues();
    }

    public void setValues() {
        try {
            List<DrinkingInvitation> drinkingInvitations = DAOFactory.getDrinkingInvitationDAO(this).getAllFor(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
            DrinkingInvitationAdapter drinkingInvitationAdapter = new DrinkingInvitationAdapter(this, R.layout.buddy_list_row_layout, drinkingInvitations.toArray(new DrinkingInvitation[]{}));
            ((ListView) findViewById(R.id.drinkinginvitation_invitations)).setAdapter(drinkingInvitationAdapter);
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during get DrinkingInvitations", e);
        }

    }

}
