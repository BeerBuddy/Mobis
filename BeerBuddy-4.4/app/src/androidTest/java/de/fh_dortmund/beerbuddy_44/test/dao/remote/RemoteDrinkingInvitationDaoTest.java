package de.fh_dortmund.beerbuddy_44.test.dao.remote;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Date;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.TestActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class RemoteDrinkingInvitationDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private DrinkingInvitationDAORemote dao;
    private Person einlader;
    private Person eingeladener;
    private DrinkingSpot drinkingSpot;

    public RemoteDrinkingInvitationDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new DrinkingInvitationDAORemote(getActivity());
        PersonDAORemote personDAORemote = new PersonDAORemote(getActivity());
        final DrinkingSpotDAO dsDao = new DrinkingSpotDAORemote(getActivity());


        // insert a new einlader
        eingeladener = new Person();
        String uniqueEmail = "test@test." + System.currentTimeMillis();
        eingeladener.setEmail(uniqueEmail);
        eingeladener.setPassword(uniqueEmail);
        personDAORemote.insertOrUpdate(eingeladener, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(Person person) {
                eingeladener = person;
            }
        });
        einlader = new Person();
        uniqueEmail = "test@test." + System.currentTimeMillis();
        einlader.setEmail(uniqueEmail);
        einlader.setPassword(uniqueEmail);
        personDAORemote.insertOrUpdate(einlader, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(Person person) {
                einlader = person;
                DrinkingSpot ds = new DrinkingSpot();
                ds.setActive(true);
                ds.setBeschreibung("test Spot");
                ds.setCreator(eingeladener);
                ds.setGps("1231;23123");
                ds.setStartTime(new Date(System.currentTimeMillis()));

                dsDao.insertOrUpdate(ds, new RequestListener<DrinkingSpot>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {

                    }

                    @Override
                    public void onRequestSuccess(DrinkingSpot ds) {
                        drinkingSpot = ds;
                        DrinkingInvitation di = new DrinkingInvitation();
                        di.setEingeladenerId(eingeladener.getId());
                        di.setEinladerId(einlader.getId());
                        di.setDrinkingSpotId(drinkingSpot.getId());

                        dao.insertOrUpdate(di, new RequestListener<DrinkingInvitation>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {

                            }

                            @Override
                            public void onRequestSuccess(DrinkingInvitation ds) {

                            }
                        });
                    }
                });
            }
        });


    }

    public void test_getAllFor() throws Exception {

        dao.getAllFor(eingeladener.getId(), new RequestListener<DrinkingInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                assertEquals(drinkingInvitations.length, 1);

            }
        });

        dao.getAllFor(einlader.getId(), new RequestListener<DrinkingInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                assertEquals(drinkingInvitations.length, 0);
            }
        });


    }

    public void test_getAllFrom() throws Exception {
        dao.getAllFrom(eingeladener.getId(), new RequestListener<DrinkingInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                assertEquals(drinkingInvitations.length, 0);

            }
        });

        dao.getAllFrom(einlader.getId(), new RequestListener<DrinkingInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(DrinkingInvitation[] drinkingInvitations) {
                assertEquals(drinkingInvitations.length, 1);
            }
        });

    }

}
