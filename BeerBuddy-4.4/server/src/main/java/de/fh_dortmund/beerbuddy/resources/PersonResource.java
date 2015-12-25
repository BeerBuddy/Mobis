package de.fh_dortmund.beerbuddy.resources;

import com.codahale.metrics.annotation.Timed;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy.persistence.*;
import io.dropwizard.hibernate.UnitOfWork;

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
    public Person getPerson(@PathParam("id") long id) {
        return personDAO.findById(id).get();
    }

    @GET
    @Timed
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Person removePerson(@PathParam("id") long id) {
        return personDAO.remove(id);
    }

    @GET
    @Timed
    @Path("/all")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPersons() {
        return personDAO.findAll();
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public Person addPerson(Person person) {
        return personDAO.create(person);
    }
}
