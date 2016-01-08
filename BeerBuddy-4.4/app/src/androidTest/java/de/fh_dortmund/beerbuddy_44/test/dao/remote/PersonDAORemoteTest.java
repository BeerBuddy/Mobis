package de.fh_dortmund.beerbuddy_44.test.dao.remote;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.FriendListDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.test.dao.common.FriendListDAOTest;
import de.fh_dortmund.beerbuddy_44.test.dao.common.PersonDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class PersonDAORemoteTest extends PersonDAOTest {
    private PersonDAO personDAO;

    public PersonDAORemoteTest() {
        super();
    }



    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAORemote(getActivity());
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }

}
