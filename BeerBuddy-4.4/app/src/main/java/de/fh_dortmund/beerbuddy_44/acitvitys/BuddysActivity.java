package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.j256.ormlite.spring.DaoFactory;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;

import java.util.List;

import de.fh_dortmund.beerbuddy.FriendInvitation;
import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy.PersonList;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.BuddyListAdapter;
import de.fh_dortmund.beerbuddy_44.adapter.FriendInvitationAdapter;
import de.fh_dortmund.beerbuddy_44.adapter.FriendListAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;
import de.fh_dortmund.beerbuddy_44.listener.rest.ListPersonRequestListener;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;

public class BuddysActivity extends AppCompatActivity {

    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    private String lastRequestCacheKey;
    private static final String TAG = "BuddysActivity";
    private FriendList friendList;
    private List<FriendInvitation> friendInvitations;


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

        //finish instance on Logout
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter.addAction(FriendInvitationAdapter.UPDATE_FRIENDLIST);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setValue();
            }
        }, intentFilter2);

        setContentView(R.layout.buddys_activity_main);
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




        setValue();


    }

    public void setValue()
    {

        try {
        friendList = DAOFactory.getFriendlistDAO(this).getFriendListId(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
        ListView listViewBuddys = (ListView) this.findViewById(R.id.buddys_buddys);
        FriendListAdapter adapter = new FriendListAdapter(this,
                R.layout.buddy_list_row_layout, friendList.getFriends().toArray(new Person[]{}));
        listViewBuddys.setAdapter(adapter);

        //buddys_requests
        ListView listViewRequests = (ListView) this.findViewById(R.id.buddys_requests);
        friendInvitations = DAOFactory.getFriendInvitationDAO(this).getAllFor(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
        FriendInvitationAdapter freindAdapter =new FriendInvitationAdapter(this,  R.layout.buddy_list_row_layout, friendInvitations.toArray(new FriendInvitation[]{}));
        listViewRequests.setAdapter(freindAdapter);
        } catch (BeerBuddyException e) {
            Log.e(TAG, e.getMessage(), e);
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


        return super.onOptionsItemSelected(item);
    }


    private void performRequest() {
        BuddysActivity.this.setProgressBarIndeterminateVisibility(true);

        GetAllPersonsRequest request = new GetAllPersonsRequest();
        lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListPersonRequestListener() {
            @Override
            public void onRequestFailure(SpiceException e) {

            }

            @Override
            public void onRequestSuccess(PersonList listFollowers) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

}
