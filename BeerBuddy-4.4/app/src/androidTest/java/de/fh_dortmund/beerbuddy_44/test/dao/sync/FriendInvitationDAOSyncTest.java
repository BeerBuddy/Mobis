package de.fh_dortmund.beerbuddy_44.test.dao.sync;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendListDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.FriendInvitationDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.FriendListDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.PersonDAOSync;
import de.fh_dortmund.beerbuddy_44.test.dao.common.FriendInvitationDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class FriendInvitationDAOSyncTest extends FriendInvitationDAOTest {
    private FriendInvitationDAO friendInvitationDAO;
    private FriendListDAO friendListDAO;
    private PersonDAO personDAO;

    public FriendInvitationDAOSyncTest() {
        super();

    }



    @Override
    public void setUp() throws Exception {
        friendInvitationDAO = new FriendInvitationDAOSync(getActivity());
        personDAO = new PersonDAOSync(getActivity());
        friendListDAO = new FriendListDAOSync(getActivity()) {
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
