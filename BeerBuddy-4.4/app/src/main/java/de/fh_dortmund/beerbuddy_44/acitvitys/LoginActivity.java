package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.listener.android.LoginListener;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;
import lombok.Getter;
import lombok.Setter;

public class LoginActivity extends AppCompatActivity {
    /* Request code used to invoke sign in user interactions. */
    public static final int RC_SIGN_IN = 0;
    private static final String TAG = "LoginActivity";
    /* Is there a ConnectionResult resolution in progress? */
    @Getter
    @Setter
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    @Getter
    @Setter
    private boolean mShouldResolve = false;

    /* Client used to interact with Google APIs. */
    @Getter
    @Setter
    private GoogleApiClient mGoogleApiClient;
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private String lastRequestCacheKey;

    public de.fh_dortmund.beerbuddy.Person getPerson() {
        String email = ((EditText) findViewById(R.id.login_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.login_password)).getText().toString();
        de.fh_dortmund.beerbuddy.Person p = new de.fh_dortmund.beerbuddy.Person();
        p.setPassword(password);
        p.setEmail(email);
        return p;

    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        //        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      //  drawer.setDrawerListener(toggle);
      //  toggle.syncState();

        LoginListener loginListener = new LoginListener(this);

        //init GoogleApi
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(loginListener)
                .addOnConnectionFailedListener(loginListener)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        /* no navigation in Login
        NavigationListener listener =new NavigationListener(this);
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);
        */
        //register Google Login Listener
        findViewById(R.id.sign_in_button).setOnClickListener(loginListener);
        //register login/register Button
        findViewById(R.id.action_login).setOnClickListener(loginListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
            this.sendBroadcast(broadcastIntent);
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
