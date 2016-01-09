package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.DrinkingInvitationAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;

/**
 * Created by David on 01.12.2015.
 */
public class DrinkingInvitationActivity extends BeerBuddyActivity {
    private static final String TAG = "DrinkingInvitationAct";

    public DrinkingInvitationActivity() {
        super(R.layout.drinkinginvitations_activity_main, true);
    }

    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DrinkingInvitationAdapter.UPDATE_DRINKING_INVITATIONS);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setValue();
            }
        }, intentFilter);

        setValue();
    }

    public void setValue() {
        try {
            DAOFactory.getDrinkingInvitationDAO(this).getAllFor(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId(), new RequestListener<DrinkingInvitation[]>() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    Log.e(TAG, "Error accured during get DrinkingInvitations", e);
                }

                @Override
                public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                    setValueDI(Arrays.asList(drinkingInvitations));
                }
            });
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during get DrinkingInvitations", e);
        }
    }

    public void setValueDI(List<DrinkingInvitation> drinkingInvitations) {
        DrinkingInvitationAdapter drinkingInvitationAdapter = new DrinkingInvitationAdapter(this, R.layout.buddy_list_row_layout, drinkingInvitations.toArray(new DrinkingInvitation[]{}));
        ((ListView) findViewById(R.id.drinkinginvitation_invitations)).setAdapter(drinkingInvitationAdapter);
    }

}