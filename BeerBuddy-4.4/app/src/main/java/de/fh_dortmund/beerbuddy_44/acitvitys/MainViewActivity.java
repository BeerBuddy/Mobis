package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import de.fh_dortmund.beerbuddy.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

public class MainViewActivity extends BeerBuddyActivity implements OnMapReadyCallback {
    public MainViewActivity() {
        super(R.layout.mainview_activity_main, false);
    }

    private GoogleMap mMap;
    private static final String TAG = "MainViewActivity";
    private LatLng location;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        try {
            //get current GPS position
            if (location != null) {
                //move the map to current location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
                List<DrinkingSpot> spots = DAOFactory.getDrinkingSpotDAO(this).getAll(ObjectMapperUtil.getLocationFromLatLang(location));
                Log.i(TAG, "Spots:  " + spots.size());
                for (DrinkingSpot ds : spots) {
                    createMarker(ds);
                }

                final Context context = this;
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        try {
                            long dsid = Long.parseLong(marker.getSnippet());
                            showDrinkingView(DAOFactory.getDrinkingSpotDAO(context).getById(dsid));
                            return true;
                        } catch (BeerBuddyException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }

                });
            }

        } catch (Exception e) {
            e.printStackTrace();

            Log.e(TAG, "Error accured during map initialising ", e);
        }
    }

    public void hideDrinkingView() {
        ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout)).setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    public void showDrinkingView(final DrinkingSpot spot) {
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        slidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");
                hideDrinkingView();

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });
    /* FIXME uncomment an fix
        ((TextView) slidingUpPanelLayout.findViewById(R.id.mainview_agefrom)).setText(spot.getAgeFrom());
        ((TextView) slidingUpPanelLayout.findViewById(R.id.mainview_ageTo)).setText(spot.getAgeTo());
       ((TextView) slidingUpPanelLayout.findViewById(R.id.mainview_creatorname)).setText(spot.getCreator().getUsername());
        ((TextView) slidingUpPanelLayout.findViewById(R.id.mainview_description)).setText(spot.getDescription());
        final Context context = this;
        ((Button) slidingUpPanelLayout.findViewById(R.id.mainview_creatorprofil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the profil
                Intent i = new Intent(context, ViewProfilActivity.class);
                i.putExtra("id", spot.getCreator().getId());
                startActivity(i);
            }
        });

        LinearLayout layout = (LinearLayout) slidingUpPanelLayout.findViewById(R.id.mainview_groupmembers);
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
        ((TextView) slidingUpPanelLayout.findViewById(R.id.mainview_isdrinkingtext)).setText(getString(R.string.mainview_isdrinkinginagroup) + " " + amount);
        */
    }

    private void createMarker(DrinkingSpot ds) {
        LatLng latLng = ObjectMapperUtil.getLatLangFropmGPS(ds.getGps());
        mMap.addMarker(new MarkerOptions().position(latLng).snippet(ds.getId() + "").title(ds.getPersons().get(0).getUsername() + " is drinking with " + ds.getPersons().size() + " others."));
    }


    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {

        //Get the Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            if (DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId() == 0) {
                //send him to the Login
                this.startActivityForResult(new Intent(this, LoginActivity.class), Activity.RESULT_OK);
            } else {
                Log.i(TAG, "user is logged in: " + DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
            }
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during Logincheck ", e);
        }

        //TODO check if called with Extra Value long "id" if called show this drinking spot

        try {
            Intent intent = getIntent();
            if (intent != null && intent.getExtras() != null) {
                Bundle b = intent.getExtras();
                long id = b.getLong("id");
                if (id != 0) {
                    DrinkingSpot drinkingSpot = DAOFactory.getDrinkingSpotDAO(this).getActiveByPersonId(id);
                    showDrinkingView(drinkingSpot);
                    location = ObjectMapperUtil.getLatLangFropmGPS(drinkingSpot.getGps());
                }
            } else {
                location = ObjectMapperUtil.getLatLngFromLocation(DAOFactory.getLocationDAO(this).getCurrentLocation());
                hideDrinkingView();
            }

        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during Location ", e);

        }


    }

}
