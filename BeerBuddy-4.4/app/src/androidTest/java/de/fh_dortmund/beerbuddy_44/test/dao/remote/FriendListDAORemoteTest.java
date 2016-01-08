package de.fh_dortmund.beerbuddy_44.test.dao.remote;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendListDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.test.dao.common.FriendInvitationDAOTest;
import de.fh_dortmund.beerbuddy_44.test.dao.common.FriendListDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class FriendListDAORemoteTest extends FriendListDAOTest {
    private FriendListDAO friendListDAO;
    private PersonDAO personDAO;

    public FriendListDAORemoteTest() {
        super();
    }



    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAORemote(getActivity());
        friendListDAO = new FriendListDAORemote(getActivity());
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }
    @Override
    public FriendListDAO getFriendListDAO() {
        return friendListDAO;
    }

}
