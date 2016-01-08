package de.fh_dortmund.beerbuddy_44.test.dao.remote;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Date;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.TestActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendListDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class RemoteFriendListDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private FriendListDAORemote dao ;
    private Person einlader;
    private Person eingeladener;
    private DrinkingSpot drinkingSpot;

    public RemoteFriendListDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new FriendListDAORemote(getActivity());
        PersonDAORemote personDAORemote = new PersonDAORemote(getActivity());
        DrinkingSpotDAO dsDao = new DrinkingSpotDAORemote(getActivity());

        // insert a new einlader
        Person p = new Person();
        String uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        p.setPassword(uniqueEmail);
        personDAORemote.insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(Person person) {
                einlader =person;
            }
        });
        p = new Person();
        uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        p.setPassword(uniqueEmail);
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

        dsDao.insertOrUpdate(ds, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(DrinkingSpot ds) {
                drinkingSpot = ds;
            }
        });


        FriendList di = new FriendList();
        di.setPersonid(einlader.getId());
        di.getFriends().add(eingeladener);
        dao.insertOrUpdate(di, new RequestListener<FriendList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(FriendList friendList) {

            }
        });
    }

    public void test_isFriend() throws Exception {
        dao.isFriendFromId(einlader.getId(), eingeladener.getId(), new RequestListener<Boolean>() {
           @Override
           public void onRequestFailure(SpiceException spiceException) {

           }

           @Override
           public void onRequestSuccess(Boolean aBoolean) {
               assertTrue(aBoolean);
           }
       });
        //rnd number
       dao.isFriendFromId(einlader.getId(), 32423423, new RequestListener<Boolean>() {
           @Override
           public void onRequestFailure(SpiceException spiceException) {

           }

           @Override
           public void onRequestSuccess(Boolean aBoolean) {
               assertFalse(aBoolean);
           }
       });
    }

    public void test_getAllFrom() throws Exception {
        dao.getFriendList(einlader.getId(), new RequestListener<FriendList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(FriendList friendList) {
                assertEquals(1,friendList.getFriends().size());
            }
        });

    }

}
