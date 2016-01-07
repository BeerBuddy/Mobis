package de.fh_dortmund.beerbuddy_44.test.dao;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.Date;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.FriendListDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class LocalFriendListDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private FriendListDAOLocal dao ;
    private Person einlader;
    private Person eingeladener;
    private DrinkingSpot drinkingSpot;

    public LocalFriendListDaoTest() {
        super(MainViewActivity.class);
    }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new FriendListDAOLocal(getActivity());
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


        FriendList di = new FriendList();
        di.setPersonid(einlader.getId());
        di.getFriends().add(eingeladener);
        dao.insertOrUpdate(di);
    }

    public void test_isFriend() throws Exception {
       assertTrue( dao.isFriendFromId(einlader.getId(), eingeladener.getId()));
        //rnd number
       assertFalse(dao.isFriendFromId(einlader.getId(), 32423423));
    }

    public void test_getAllFrom() throws Exception {
       assertEquals(1, dao.getFriendList(einlader.getId()).getFriends().size());
    }

}
