package de.fh_dortmund.beerbuddy_44.test.dao;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.FriendInvitationDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class LocalFriendInvitationDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private FriendInvitationDAOLocal dao;
    private Person einlader;
    private Person eingeladener;

    public LocalFriendInvitationDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new FriendInvitationDAOLocal(getActivity());
        PersonDAORemote personDAORemote = new PersonDAORemote(getActivity());


        // insert a new einlader
        Person p = new Person();
        String uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        personDAORemote.insertOrUpdate(p);
        einlader = personDAORemote.getByEmail(uniqueEmail);
        p = new Person();
        uniqueEmail = "test@test." + System.currentTimeMillis();
        p.setEmail(uniqueEmail);
        personDAORemote.insertOrUpdate(p);
        eingeladener = personDAORemote.getByEmail(uniqueEmail);


        FriendInvitation di = new FriendInvitation();
        di.setEingeladenerId(eingeladener.getId());
        di.setEinladerId(einlader.getId());

        dao.insertOrUpdate(di);
    }

    public void test_getAllFor() throws Exception {

        List<FriendInvitation> allFor = dao.getAllFor(eingeladener.getId());
        assertEquals(allFor.size(), 1);
        allFor = dao.getAllFor(einlader.getId());
        assertEquals(allFor.size(), 0);
    }

    public void test_getAllFrom() throws Exception {
        List<FriendInvitation> allFor = dao.getAllFrom(einlader.getId());
        assertEquals(allFor.size(), 1);
        allFor = dao.getAllFrom(eingeladener.getId());
        assertEquals(allFor.size(), 0);
    }

}
