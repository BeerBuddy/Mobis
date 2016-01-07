package de.fh_dortmund.beerbuddy.resources;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.persistence.FriendListDAO;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Created by Andreas on 04.01.2016.
 */
@Path("/friendlist")
public class FriendListResource {

    private final FriendListDAO friendListDAO;

    public FriendListResource(FriendListDAO friendListDAO) {
        this.friendListDAO = friendListDAO;
    }

    @GET
    @Timed
    @Path("/isFriendFrom/{personid}/{friendid}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public boolean isFriendFromId(@PathParam("personid") long personid, @PathParam("friendid") long friendid) throws BeerBuddyException {
        return friendListDAO.isFriendFromId(personid, friendid);
    }

    @GET
    @Timed
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public FriendList getFriendList(@PathParam("id") long personid) throws BeerBuddyException {
        return friendListDAO.getFriendList(personid);
    }

    @POST
    @Timed
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    @UnitOfWork
    public FriendList insertOrUpdate(FriendList friendList) throws BeerBuddyException {
        return friendListDAO.insertOrUpdate(friendList);
    }
}
