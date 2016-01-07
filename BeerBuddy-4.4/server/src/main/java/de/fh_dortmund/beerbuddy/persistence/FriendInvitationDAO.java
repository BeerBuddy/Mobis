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

    private final FriendListDAO friendListDAO;

    public FriendInvitationDAO(SessionFactory factory, FriendListDAO friendListDAO) {
        super(factory);
        this.friendListDAO = friendListDAO;
    }

    public FriendInvitation insertOrUpdate(FriendInvitation i) {
        long version = i.getVersion();
        i.setVersion(++version);
        return persist(i);
    }

    public List<FriendInvitation> getAllFor(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM FriendInvitation i WHERE i.eingeladenerId=" + personid).list();
    }

    public List<FriendInvitation> getAllFrom(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM FriendInvitation i WHERE i.einladerId=" + personid).list();
    }

    public void accept(FriendInvitation invitation) throws BeerBuddyException {
        // Nach dem akzeptieren werden beide in die Freundesliste des jeweils anderen geschrieben und die Invitation gelöscht
        Long einladerId = invitation.getEinladerId();
        Long eingeladenerId = invitation.getEingeladenerId();

        friendListDAO.addPersonToFriendList(einladerId, eingeladenerId);
        friendListDAO.addPersonToFriendList(eingeladenerId, einladerId);

        super.currentSession().delete(invitation);
    }

    @Override
    public void decline(FriendInvitation invitation) throws BeerBuddyException {
        super.currentSession().delete(invitation);
    }
}
