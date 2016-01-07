package de.fh_dortmund.beerbuddy.resources;

import com.codahale.metrics.annotation.Timed;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.persistence.DrinkingSpotDAO;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Created by Andreas on 04.01.2016.
 */
@Path("/drinkingspot")
public class DrinkingSpotResource {

    private final DrinkingSpotDAO drinkingSpotDAO;

    public DrinkingSpotResource(DrinkingSpotDAO drinkingSpotDAO) {
        this.drinkingSpotDAO = drinkingSpotDAO;
    }

    @GET
    @Timed
    @Path("/getall")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public List<DrinkingSpot> getAll() throws BeerBuddyException {
        return drinkingSpotDAO.getAll();
    }

    @GET
    @Timed
    @Path("/getactive/{personid}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public DrinkingSpot getActiveByPersonId(@PathParam("personid") long personId) throws BeerBuddyException {
        return drinkingSpotDAO.getActiveByPersonId(personId);
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public DrinkingSpot insertOrUpdate(DrinkingSpot drinkingSpot) throws BeerBuddyException {
        return drinkingSpotDAO.insertOrUpdate(drinkingSpot);
    }

    @GET
    @Timed
    @Path("/get/{dsid}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public DrinkingSpot getById(@PathParam("dsid") long dsid) throws BeerBuddyException {
        return drinkingSpotDAO.getById(dsid);
    }

    @POST
    @Timed
    @Path("/join/{dsid}/{personid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public void join(@PathParam("dsid") long dsid, @PathParam("personid") long personId) throws BeerBuddyException {
        drinkingSpotDAO.join(dsid, personId);
    }

    @GET
    @Timed
    @Path("/deactivate/{dsid}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public void deactivate(@PathParam("dsid") long dsid) throws BeerBuddyException {
        drinkingSpotDAO.deactivate(dsid);
    }
}
