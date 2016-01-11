package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.google.android.gms.maps.model.LatLng;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Date;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.listener.android.DrinkingListener;
import lombok.Getter;

public class DrinkingActivity extends BeerBuddyActivity {
    private static final String TAG = "DrinkingActivity";
    @Getter
    private DrinkingSpot drinkingSpot;
    private Person creator;
    private long[] invitedPersons;

    public DrinkingActivity() {
        super(R.layout.drinking_activity_main, true);
    }

    @Override
    public void onFurtherCreate(Bundle savedInstanceState) {
            DrinkingListener drinkingListener = new DrinkingListener(this);
            Button save = (Button) findViewById(R.id.drinking_save);
            Button deactivate = (Button) findViewById(R.id.drinking_deactivate);
            Button invite = (Button) findViewById(R.id.drinking_invite);
            RadioButton alone = (RadioButton) findViewById(R.id.drinking_alone);
            RadioButton group = (RadioButton) findViewById(R.id.drinking_group);

            alone.setOnClickListener(drinkingListener);
            group.setOnClickListener(drinkingListener);
            invite.setOnClickListener(drinkingListener);
            save.setOnClickListener(drinkingListener);
            deactivate.setOnClickListener(drinkingListener);

            ((RadioButton)findViewById(R.id.drinking_alone)).setChecked(true);

            try {
                //Let's get the current Person
                long theId = DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId();
                DAOFactory.getPersonDAO(this).getById(theId, new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException e) {
                        Log.e(TAG, "Error accured during getPerson", e);
                    }

                    @Override
                    public void onRequestSuccess(Person person) {
                        creator = person;
                    }
                });

                DAOFactory.getDrinkingSpotDAO(this).getActiveByPersonId(theId, new RequestListener<DrinkingSpot>() {
                     @Override
                     public void onRequestFailure(SpiceException e) {
                         Log.e(TAG, "Error accured during getDrinkingSpot", e);
                         //if it's a new spot, we need a spot-object first
                         //and we set the current Person as its creator
                         if (drinkingSpot == null) {
                             createDrinkingSpot();
                         }
                     }

                     @Override
                     public void onRequestSuccess(DrinkingSpot oldDrinkingSpot) {
                         if (oldDrinkingSpot != null) {
                             drinkingSpot = oldDrinkingSpot;
                             setValue(oldDrinkingSpot);
                         }
                     }
                 });

            } catch (BeerBuddyException e) {
                e.printStackTrace();
                Log.e(TAG, "Error accured during getDrinkingSpot", e);
            }

            //Listener for Scrolling Elements within ScrollView
            View.OnTouchListener otl = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            };

            EditText et = (EditText) findViewById(R.id.drinking_description);
            et.setOnTouchListener(otl);

            NumberPicker np1 = (NumberPicker) findViewById(R.id.drinking_group_amount_male);
            np1.setMinValue(0);
            np1.setMaxValue(20);
            np1.setWrapSelectorWheel(false);
            NumberPicker np2 = (NumberPicker) findViewById(R.id.drinking_group_amount_female);
            np2.setMinValue(0);
            np2.setMaxValue(20);
            np2.setWrapSelectorWheel(false);
            NumberPicker np3 = (NumberPicker) findViewById(R.id.drinking_group_age_from);
            np3.setMinValue(0);
            np3.setMaxValue(80);
            np3.setWrapSelectorWheel(false);
            NumberPicker np4 = (NumberPicker) findViewById(R.id.drinking_group_age_to);
            np4.setMinValue(0);
            np4.setMaxValue(80);
            np4.setWrapSelectorWheel(false);

    }

    public void createDrinkingSpot(){
        drinkingSpot = new DrinkingSpot();
        drinkingSpot.setCreator(creator);
    }

    public DrinkingSpot getValue() {
        try {
            //get current Location
            LatLng location = ObjectMapperUtil.getLatLngFromLocation(DAOFactory.getLocationDAO(this).getCurrentLocation());

            if (drinkingSpot == null) {
                createDrinkingSpot();
            }

            //Converting Location: LatLng --> String
            if (location != null) {
                String locationString = location.toString();
                locationString = locationString.substring(9);
                locationString = locationString.replace("(", "");
                locationString = locationString.replace(")", "");
                locationString = locationString.replace(",", ";");
                drinkingSpot.setGps(locationString);
            }
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during Location ", e);
        }

        if (!drinkingSpot.getPersons().contains(drinkingSpot.getCreator())) {
            drinkingSpot.getPersons().add(drinkingSpot.getCreator());
        }

        drinkingSpot.setStartTime(new Date());
        drinkingSpot.setBeschreibung(((EditText) findViewById(R.id.drinking_description)).getText().toString());
        drinkingSpot.setAmountFemaleWithoutBeerBuddy(((NumberPicker) findViewById(R.id.drinking_group_amount_female)).getValue());
        drinkingSpot.setAmountMaleWithoutBeerBuddy(((NumberPicker) findViewById(R.id.drinking_group_amount_male)).getValue());
        drinkingSpot.setAgeFrom(((NumberPicker) findViewById(R.id.drinking_group_age_from)).getValue());
        drinkingSpot.setAgeTo(((NumberPicker) findViewById(R.id.drinking_group_age_to)).getValue());
        return drinkingSpot;
    }

    public void setValue(DrinkingSpot spot) {

        if(spot.getPersons().isEmpty() && spot.getAgeFrom() == 0 && spot.getAgeTo() == 0 && spot.getAmountFemaleWithoutBeerBuddy() ==0 && spot.getAmountMaleWithoutBeerBuddy() == 0)
        {
            //hes drinking alone
            ((RadioButton)findViewById(R.id.drinking_alone)).setChecked(true);
        }
        else
        {
            //hes drinking in a group
            ((RadioButton)findViewById(R.id.drinking_group)).setChecked(true);
            LinearLayout layout = (LinearLayout) findViewById(R.id.drinking_group_layout);
            layout.setVisibility(View.VISIBLE);
            //start counting

            int male = 0;
            int female = 0;
            int minAge =  Integer.MAX_VALUE;
            int maxAge = -1;
            //male = spot.getAmountMaleWithoutBeerBuddy();
            //female = spot.getAmountFemaleWithoutBeerBuddy();
            minAge = spot.getAgeFrom();
            maxAge = spot.getAgeTo();
            List<Person> persons = spot.getPersons();
            if (!persons.contains(spot.getCreator())) {
                persons.add(spot.getCreator());
            }

            for (Person p : persons) {
                int age = Integer.MAX_VALUE;
                if (p.getDateOfBirth() != null) {
                    age = ObjectMapperUtil.getAgeFromBirthday(p.getDateOfBirth());
                }

                if(age > maxAge )
                {
                    maxAge = age;
                }

                if(age < minAge || minAge == 0)
                {
                    minAge = age;
                }

                if (p.getGender() == Person.Gender.MALE) {
                    male++;
                } else if (p.getGender() == Person.Gender.FEMALE) {
                    female++;
                } else {
                    Log.e(TAG, "undefined Gender in person: " +p.getId());
                }
            }

            if((minAge < spot.getAgeFrom() && minAge != Integer.MAX_VALUE)|| (minAge == 0));
            {
                drinkingSpot.setAgeFrom(minAge);
            }

            if(maxAge > spot.getAgeTo() && maxAge != -1)
            {
                drinkingSpot.setAgeTo(maxAge);
            }

            drinkingSpot.setAmountFemaleWithoutBeerBuddy(female);
            drinkingSpot.setAmountMaleWithoutBeerBuddy(male);

            ((NumberPicker)findViewById(R.id.drinking_group_amount_female)).setValue(drinkingSpot.getAmountFemaleWithoutBeerBuddy());
            ((NumberPicker)findViewById(R.id.drinking_group_amount_male)).setValue(drinkingSpot.getAmountMaleWithoutBeerBuddy());
            ((NumberPicker)findViewById(R.id.drinking_group_age_from)).setValue(drinkingSpot.getAgeFrom());
            ((NumberPicker)findViewById(R.id.drinking_group_age_to)).setValue(drinkingSpot.getAgeTo());
        }
        ((EditText)findViewById(R.id.drinking_description)).setText(spot.getBeschreibung());
    }

    public long[] getInvitedPersons(){
        return invitedPersons;
    }

    public void setInvitedPersons(long[] iP){
        invitedPersons = iP;
    }
}