package de.fh_dortmund.beerbuddy_44.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;

import org.codehaus.jackson.map.ObjectMapper;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;
import de.fh_dortmund.beerbuddy_44.requests.SavePersonRequest;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class SaveSimplePersonTest extends InstrumentationTestCase {

    private SavePersonRequest request;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Person p = new Person();
        p.setEmail("adasd@asd.de");
        p.setPassword("asdasddas");
        request = new SavePersonRequest(p);
        JacksonSpringAndroidSpiceService service = new JacksonSpringAndroidSpiceService();
        request.setRestTemplate(service.createRestTemplate());
        Log.i("BuddysActivity", request.toString());
        Log.i("BuddysActivity", request.getRestTemplate().toString());
        Log.i("BuddysActivity", new ObjectMapper().writeValueAsString(p));
    }

    public void test_loadDataFromNetwork() throws Exception {
        Person persons = request.loadDataFromNetwork();
            Log.i("BuddysActivity",persons.toString());

        assertEquals(persons.getEmail(),"adasd@asd.de");
    }
}
