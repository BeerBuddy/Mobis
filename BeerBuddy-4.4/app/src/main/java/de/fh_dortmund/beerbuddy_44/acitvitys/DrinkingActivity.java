package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.listener.android.NavigationListener;

/**
 * Created by David on 01.11.2015.
 */
public class DrinkingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drinking_main);

        //register Navigationb Listener
        NavigationListener listener =new NavigationListener(this);
        NavigationView navigationView = (NavigationView) this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(listener);
    }
}