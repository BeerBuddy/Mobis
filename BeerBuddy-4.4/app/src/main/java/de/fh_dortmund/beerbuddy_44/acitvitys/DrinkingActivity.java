package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import de.fh_dortmund.beerbuddy.DrinkingSpot;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.InvitedListAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.listener.android.DrinkingListener;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;
import lombok.Getter;

/**
 * Created by David on 01.11.2015.
 *
 * Revised and Updated by Marco on 10.12.2015.
 */
public class DrinkingActivity extends AppCompatActivity {
    private static final String TAG = "DrinkingActivity";
    @Getter
    private DrinkingSpot drinkingSpot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.drinking_activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //finish instance on Logout
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    finish();
                }
            }, intentFilter);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            //register Navigation Listener
            NavigationListener listener = new NavigationListener(this);
            NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(listener);

            DrinkingListener drinkingListener = new DrinkingListener(this);

            Button save = (Button) findViewById(R.id.drinking_save);
            Button invite = (Button) findViewById(R.id.drinking_invite);
            RadioButton alone = (RadioButton) findViewById(R.id.drinking_alone);
            RadioButton group = (RadioButton) findViewById(R.id.drinking_group);

            alone.setOnClickListener(drinkingListener);
            group.setOnClickListener(drinkingListener);
            invite.setOnClickListener(drinkingListener);
            save.setOnClickListener(drinkingListener);

            try {
                 drinkingSpot = DAOFactory.getDrinkingSpotDAO(this).getActiveById(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
                if (drinkingSpot != null) {
                    setValue(drinkingSpot);
                }
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

            ListView lv = (ListView) findViewById(R.id.drinking_buddys);
            lv.setOnTouchListener(otl);
            EditText et = (EditText) findViewById(R.id.drinking_description);
            et.setOnTouchListener(otl);
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

            int male =0;
            int female =0;
            int minAge =  Integer.MAX_VALUE;
            int maxAge =-1;

            InvitedListAdapter adapter = new InvitedListAdapter(this,
                    R.layout.buddy_list_row_layout, spot.getPersons().toArray(new Person[]{}));
            ((ListView)findViewById(R.id.drinking_buddys)).setAdapter(adapter);

            for (Person p : spot.getPersons()) {
                int age = ObjectMapperUtil.getAgeFromBirthday(p.getDateOfBirth());

                if(age > maxAge )
                {
                    maxAge =age;
                }

                if(age < minAge)
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

            if(minAge > spot.getAgeFrom() )
            {
                minAge = spot.getAgeFrom();
            }

            if(maxAge < spot.getAgeTo())
            {
                maxAge = spot.getAgeTo();
            }

            ((NumberPicker)findViewById(R.id.drinking_group_amount_female)).setValue(spot.getAmountFemaleWithoutBeerBuddy());
            ((NumberPicker)findViewById(R.id.drinking_group_amount_male)).setValue(spot.getAmountMaleWithoutBeerBuddy());

            ((NumberPicker)findViewById(R.id.drinking_group_age_from)).setValue(minAge);
            ((NumberPicker)findViewById(R.id.drinking_group_age_to)).setValue(maxAge);


        }

        ((EditText)findViewById(R.id.drinking_description)).setText(spot.getDescription());


    }

    public DrinkingSpot getValue() {

        drinkingSpot.setDescription(((EditText) findViewById(R.id.drinking_description)).getText().toString());
        drinkingSpot.setAmountFemaleWithoutBeerBuddy(((NumberPicker) findViewById(R.id.drinking_group_amount_female)).getValue());
        drinkingSpot.setAmountMaleWithoutBeerBuddy(((NumberPicker) findViewById(R.id.drinking_group_amount_male)).getValue());
        drinkingSpot.setAgeFrom(((NumberPicker) findViewById(R.id.drinking_group_age_from)).getValue());
        drinkingSpot.setAgeTo(((NumberPicker) findViewById(R.id.drinking_group_age_to)).getValue());
        return drinkingSpot;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }
}