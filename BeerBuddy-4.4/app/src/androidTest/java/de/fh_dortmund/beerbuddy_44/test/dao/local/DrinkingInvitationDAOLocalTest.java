package de.fh_dortmund.beerbuddy_44.test.dao.local;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingInvitationDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingInvitationDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.DrinkingSpotDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.test.dao.common.DrinkingInvitationDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class DrinkingInvitationDAOLocalTest extends DrinkingInvitationDAOTest {
    private  DrinkingSpotDAO drinkingSpotDAO;
    private  DrinkingInvitationDAO drinkingInvitationDAO;
    private  PersonDAO personDAO;

    public DrinkingInvitationDAOLocalTest() {
        super();

    }

    @Override
    public void setUp() throws Exception {
        drinkingInvitationDAO = new DrinkingInvitationDAOLocal(getActivity());
        personDAO = new PersonDAOLocal(getActivity());
        drinkingSpotDAO = new DrinkingSpotDAOLocal(getActivity());
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
