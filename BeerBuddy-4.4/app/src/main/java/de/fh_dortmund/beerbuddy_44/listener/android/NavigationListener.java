package de.fh_dortmund.beerbuddy_44.listener.android;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import de.fh_dortmund.beerbuddy_44.R;

/**
 * Created by David on 28.10.2015.
 */
public class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

    private Activity activity;

    public NavigationListener(Activity activity)
    {
        this.activity = activity;


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
