package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Date;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.IntentUtil;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;

public class MainViewActivity extends BeerBuddyActivity implements OnMapReadyCallback {
    private static final String TAG = "MainViewActivity";
    private SlidingUpPanelLayout.PanelState defaultState;
    private LatLng location;

    public MainViewActivity() {
        super(R.layout.mainview_activity_main, true);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        try {
            final BeerBuddyActivity context = this;
            DAOFactory.getDrinkingSpotDAO(this).getAll(new RequestListener<DrinkingSpot[]>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {

                }

                @Override
                public void onRequestSuccess(DrinkingSpot[] drinkingSpots) {
                    Log.i(TAG, "Spots:  " + drinkingSpots.length);
                    for (DrinkingSpot ds : drinkingSpots) {
                        createMarker(ds, googleMap);
                    }


                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            long dsid = Long.parseLong(marker.getSnippet());
                            DAOFactory.getDrinkingSpotDAO(context).getById(dsid, new RequestListener<DrinkingSpot>() {
                                @Override
                                public void onRequestFailure(SpiceException spiceException) {

                                }

                                @Override
                                public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                                    showDrinkingView(drinkingSpot);
                                }
                            });
                            return true;
                        }

                    });

                }
            });


            //get current GPS position
            if (location != null) {
                //move the map to current location
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }

        } catch (Exception e) {
            e.printStackTrace();

            Log.e(TAG, "Error accured during map initialising ", e);
        }
    }

    @Override
    public void onBackPressed() {

        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if (!slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.HIDDEN)) {
            hideDrinkingView();
        } else {
            super.onBackPressed();
        }
    }

    public void hideDrinkingView() {
        SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //maybe this should help
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        // ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout)).setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
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
        final Context context = this;
        int totalAmount = spot.getTotalAmount() + 1 + spot.getPersons().size();
        int ageFrom = spot.getAgeFrom();
        int ageTo = spot.getAgeTo();

        Date dateOfBirth = spot.getCreator().getDateOfBirth();
        if(dateOfBirth != null)
        {
            if (ageFrom > ObjectMapperUtil.getAgeFromBirthday((dateOfBirth)) || (ageFrom == 0)){
                ageFrom = ObjectMapperUtil.getAgeFromBirthday(dateOfBirth);
            }

            if (ageTo < ObjectMapperUtil.getAgeFromBirthday((dateOfBirth))){
                ageTo = ObjectMapperUtil.getAgeFromBirthday(dateOfBirth);
            }
        }

        List<Person> persons = spot.getPersons();
        for (Person p : persons) {
            Date dateOfBirth1 = p.getDateOfBirth();
            if (dateOfBirth1 != null) {
                int pAge = ObjectMapperUtil.getAgeFromBirthday(dateOfBirth1);
                if (pAge < ageFrom) {
                    ageFrom = pAge;
                }
                if (pAge > ageTo) {
                    ageTo = pAge;
                }
            }

        }


        ((TextView) slidingUpPanelLayout.findViewById(R.id.mainview_group)).setText(getResources().getText(R.string.mainview_person_count).toString() + " " + totalAmount + " " + getResources().getText(R.string.mainview_age).toString() + " " + ageFrom + " - " + ageTo);
        slidingUpPanelLayout.findViewById(R.id.mainview_view).setOnClickListener(new IntentUtil.ShowDrinkingSpotListener(context, spot.getId(), true));
        slidingUpPanelLayout.findViewById(R.id.mainview_navigate).setOnClickListener(new IntentUtil.ShowDrinkingSpotOnGoogleMapListener(context, spot));
        ((TextView) slidingUpPanelLayout.findViewById(R.id.mainview_name)).setText(spot.getCreator().getUsername() + " is drinking");
    }


    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {
        this.defaultState = ((SlidingUpPanelLayout) findViewById(R.id.sliding_layout)).getPanelState();
        //Get the Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            Intent intent = getIntent();
            if (intent != null && intent.getExtras() != null) {
                Bundle b = intent.getExtras();
                long id = b.getLong("id");
                if (id != 0) {
                    DAOFactory.getDrinkingSpotDAO(this).getById(id, new RequestListener<DrinkingSpot>() {
                        @Override
                        public void onRequestFailure(SpiceException spiceException) {

                        }

                        @Override
                        public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                            showDrinkingView(drinkingSpot);
                            location = ObjectMapperUtil.getLatLangFropmGPS(drinkingSpot.getGps());
                        }
                    });
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
