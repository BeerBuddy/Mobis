package de.fh_dortmund.beerbuddy.resources;

import com.codahale.metrics.annotation.Timed;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.persistence.*;
import io.dropwizard.hibernate.UnitOfWork;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Path("/person")
public class PersonResource {

    private final PersonDAO personDAO;

    public PersonResource(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GET
    @Timed
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Person getPerson(@PathParam("id") long id) throws BeerBuddyException {
        return personDAO.getById(id);
    }

    /*
    @GET
    @Timed
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Person removePerson(@PathParam("id") long id) {
        throw new NotImplementedException();
    }
    */

    @GET
    @Timed
    @Path("/all")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPersons() throws BeerBuddyException {
        return personDAO.getAll();
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public void addPerson(Person person) throws BeerBuddyException {
        personDAO.insertOrUpdate(person);
    }
}
