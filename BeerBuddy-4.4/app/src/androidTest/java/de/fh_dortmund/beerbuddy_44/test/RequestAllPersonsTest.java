package de.fh_dortmund.beerbuddy_44.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import de.fh_dortmund.beerbuddy.entities.PersonList;
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
    }

    public void test_loadDataFromNetwork() throws Exception {
        PersonList persons = request.loadDataFromNetwork();
        System.out.println(persons);
        assertEquals(0,persons.size());
    }
}
