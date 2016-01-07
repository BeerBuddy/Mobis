package de.fh_dortmund.beerbuddy_44.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;
import de.fh_dortmund.beerbuddy_44.requests.SavePersonRequest;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class RequestInsertPersonsTest extends InstrumentationTestCase {

    private SavePersonRequest request;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Person p = new Person();
        p.setEmail("asd@asd.de");
        p.setUsername("asd");
        request = new SavePersonRequest(p);
        JacksonSpringAndroidSpiceService service = new JacksonSpringAndroidSpiceService();
        request.setRestTemplate(service.createRestTemplate());
    }

    public void test_loadDataFromNetwork() throws Exception {
        request.loadDataFromNetwork();
    }
}