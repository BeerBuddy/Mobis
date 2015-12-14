package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;

import java.util.List;

import de.fh_dortmund.beerbuddy.DrinkingSpot;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy.PersonList;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;
import de.fh_dortmund.beerbuddy_44.listener.rest.ListPersonRequestListener;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;

public class MainViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private String lastRequestCacheKey;
    private static final String TAG = "MainViewActivity";
    private LatLng location;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        try {
            //move the map to current location
            if(location != null)
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
            }

            List<DrinkingSpot> spots = DAOFactory.getDrinkingSpotDAO(this).getAll(ObjectMapperUtil.getLocationFromLatLang(location));
            Log.i(TAG, "Spots:  " + spots.size());
            for (DrinkingSpot ds : spots) {
                createMarker(ds);
            }
            final Context context = this;

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String drinkingSpotId = marker.getSnippet();
                    long drinkingId = Long.parseLong(drinkingSpotId);
                    try {
                        showDrinkingView(DAOFactory.getDrinkingSpotDAO(context).getActiveById(drinkingId));
                    } catch (BeerBuddyException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error accured during get DrinkingSpot ", e);
                        return false;
                    }

                    return true;
                }

            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    hideDrinkingView();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during map initialising ", e);
        }
    }

    public void hideDrinkingView() {
        findViewById(R.id.mainview_slidingpanel).setVisibility(View.GONE);
    }

    public void showDrinkingView(final DrinkingSpot spot) {
        findViewById(R.id.mainview_slidingpanel).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.mainview_agefrom)).setText(spot.getAgeFrom());
        ((TextView) findViewById(R.id.mainview_ageTo)).setText(spot.getAgeTo());
        ((TextView) findViewById(R.id.mainview_creatorname)).setText(spot.getCreator().getUsername());
        ((TextView) findViewById(R.id.mainview_description)).setText(spot.getDescription());
        final Context context = this;
        ((Button) findViewById(R.id.mainview_creatorprofil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the profil
                Intent i = new Intent(context, ViewProfilActivity.class);
                i.putExtra("id", spot.getCreator().getId());
                startActivity(i);
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.mainview_groupmembers);
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
        ((TextView) findViewById(R.id.mainview_isdrinkingtext)).setText(getString(R.string.mainview_isdrinkinginagroup) + " " + amount);
    }

    private void createMarker(DrinkingSpot ds) {
        LatLng latLng = ObjectMapperUtil.getLatLangFropmGPS(ds.getGps());
        mMap.addMarker(new MarkerOptions().position(latLng).snippet(ds.getId() + "").title(ds.getPersons().get(0).getUsername() + " is drinking with " + ds.getPersons().size() + " others."));
    }


    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //finish instance on Logout
       /* IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);
    */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Get the Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //register Navigationb Listener
        NavigationListener listener = new NavigationListener(this);
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);

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
        if(intent != null && intent.getExtras() != null)
        {
            Bundle b = intent.getExtras();
            long id = b.getLong("id");
            if (id != 0) {
                DrinkingSpot drinkingSpot = DAOFactory.getDrinkingSpotDAO(this).getActiveById(id);
                showDrinkingView(drinkingSpot);
                location = ObjectMapperUtil.getLatLangFropmGPS(drinkingSpot.getGps());
            }
        }
        else {
            location = ObjectMapperUtil.getLatLngFromLocation(DAOFactory.getLocationDAO(this).getCurrentLocation());
            hideDrinkingView();
        }


        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during Location ", e);

        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }
}
