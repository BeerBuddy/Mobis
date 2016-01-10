package de.fh_dortmund.beerbuddy_44.test.dao.common;

import android.test.ActivityInstrumentationTestCase2;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by grimm on 08.01.2016.
 */
public abstract class FriendListDAOTest extends ActivityInstrumentationTestCase2<MainViewActivity> {


    private boolean testDone;

    public FriendListDAOTest() {
        super(MainViewActivity.class);
    }
    public abstract PersonDAO getPersonDAO();

    public abstract FriendListDAO getFriendListDAO();

    public void test_Insert() {

        Person p = new Person();
        p.setEmail("test@Insert.de");
        p.setPassword("test_Insert_Get1");

        getPersonDAO().insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                testDone = true;
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(final Person p1) {
                final FriendList fl = new FriendList();
                fl.setPersonid(p1.getId());
                getFriendListDAO().insertOrUpdate(fl, new RequestListener<FriendList>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        testDone = true;
                        fail(spiceException.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(final FriendList friendList) {
                        assertTrue(friendList.getId() != 0);
                        assertTrue(friendList.getId() != -1);
                        assertEquals(friendList.getPersonid(), p1.getId());
                        getFriendListDAO().getFriendList(p1.getId(), new RequestListener<FriendList>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                testDone = true;
                                fail(spiceException.getMessage());
                            }

                            @Override
                            public void onRequestSuccess(FriendList fList) {
                                assertEquals(friendList,fList);
                                testDone = true;
                            }
                        });
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
