package de.fh_dortmund.beerbuddy_44.test.dao.common;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;
import java.util.Date;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by grimm on 08.01.2016.
 */
public abstract class PersonDAOTest extends ActivityInstrumentationTestCase2<MainViewActivity> {


    private boolean testDone;

    public PersonDAOTest() {
        super(MainViewActivity.class);
    }


    public abstract PersonDAO getPersonDAO();

    public void test_Insert() {

        Person p = new Person();
        final String mail = "test@Insert" + System.currentTimeMillis() + ".de";
        p.setEmail(mail);
        p.setPassword("test_Insert_Get1");

        getPersonDAO().insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                testDone = true;
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(final Person p1) {
                assertTrue(p1.getId() != 0);
                assertTrue(p1.getId() != -1);

                assertEquals(mail, p1.getEmail());
                getPersonDAO().getByEmail(p1.getEmail(), new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        testDone = true;
                        fail(spiceException.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(Person person) {
                        testDone = true;
                        assertEquals(p1, person);
                    }
                });
                getPersonDAO().getById(p1.getId(), new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        testDone = true;
                        fail(spiceException.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(Person person) {
                        testDone = true;
                        assertEquals(p1, person);
                    }
                });
                getPersonDAO().getAll(new RequestListener<Person[]>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        testDone = true;
                        fail(spiceException.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(Person[] persons) {
                        testDone = true;
                        for (Person p : persons) {
                            Log.i("BeerBuddyTest", p.toString());
                        }
                        assertTrue(Arrays.asList(persons).contains(p1));
                    }
                });
            }
        });
        while (!testDone) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
