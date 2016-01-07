package de.fh_dortmund.beerbuddy_44.test.dao.remote;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.acitvitys.TestActivity;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
public class RemoteFriendInvitationDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private FriendInvitationDAORemote dao;
    private Person einlader;
    private Person eingeladener;

    public RemoteFriendInvitationDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new FriendInvitationDAORemote(getActivity());
        PersonDAORemote personDAORemote = new PersonDAORemote(getActivity());


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


        FriendInvitation di = new FriendInvitation();
        di.setEingeladenerId(eingeladener.getId());
        di.setEinladerId(einlader.getId());

        dao.insertOrUpdate(di, new RequestListener<FriendInvitation>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(FriendInvitation friendInvitation) {

            }
        });
    }

    public void test_getAllFor() throws Exception {

         dao.getAllFor(eingeladener.getId(), new RequestListener<FriendInvitation[]>() {
             @Override
             public void onRequestFailure(SpiceException spiceException) {

             }

             @Override
             public void onRequestSuccess(FriendInvitation[] friendInvitations) {
                 assertEquals(friendInvitations.length, 1);
             }
         });

        dao.getAllFor(einlader.getId(), new RequestListener<FriendInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(FriendInvitation[] friendInvitations) {
                assertEquals(friendInvitations.length, 0);
            }
        });

    }

    public void test_getAllFrom() throws Exception {
        dao.getAllFrom(eingeladener.getId(), new RequestListener<FriendInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(FriendInvitation[] friendInvitations) {
                assertEquals(friendInvitations.length, 0);
            }
        });

        dao.getAllFrom(einlader.getId(), new RequestListener<FriendInvitation[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(FriendInvitation[] friendInvitations) {
                assertEquals(friendInvitations.length, 1);
            }
        });

    }

}
