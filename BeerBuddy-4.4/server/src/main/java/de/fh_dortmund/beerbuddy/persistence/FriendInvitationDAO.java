package de.fh_dortmund.beerbuddy.persistence;

import org.hibernate.SessionFactory;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IInvitationDAO;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by Andreas on 30.12.2015.
 */
public class FriendInvitationDAO extends AbstractDAO<FriendInvitation> implements IInvitationDAO<FriendInvitation> {

    public FriendInvitationDAO(SessionFactory factory) {
        super(factory);
    }

    public void insertOrUpdate(FriendInvitation i) {
        persist(i);
    }

    public List<FriendInvitation> getAllFor(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM FriendInvitation i WHERE i.eingeladenerId=" + personid).list();
    }

    public List<FriendInvitation> getAllFrom(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM FriendInvitation i WHERE i.einladerId=" + personid).list();
    }

    public void accept(FriendInvitation invitation) throws BeerBuddyException {
        persist(invitation);
    }
}
