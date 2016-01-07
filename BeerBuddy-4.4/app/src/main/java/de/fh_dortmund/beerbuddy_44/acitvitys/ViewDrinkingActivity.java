package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;

import de.fh_dortmund.beerbuddy_44.IntentUtil;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.BuddyListAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.MissingParameterExcetion;
import lombok.Getter;

/**
 * Created by David on 01.11.2015.
 * <p/>
 * Revised and Updated by Marco on 10.12.2015.
 */
public class ViewDrinkingActivity extends BeerBuddyActivity  implements OnMapReadyCallback{
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
            //get current GPS position
            if (drinkingSpot != null) {
                //move the map to current location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ObjectMapperUtil.getLatLangFropmGPS(drinkingSpot.getGps()), 20));
                createMarker(drinkingSpot,googleMap);
            }
    }

    public ViewDrinkingActivity() {
        super(R.layout.view_drinking_activity_main, true);
    }

    private GoogleMap mMap;
    private static final String TAG = "ViewDrinkingAct";
    @Getter
    private DrinkingSpot drinkingSpot;

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

                drinkingSpot = DAOFactory.getDrinkingSpotDAO(this).getById(id);
                if (drinkingSpot != null) {
                    setValue(drinkingSpot);
                }
            } else {
                throw new MissingParameterExcetion("Expected a Parameter: long id when calling ViewDrinkingActivity");
            }
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during getDrinkingSpot", e);
        }
    }

    public void setValue(final DrinkingSpot spot) {

        ((TextView) findViewById(R.id.drinking_view_age)).setText(spot.getAgeFrom() +" - " +spot.getAgeTo());
      ((TextView) findViewById(R.id.drinking_view_creatorname)).setText(spot.getCreator().getUsername());
        ((TextView) findViewById(R.id.drinking_view_description)).setText(spot.getBeschreibung());
        final Context context = this;


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
        int amount = spot.getAmountMaleWithoutBeerBuddy() +
                spot.getAmountFemaleWithoutBeerBuddy() +
                spot.getPersons().size();
        ((TextView) findViewById(R.id.drinking_view_isdrinkingtext)).setText(getString(R.string.mainview_isdrinkinginagroup) + " " + amount);

        ((ListView) findViewById(R.id.drinking_view_usersjoined)).setAdapter(new BuddyListAdapter(context, R.layout.buddy_list_row_layout, spot.getPersons().toArray(new Person[]{})));
        ((Button) findViewById(R.id.drinking_view_creatorprofil)).setOnClickListener(new IntentUtil.ShowProfilListener(context, spot.getCreator().getId()));
        ((Button) findViewById(R.id.drinking_view_navigate)).setOnClickListener(new IntentUtil.ShowDrinkingSpotOnGoogleMapListener(context, spot));
        ((Button) findViewById(R.id.drinking_view_showonmap)).setOnClickListener(new IntentUtil.ShowDrinkingSpotOnMapListener(context, spot.getId()));
        ((Button) findViewById(R.id.drinking_view_join)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DAOFactory.getDrinkingSpotDAO(context).join(spot.getId(), DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId());
                } catch (BeerBuddyException e) {
                    e.printStackTrace();
                    Log.w(TAG, "Error accord during join", e);
                }
            }
        });

    }

}