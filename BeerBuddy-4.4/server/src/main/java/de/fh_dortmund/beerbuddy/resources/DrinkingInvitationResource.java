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

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.persistence.DrinkingInvitationDAO;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/drinkinginvitation")
public class DrinkingInvitationResource {

    private final DrinkingInvitationDAO invitationDAO;

    public DrinkingInvitationResource(DrinkingInvitationDAO drinkingInvitationDAO) {
        this.invitationDAO = drinkingInvitationDAO;
    }

    @GET
    @Timed
    @Path("/getallfor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public List<DrinkingInvitation> getAllFor(@PathParam("id") long personid) throws BeerBuddyException {
        return invitationDAO.getAllFor(personid);
    }

    @GET
    @Timed
    @Path("/getallfrom/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public List<DrinkingInvitation> getAllFrom(@PathParam("id") long personid) throws BeerBuddyException {
        return invitationDAO.getAllFrom(personid);
    }

    @POST
    @Timed
    @Path("/save")
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public void addDrinkingInvitation(DrinkingInvitation drinkingInvitation) throws BeerBuddyException {
        invitationDAO.insertOrUpdate(drinkingInvitation);
    }

    @POST
    @Timed
    @Path("/accept")
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public void accept(DrinkingInvitation drinkingInvitation) throws BeerBuddyException {
        invitationDAO.accept(drinkingInvitation);
    }

    @POST
    @Timed
    @Path("/decline")
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public void decline(DrinkingInvitation drinkingInvitation) throws BeerBuddyException {
        invitationDAO.decline(drinkingInvitation);
    }
}
