package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.support.v7.app.AppCompatActivity;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by David on 06.01.2016.
 */
public class TestActivity extends AppCompatActivity {
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);


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
}
