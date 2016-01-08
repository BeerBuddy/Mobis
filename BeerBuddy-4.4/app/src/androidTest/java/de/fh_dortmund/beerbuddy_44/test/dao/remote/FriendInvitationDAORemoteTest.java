package de.fh_dortmund.beerbuddy_44.test.dao.remote;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendListDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.test.dao.common.DrinkingInvitationDAOTest;
import de.fh_dortmund.beerbuddy_44.test.dao.common.FriendInvitationDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class FriendInvitationDAORemoteTest extends FriendInvitationDAOTest {
    private FriendInvitationDAO friendInvitationDAO;
    private FriendListDAO friendListDAO;
    private PersonDAO personDAO;

    public FriendInvitationDAORemoteTest() {
        super();

    }



    @Override
    public void setUp() throws Exception {
        friendInvitationDAO = new FriendInvitationDAORemote(getActivity());
        personDAO = new PersonDAORemote(getActivity());
        friendListDAO = new FriendListDAORemote(getActivity()) {
        };
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }
    @Override
    public FriendListDAO getFriendListDAO() {
        return friendListDAO;
    }

    @Override
    public FriendInvitationDAO getFriendInvitationDAO() {
        return friendInvitationDAO;
    }
}
