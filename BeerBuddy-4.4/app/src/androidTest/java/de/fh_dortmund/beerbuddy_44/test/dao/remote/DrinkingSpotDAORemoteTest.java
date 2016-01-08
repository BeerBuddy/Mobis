package de.fh_dortmund.beerbuddy_44.test.dao.remote;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.test.dao.common.DrinkingInvitationDAOTest;
import de.fh_dortmund.beerbuddy_44.test.dao.common.DrinkingSpotDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class DrinkingSpotDAORemoteTest extends DrinkingSpotDAOTest {
    private  DrinkingSpotDAO drinkingSpotDAO;
    private  PersonDAO personDAO;

    public DrinkingSpotDAORemoteTest() {
        super();

    }

    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAORemote(getActivity());
        drinkingSpotDAO = new DrinkingSpotDAORemote(getActivity());
    }

    public DrinkingSpotDAO getDrinkingSpotDAO() {
        return drinkingSpotDAO;
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }
}
