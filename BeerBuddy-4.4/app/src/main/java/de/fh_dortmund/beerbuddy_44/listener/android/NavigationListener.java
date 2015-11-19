package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.BuddysActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.EditProfilActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.LoginActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by David on 28.10.2015.
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    @NonNull
    private Activity activity;
    private static final String TAG = "NavigationListener";

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = null;
        if (id == R.id.action_main) {
            i = new Intent(activity, MainViewActivity.class);
            // Handle the camera action
        } else if (id == R.id.action_logout) {
            i = logout(i);
        } else if (id == R.id.action_freundesliste) {
            i = new Intent(activity, BuddysActivity.class);
        } else if (id == R.id.action_impressum) {

        } else if (id == R.id.action_profil) {
            i = new Intent(activity, EditProfilActivity.class);
        }
        if(i!= null)
        {
            activity.startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Intent logout(Intent i) {
        try {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("de.fh_dortmund.beerbuddy_44.ACTION_LOGOUT");
            activity.sendBroadcast(broadcastIntent);
            DAOFactory.getCurrentPersonDAO(activity).deleteCurrentPerson();
            i = new Intent(activity, LoginActivity.class);
        } catch (BeerBuddyException e) {
            Log.e(TAG, "Could not logout ", e);
        }
        return i;
    }
}
