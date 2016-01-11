package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.BuddysActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.DrinkingActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.DrinkingInvitationActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.EditProfilActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.ImprintActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.LoginActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by David on 28.10.2015.
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    @NonNull
    private BeerBuddyActivity activity;
    private static final String TAG = "NavigationListener";

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = null;
        switch(id)
        {
            case R.id.action_imdrinking:
                i = new Intent(activity, DrinkingActivity.class);
                break;
            case R.id.action_profil:
                i = new Intent(activity, EditProfilActivity.class);
                break;
            case R.id.action_freundesliste:
                i = new Intent(activity, BuddysActivity.class);
                break;
            case R.id.action_main:
                i = new Intent(activity, MainViewActivity.class);
                break;
            case R.id.action_message:
                i = new Intent(activity, DrinkingInvitationActivity.class);
                break;
            case R.id.action_logout:
                logout(i);
                break;
            case R.id.action_impressum:
                i = new Intent(activity, ImprintActivity.class);
                break;
            default:
                Log.e(TAG, "No action defined for Button: "+id);
                break;

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
            activity.startActivityForResult(new Intent(activity, LoginActivity.class), Activity.RESULT_OK);
            i = new Intent(activity, MainViewActivity.class);
        } catch (BeerBuddyException e) {
            Log.e(TAG, "Could not logout ", e);
        }
        return i;
    }
}
