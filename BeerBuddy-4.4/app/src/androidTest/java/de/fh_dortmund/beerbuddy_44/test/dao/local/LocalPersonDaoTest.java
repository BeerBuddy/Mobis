package de.fh_dortmund.beerbuddy_44.test.dao.local;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import junit.framework.Assert;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.TestActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class LocalPersonDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private PersonDAOLocal dao;

    public LocalPersonDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new PersonDAOLocal(getActivity());
        Person p = new Person();
        p.setEmail("test@test.de");
        p.setPassword("password");
        dao.insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                fail(e.getMessage());
            }

            @Override
            public void onRequestSuccess(Person person) {

            }
        });
    }

    public void test_getAll() throws Exception {
        dao.getAll(new RequestListener<Person[]>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                fail(e.getMessage());
            }

            @Override
            public void onRequestSuccess(Person[] persons) {
                assertTrue(persons.length > 0);
            }
        });

    }

    public void test_getByEmail() throws Exception {
        dao.getByEmail("test@test.de", new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                fail(e.getMessage());
            }

            @Override
            public void onRequestSuccess(Person person) {
                assertNotNull(person);
            }
        });
    }

    public void test_getById() throws Exception {
        dao.getAll(new RequestListener<Person[]>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                fail(e.getMessage());
            }

            @Override
            public void onRequestSuccess(final Person[] persons) {
                dao.getById(persons[0].getId(), new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException e) {
                        fail(e.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(Person person) {
                        Assert.assertEquals(person, persons[0]);
                    }
                });

            }
        });

    }
}
