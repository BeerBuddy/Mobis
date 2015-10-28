package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.BuddysActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.EditProfilActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.LoginActivity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by David on 28.10.2015.
 */
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    @NonNull
    private Activity activity;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = null;
        if (id == R.id.action_login) {
            i = new Intent(activity, LoginActivity.class);
            // Handle the camera action
        } else if (id == R.id.action_logout) {
            i = new Intent(activity, LoginActivity.class);
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
}
