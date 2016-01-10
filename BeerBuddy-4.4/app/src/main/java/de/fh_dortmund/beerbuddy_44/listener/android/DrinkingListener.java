package de.fh_dortmund.beerbuddy_44.listener.android;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.DrinkingActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.picker.BuddyPicker;

/**
 * Created by grimm on 01.12.2015.
 */
public class DrinkingListener implements View.OnClickListener {

    private static final String TAG = "DrinkingListener";
    private DrinkingActivity context;

    public DrinkingListener(DrinkingActivity context) {
        this.context = context;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drinking_group:
                showGroupLayout();
                break;
            case R.id.drinking_alone:
                hideGroupLayout();
                break;
            case R.id.drinking_invite:
                showInviteDialog();
                break;
            case R.id.drinking_save:
                saveDrinkingSpot();
                break;
            default:
                Log.e(TAG, "No Action defined for " + v.getId());
                break;
        }

    }

    private void saveDrinkingSpot() {
            DrinkingSpot drinkingSpot = context.getValue();
            DAOFactory.getDrinkingSpotDAO(context).insertOrUpdate(drinkingSpot, new RequestListener<DrinkingSpot>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    Log.e(TAG, "Error during save of DrinkingSpot " ,spiceException );
                }

                @Override
                public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                    Toast.makeText(context, context.getString(R.string.drinking_saved), Toast.LENGTH_SHORT).show();
                }
            });


    }

    private void showInviteDialog() {
        BuddyPicker.show(context);
    }

    private void showGroupLayout() {
        LinearLayout layout = (LinearLayout) context.findViewById(R.id.drinking_group_layout);
        layout.setVisibility(View.VISIBLE);
    }

    private void hideGroupLayout() {
        LinearLayout layout = (LinearLayout) context.findViewById(R.id.drinking_group_layout);
        layout.setVisibility(View.GONE);
        context.getDrinkingSpot().setAmountMaleWithoutBeerBuddy(0);
        context.getDrinkingSpot().setAmountFemaleWithoutBeerBuddy(0);
        context.getDrinkingSpot().setAgeFrom(0);
        context.getDrinkingSpot().setAgeTo(0);
        ((NumberPicker)context.findViewById(R.id.drinking_group_amount_female)).setValue(context.getDrinkingSpot().getAmountFemaleWithoutBeerBuddy());
        ((NumberPicker)context.findViewById(R.id.drinking_group_amount_male)).setValue(context.getDrinkingSpot().getAmountMaleWithoutBeerBuddy());
        ((NumberPicker) context.findViewById(R.id.drinking_group_age_from)).setValue(context.getDrinkingSpot().getAgeFrom());
        ((NumberPicker) context.findViewById(R.id.drinking_group_age_to)).setValue(context.getDrinkingSpot().getAgeTo());
    }
}
