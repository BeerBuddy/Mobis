package de.fh_dortmund.beerbuddy.persistence;

import com.google.common.base.Optional;

import org.hibernate.SessionFactory;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IPersonDAO;
import io.dropwizard.hibernate.AbstractDAO;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Andreas on 17.12.2015.
 */
public class PersonDAO extends AbstractDAO<Person> implements IPersonDAO {

    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Person> getAll() throws BeerBuddyException {
        return super.currentSession().createQuery("FROM Person").list();
    }

    public Person getById(long id) throws BeerBuddyException {
        return Optional.fromNullable(get(id)).get();
    }

    public Person getByEmail(String mail) throws BeerBuddyException {
        List persons = super.currentSession().createQuery("FROM Person p WHERE p.email LIKE '" + mail + "'").list();
        if(persons.isEmpty()){
            return null;
        }
        return (Person)persons.get(0);
    }

    public Person insertOrUpdate(Person p) throws BeerBuddyException {
        long version = p.getVersion();
        p.setVersion(++version);
        return persist(p);
    }
}
