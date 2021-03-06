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

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.persistence.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy.persistence.FriendInvitationDAO;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/friendinvitation")
public class FriendInvitationResource {

    private final FriendInvitationDAO invitationDAO;

    public FriendInvitationResource(FriendInvitationDAO friendInvitationDAO) {
        this.invitationDAO = friendInvitationDAO;
    }

    @GET
    @Timed
    @Path("/getallfor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public List<FriendInvitation> getAllFor(@PathParam("id") long personid) throws BeerBuddyException {
        return invitationDAO.getAllFor(personid);
    }

    @GET
    @Timed
    @Path("/getallfrom/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public List<FriendInvitation> getAllFrom(@PathParam("id") long personid) throws BeerBuddyException {
        return invitationDAO.getAllFrom(personid);
    }

    @POST
    @Timed
    @Path("/save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public FriendInvitation addFriendInvitation(FriendInvitation friendInvitation) throws BeerBuddyException {
        return invitationDAO.insertOrUpdate(friendInvitation);
    }

    @POST
    @Timed
    @Path("/accept")
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public void accept(FriendInvitation friendInvitation) throws BeerBuddyException {
        invitationDAO.accept(friendInvitation);
    }

    @POST
    @Timed
    @Path("/decline")
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public void decline(FriendInvitation friendInvitation) throws BeerBuddyException {
        invitationDAO.decline(friendInvitation);
    }
}
