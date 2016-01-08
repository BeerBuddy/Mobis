package de.fh_dortmund.beerbuddy_44.test.dao.local;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.FriendListDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.test.dao.common.FriendListDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class FriendListDAOLocalTest extends FriendListDAOTest {
    private FriendListDAO friendListDAO;
    private PersonDAO personDAO;

    public FriendListDAOLocalTest() {
        super();
    }



    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAOLocal(getActivity());
        friendListDAO = new FriendListDAOLocal(getActivity());
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }
    @Override
    public FriendListDAO getFriendListDAO() {
        return friendListDAO;
    }

}
