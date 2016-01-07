package de.fh_dortmund.beerbuddy.persistence;

import org.hibernate.SessionFactory;


import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IInvitationDAO;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by Andreas on 30.12.2015.
 */
public class DrinkingInvitationDAO extends AbstractDAO<DrinkingInvitation> implements IInvitationDAO<DrinkingInvitation> {

    private final DrinkingSpotDAO drinkingSpotDAO;

    public DrinkingInvitationDAO(SessionFactory factory, DrinkingSpotDAO drinkingSpotDAO) {
        super(factory);
        this.drinkingSpotDAO = drinkingSpotDAO;
    }

    public DrinkingInvitation insertOrUpdate(DrinkingInvitation i) {
        long version = i.getVersion();
        i.setVersion(++version);
        return persist(i);
    }

    public List<DrinkingInvitation> getAllFor(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM DrinkingInvitation i WHERE i.eingeladenerId=" + personid).list();
    }

    public List<DrinkingInvitation> getAllFrom(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM DrinkingInvitation i WHERE i.einladerId=" + personid).list();
    }

    public void accept(DrinkingInvitation invitation) throws BeerBuddyException {
        Long einladerId = invitation.getEinladerId();
        Long eingeladenerId = invitation.getEingeladenerId();
        Long drinkingSpotId = invitation.getDrinkingSpotId();

        // join dem drinkingSpot
        drinkingSpotDAO.join(drinkingSpotId, eingeladenerId);

        super.currentSession().delete(invitation);
    }

    public void decline(DrinkingInvitation invitation) throws BeerBuddyException {
        super.currentSession().delete(invitation);
    }
}
