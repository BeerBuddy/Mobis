package de.fh_dortmund.beerbuddy_44.test.dao;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.Date;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class LocalDrinkingSpotDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private DrinkingSpotDAO dao ;
    private Person einlader;
    private Person eingeladener;
    private DrinkingSpot drinkingSpot;

    public LocalDrinkingSpotDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new DrinkingSpotDAORemote(getActivity());
        PersonDAOLocal personDAORemote = new PersonDAOLocal(getActivity());

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

        dao.insertOrUpdate(ds);
        drinkingSpot = dao.getActiveByPersonId(einlader.getId());


    }

    public void test_join() throws Exception {
        dao.join(drinkingSpot.getId(), eingeladener.getId());
        assertTrue(dao.getById(drinkingSpot.getId()).getPersons().contains(eingeladener));
    }

    public void test_getAll() throws Exception {
        List<DrinkingSpot> all = dao.getAll();
        assertTrue(all.size() > 0);
    }

}
