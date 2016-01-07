package de.fh_dortmund.beerbuddy_44.test.dao.local;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Date;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.TestActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class LocalDrinkingSpotDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private DrinkingSpotDAOLocal dao;
    private Person einlader;
    private Person eingeladener;
    private DrinkingSpot drinkingSpot;

    public LocalDrinkingSpotDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new DrinkingSpotDAOLocal(getActivity());
        PersonDAOLocal personDAORemote = new PersonDAOLocal(getActivity());

        // insert a new einlader
        Person p = new Person();
        String uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        personDAORemote.insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(Person person) {
                einlader = person;
            }
        });
        p = new Person();
        uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        personDAORemote.insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(Person person) {
                eingeladener = person;
            }
        });

        DrinkingSpot ds = new DrinkingSpot();
        ds.setActive(true);
        ds.setBeschreibung("test Spot");
        ds.setCreator(einlader);
        ds.setGps("1231;23123");
        ds.setStartTime(new Date(System.currentTimeMillis()));

        dao.insertOrUpdate(ds, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(DrinkingSpot ds) {
                drinkingSpot = ds;
            }
        });

    }

    public void test_join() throws Exception {
        dao.join(drinkingSpot.getId(), eingeladener.getId(), new RequestListener<Void>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(Void aVoid) {

            }
        });
        dao.getById(drinkingSpot.getId(), new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                assertTrue(drinkingSpot.getPersons().contains(eingeladener));
            }
        });

    }

    public void test_getAll() throws Exception {
        dao.getAll(new RequestListener<DrinkingSpot[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(DrinkingSpot[] drinkingSpots) {
                assertTrue(drinkingSpots.length > 0);
            }
        });

    }

}
