package de.fh_dortmund.beerbuddy.persistence;

import com.google.common.base.Optional;

import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IDrinkingSpotDAO;
import de.fh_dortmund.beerbuddy.interfaces.IPersonDAO;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by Andreas on 04.01.2016.
 */
public class DrinkingSpotDAO extends AbstractDAO<DrinkingSpot> implements IDrinkingSpotDAO {

    private IPersonDAO personDAO;

    public DrinkingSpotDAO(SessionFactory factory, IPersonDAO personDAO) {
        super(factory);
        this.personDAO = personDAO;
    }

    public List<DrinkingSpot> getAll() throws BeerBuddyException {
        return super.currentSession().createQuery("FROM DrinkingSpot ds").list();
    }

    public DrinkingSpot getActiveByPersonId(long personId) throws BeerBuddyException {
        List<DrinkingSpot> drinkingSpots = super.currentSession().createQuery("FROM DrinkingSpot ds WHERE ds.creator.id=" + personId).list();
        if (drinkingSpots != null && !drinkingSpots.isEmpty()) {
            return drinkingSpots.get(0);
        }
        throw new BeerBuddyException("No DrinkingSpot found for personId: " + personId);
    }

    public void insertOrUpdate(DrinkingSpot drinkingSpot) throws BeerBuddyException {
        persist(drinkingSpot);
    }

    public DrinkingSpot getById(long dsid) throws BeerBuddyException {
        return Optional.fromNullable(get(dsid)).get();
    }

    public void join(long dsid, long personId) throws BeerBuddyException {
        DrinkingSpot drinkingSpot = getById(dsid);
        Collection<Person> persons = drinkingSpot.getPersons();
        persons.add(personDAO.getById(personId));
        drinkingSpot.setPersons(persons);
        persist(drinkingSpot);
    }
}
