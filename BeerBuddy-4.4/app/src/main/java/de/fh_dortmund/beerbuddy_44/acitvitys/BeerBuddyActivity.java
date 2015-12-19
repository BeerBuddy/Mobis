package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;

/**
 * Created by David on 19.12.2015.
 */
public abstract class BeerBuddyActivity extends AppCompatActivity {


    private final int layout;
    private final boolean finishOnLogout;

    public BeerBuddyActivity(int layout, boolean finishOnLogout)
    {
        this.layout = layout;
        this.finishOnLogout =finishOnLogout;
    }

    //protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(finishOnLogout)
        {
            //finish instance on Logout
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    finish();
                }
            }, intentFilter);
        }

        setContentView(layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        //register Navigationb Listener
        NavigationListener listener = new NavigationListener(this);
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);

        onFurtherCreate(savedInstanceState);
    }

    protected abstract void onFurtherCreate(Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();
      //  spiceManager.start(this);
    }

    @Override
    protected void onStop() {
       // spiceManager.shouldStop();
        super.onStop();
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }


}
