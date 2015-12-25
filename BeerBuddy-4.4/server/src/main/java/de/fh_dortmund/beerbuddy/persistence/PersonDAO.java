package de.fh_dortmund.beerbuddy.persistence;

import com.google.common.base.Optional;

import org.hibernate.SessionFactory;

import java.util.List;

import de.fh_dortmund.beerbuddy.Person;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by Andreas on 17.12.2015.
 */
public class PersonDAO extends AbstractDAO<Person> {
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public List<Person> findAll() {
        List<Person> persons = super.currentSession().createQuery("FROM Person").list();
        return persons;
    }

    public Person create(Person person) {
        return persist(person);
    }

    public Person remove(Long id) {
        Person person = findById(id).get();
        if (person != null) {
            currentSession().delete(person);
        }
        return person;
    }
}
