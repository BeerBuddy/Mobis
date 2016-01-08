package de.fh_dortmund.beerbuddy_44.test.dao.local;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.test.dao.common.PersonDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class PersonDAOLocalTest extends PersonDAOTest {
    private PersonDAO personDAO;

    public PersonDAOLocalTest() {
        super();
    }



    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAOLocal(getActivity());
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }

}
