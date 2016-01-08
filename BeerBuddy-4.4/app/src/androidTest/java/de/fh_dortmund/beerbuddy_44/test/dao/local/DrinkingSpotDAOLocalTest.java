package de.fh_dortmund.beerbuddy_44.test.dao.local;

import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.test.dao.common.DrinkingSpotDAOTest;

/**
 * Created by grimm on 08.01.2016.
 */
public class DrinkingSpotDAOLocalTest extends DrinkingSpotDAOTest {
    private  DrinkingSpotDAO drinkingSpotDAO;
    private  PersonDAO personDAO;

    public DrinkingSpotDAOLocalTest() {
        super();

    }

    @Override
    public void setUp() throws Exception {
        personDAO = new PersonDAOLocal(getActivity());
        drinkingSpotDAO = new DrinkingSpotDAOLocal(getActivity());
    }

    public DrinkingSpotDAO getDrinkingSpotDAO() {
        return drinkingSpotDAO;
    }


    public PersonDAO getPersonDAO() {
        return personDAO;
    }
}
