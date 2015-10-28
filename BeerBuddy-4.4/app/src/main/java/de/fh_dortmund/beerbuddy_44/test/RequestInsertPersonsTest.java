package de.fh_dortmund.beerbuddy_44.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.requests.InsertPersonRequest;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class RequestInsertPersonsTest extends InstrumentationTestCase {

    private InsertPersonRequest request;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = new InsertPersonRequest(new Person("David","Grimm"));
    }

    public void test_loadDataFromNetwork() throws Exception {
        request.loadDataFromNetwork();
    }
}
