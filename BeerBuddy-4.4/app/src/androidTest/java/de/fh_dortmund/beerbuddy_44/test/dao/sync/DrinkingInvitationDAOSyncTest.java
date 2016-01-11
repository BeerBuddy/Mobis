package de.fh_dortmund.beerbuddy_44.test.dao.sync;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.DrinkingInvitationDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.DrinkingSpotDAOSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.PersonDAOSync;
import de.fh_dortmund.beerbuddy_44.test.dao.common.DrinkingInvitationDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class DrinkingInvitationDAOSyncTest extends DrinkingInvitationDAOTest {
    private  DrinkingSpotDAO drinkingSpotDAO;
    private  DrinkingInvitationDAO drinkingInvitationDAO;
    private  PersonDAO personDAO;

    public DrinkingInvitationDAOSyncTest() {
        super();

    }

    @Override
    public void setUp() throws Exception {
        drinkingInvitationDAO = new DrinkingInvitationDAOSync(getActivity());
        personDAO = new PersonDAOSync(getActivity());
        drinkingSpotDAO = new DrinkingSpotDAOSync(getActivity());
    }

    public DrinkingSpotDAO getDrinkingSpotDAO() {
        return drinkingSpotDAO;
    }

    public DrinkingInvitationDAO getDrinkingInvitationDAO() {
        return drinkingInvitationDAO;
    }

    public PersonDAO getPersonDAO() {
        return personDAO;
    }
}
