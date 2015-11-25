package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import java.util.Calendar;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.MissingParameterExcetion;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;
import de.fh_dortmund.beerbuddy_44.listener.android.ViewProfilListener;

public class ViewProfilActivity extends AppCompatActivity {

    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private String lastRequestCacheKey;

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
        setContentView(R.layout.view_profil_activity_main);
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


        //register Navigationb Listener
        NavigationListener listener = new NavigationListener(this);
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);

        Bundle b = getIntent().getExtras();
        long id = b.getLong("id");
        try {
            if (id != 0) {
                Person p = DAOFactory.getPersonDAO(this).getById(id);
                long currentPerson = DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId();
                fillValues(p);

                //register ViewProfilListener
                if(currentPerson == p.getId() &&  DAOFactory.getFreindlistDAO(this).isFriendFromId(currentPerson, p.getId()))
                {
                    ViewProfilListener viewListener = new ViewProfilListener(this, p);
                    ((Button) findViewById(R.id.action_profil_send_request)).setOnClickListener(viewListener);
                }
                else{
                    //hide the Button
                    ((Button) findViewById(R.id.action_profil_send_request)).setVisibility(View.INVISIBLE);
                }



            } else {
                throw new MissingParameterExcetion("Expected a Parameter: long id when calling ViewProfil");
            }
        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }


    }

    private void fillValues(Person p) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(p.getImage(), 0, p.getImage().length);
        ((ImageView) findViewById(R.id.profil_image)).setImageBitmap(bitmap);
        long ageInMillis = System.currentTimeMillis() - p.getDateOfBirth().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ageInMillis);
        ((TextView) findViewById(R.id.profil_alter)).setText(calendar.get(Calendar.YEAR));
        ((TextView) findViewById(R.id.profil_username)).setText(p.getUsername());
        ((TextView) findViewById(R.id.profil_vorlieben)).setText(p.getPrefers());
        ((TextView) findViewById(R.id.profil_interessen)).setText(p.getInterests());
        //TODO gender abhängig von der Sprache setzen
        ((TextView) findViewById(R.id.profil_geschlecht)).setText(p.getGender());
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
