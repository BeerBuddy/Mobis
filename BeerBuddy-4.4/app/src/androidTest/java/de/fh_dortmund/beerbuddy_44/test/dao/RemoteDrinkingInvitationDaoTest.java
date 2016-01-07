package de.fh_dortmund.beerbuddy_44.test.dao;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.Date;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class RemoteDrinkingInvitationDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private DrinkingInvitationDAORemote dao ;
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
        DrinkingSpotDAO dsDao = new DrinkingSpotDAORemote(getActivity());



        // insert a new einlader
        Person p = new Person();
        String uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        personDAORemote.insertOrUpdate(p);
        einlader =personDAORemote.getByEmail(uniqueEmail);
        p = new Person();
        uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        personDAORemote.insertOrUpdate(p);
        eingeladener =personDAORemote.getByEmail(uniqueEmail);

        DrinkingSpot ds = new DrinkingSpot();
        ds.setActive(true);
        ds.setBeschreibung("test Spot");
        ds.setCreator(einlader);
        ds.setGps("1231;23123");
        ds.setStartTime(new Date(System.currentTimeMillis()));

        dsDao.insertOrUpdate(ds);
        drinkingSpot = dsDao.getActiveByPersonId(einlader.getId());


        DrinkingInvitation di = new DrinkingInvitation();
        di.setEingeladenerId(eingeladener.getId());
        di.setEinladerId(einlader.getId());
        di.setDrinkingSpotId(drinkingSpot.getId());

        dao.insertOrUpdate(di);
    }

    public void test_getAllFor() throws Exception {

        List<DrinkingInvitation> allFor = dao.getAllFor(eingeladener.getId());
        assertEquals(allFor.size(), 1);
         allFor = dao.getAllFor(einlader.getId());
        assertEquals(allFor.size(), 0);
    }

    public void test_getAllFrom() throws Exception {


        List<DrinkingInvitation> allFor = dao.getAllFrom(einlader.getId());
        assertEquals(allFor.size(), 1);
       allFor = dao.getAllFrom(eingeladener.getId());
        assertEquals(allFor.size(), 0);
    }

}
