package de.fh_dortmund.beerbuddy_44.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import de.fh_dortmund.beerbuddy_44.ServerUtil;
import de.fh_dortmund.beerbuddy_44.acitvitys.BuddysActivity;

/**
 * Created by grimm on 30.10.2015.
 */
@LargeTest
public class SimpleTest extends InstrumentationTestCase {

    public void test_Log()
    {
        Log.i("BuddysActivity", "Asd");
    }

    public void test_HostIP() {
        Log.i("BuddysActivity", ServerUtil.getHost());
        assertFalse(ServerUtil.getHost().equals("http://null:9000"));
    }
}
