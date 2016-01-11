package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.IntentUtil;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.FriendListAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.MissingParameterExcetion;
import lombok.Getter;

/**
 * Created by David on 01.11.2015.
 * <p/>
 * Revised and Updated by Marco on 10.12.2015.
 */
public class ViewDrinkingActivity extends BeerBuddyActivity implements OnMapReadyCallback {
    private static final String TAG = "ViewDrinkingAct";
    private GoogleMap mMap;
    @Getter
    private DrinkingSpot drinkingSpot;
    public ViewDrinkingActivity() {
        super(R.layout.view_drinking_activity_main, true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        //get current GPS position
        if (drinkingSpot != null) {
            //move the map to current location
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ObjectMapperUtil.getLatLangFropmGPS(drinkingSpot.getGps()), 15));
            createMarker(drinkingSpot, googleMap);
        }
    }

    @Override
    public void onFurtherCreate(Bundle savedInstanceState) {
        //Get the Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = getIntent().getExtras();
        long id = b.getLong("id");

        try {
            if (id != 0) {
                DAOFactory.getDrinkingSpotDAO(this).getById(id, new RequestListener<DrinkingSpot>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Log.e(TAG, "Error occurred during getDrinkingSpot", spiceException);
                    }

                    @Override
                    public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                        if (drinkingSpot != null) {
                            setValue(drinkingSpot);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ObjectMapperUtil.getLatLangFropmGPS(drinkingSpot.getGps()), 15));
                            createMarker(drinkingSpot, mMap);
                        }
                    }
                });

            } else {
                throw new MissingParameterExcetion("Expected a Parameter: long id when calling ViewDrinkingActivity");
            }
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during getDrinkingSpot", e);
        }
    }

    public void setValue(final DrinkingSpot spot) {
        int ageFrom = spot.getAgeFrom();
        if (ageFrom == 0){
            ageFrom = ObjectMapperUtil.getAgeFromBirthday(spot.getCreator().getDateOfBirth());
        }
        int ageTo = spot.getAgeTo();
        if (ageTo == 0){
            ageTo = ObjectMapperUtil.getAgeFromBirthday(spot.getCreator().getDateOfBirth());
        }
        ((TextView) findViewById(R.id.drinking_view_age)).setText(ageFrom + " - " + ageTo);

        if (spot.getCreator().getUsername() != null) {
            ((TextView) findViewById(R.id.drinking_view_creatorname)).setText(getResources().getText(R.string.mainview_creator) + ": " + spot.getCreator().getUsername());
        } else {
            ((TextView) findViewById(R.id.drinking_view_creatorname)).setText(getResources().getText(R.string.mainview_creator) + ": " + spot.getCreator().getEmail());
        }

        ((TextView) findViewById(R.id.drinking_view_description)).setText(spot.getBeschreibung());

        try {
            long id = DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId();
            List<Person> persons = spot.getPersons();
            if (spot.getCreator().getId() == id){
                findViewById(R.id.drinking_view_join).setVisibility(View.GONE);
            }
            else {
                for (Person p : persons) {
                    if (p.getId() == id) {
                        findViewById(R.id.drinking_view_join).setVisibility(View.GONE);
                    }
                }
            }
        }catch (BeerBuddyException e){
            Log.d(TAG, "Error occured during getCurrentPersonId" + e);
        }

        final BeerBuddyActivity context = this;

        /*
        LinearLayout layout = (LinearLayout) findViewById(R.id.drinking_view_groupmembers);
        for (int i = 0; i < spot.getAmountMaleWithoutBeerBuddy(); i++) {
            ImageView imageView = new ImageView(this);
            //setting image resource
            imageView.setImageResource(R.drawable.ic_human_male);
            //setting image position
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(imageView);
        }

        for (int i = 0; i < spot.getAmountFemaleWithoutBeerBuddy(); i++) {
            ImageView imageView = new ImageView(this);
            //setting image resource
            imageView.setImageResource(R.drawable.ic_human_male);
            //setting image position
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.addView(imageView);
        }
        */
        int amount = spot.getAmountMaleWithoutBeerBuddy() +
                spot.getAmountFemaleWithoutBeerBuddy() +
                spot.getPersons().size() + 1;
        ((TextView) findViewById(R.id.drinking_view_isdrinkingtext)).setText(getString(R.string.mainview_isdrinkinginagroup) + " " + amount);

        ((ListView) findViewById(R.id.drinking_view_usersjoined)).setAdapter(new FriendListAdapter(context, R.layout.buddy_list_row_layout, spot.getPersons().toArray(new Person[]{})));
        findViewById(R.id.drinking_view_creatorprofil).setOnClickListener(new IntentUtil.ShowProfilListener(context, spot.getCreator().getId()));
        findViewById(R.id.drinking_view_navigate).setOnClickListener(new IntentUtil.ShowDrinkingSpotOnGoogleMapListener(context, spot));
        findViewById(R.id.drinking_view_showonmap).setOnClickListener(new IntentUtil.ShowDrinkingSpotOnMapListener(context, spot.getId()));

        Bundle extras = getIntent().getExtras();
        boolean joinPossible = extras.getBoolean("joinPossible");

        if (joinPossible) {
            findViewById(R.id.drinking_view_join).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DAOFactory.getDrinkingSpotDAO(context).join(spot.getId(), DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId(), new RequestListener<Void>() {
                            @Override
                            public void onRequestFailure(SpiceException e) {
                                Log.w(TAG, "Error accord during join", e);
                            }

                            @Override
                            public void onRequestSuccess(Void aVoid) {

                            }
                        });
                    } catch (BeerBuddyException e) {
                        Log.w(TAG, "Error accord during join", e);
                        e.printStackTrace();
                    }
                }
            });
        } else {
            // if join is not possible we hide the button
            findViewById(R.id.drinking_view_join).setVisibility(View.GONE);
        }



    }

}