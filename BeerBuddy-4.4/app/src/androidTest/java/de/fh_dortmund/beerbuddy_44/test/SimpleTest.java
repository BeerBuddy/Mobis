package de.fh_dortmund.beerbuddy_44.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import de.fh_dortmund.beerbuddy_44.acitvitys.BuddysActivity;

/**
 * Created by grimm on 30.10.2015.
 */
public class SimpleTest extends ActivityInstrumentationTestCase2{


    public SimpleTest(Class<BuddysActivity> activityClass) {
        super(activityClass);
    }

    public void test()
    {
        Log.i("BuddysActivity", "Asd");
    }

}
