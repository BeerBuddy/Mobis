package de.fh_dortmund.beerbuddy_44.test.dao.sync;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.PersonDAOSync;
import de.fh_dortmund.beerbuddy_44.test.dao.common.PersonDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class PersonDAOSyncTest extends PersonDAOTest {
    private PersonDAO personDAO;

    public PersonDAOSyncTest() {
        super();
    }



    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAOSync(getActivity());
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }

}
