package de.fh_dortmund.beerbuddy_44.test.dao.sync;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.DrinkingSpotDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.PersonDAOSync;
import de.fh_dortmund.beerbuddy_44.test.dao.common.DrinkingSpotDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class DrinkingSpotDAOSyncTest extends DrinkingSpotDAOTest {
    private  DrinkingSpotDAO drinkingSpotDAO;
    private  PersonDAO personDAO;

    public DrinkingSpotDAOSyncTest() {
        super();

    }

    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAOSync(getActivity());
        drinkingSpotDAO = new DrinkingSpotDAOSync(getActivity());
    }

    public DrinkingSpotDAO getDrinkingSpotDAO() {
        return drinkingSpotDAO;
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }
}
