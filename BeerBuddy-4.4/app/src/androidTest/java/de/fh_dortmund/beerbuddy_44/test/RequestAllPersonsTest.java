package de.fh_dortmund.beerbuddy_44.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class RequestAllPersonsTest extends InstrumentationTestCase {

    private GetAllPersonsRequest request;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = new GetAllPersonsRequest();
        JacksonSpringAndroidSpiceService service = new JacksonSpringAndroidSpiceService();
        request.setRestTemplate(service.createRestTemplate());
    }

    public void test_loadDataFromNetwork() throws Exception {
        Person[] persons = request.loadDataFromNetwork();
        for(Person p:persons){
            Log.i("BuddysActivity",p.toString());
        }

        assertTrue( persons.length > 0);
    }
}
