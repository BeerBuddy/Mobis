package de.fh_dortmund.beerbuddy.persistence;

import org.hibernate.SessionFactory;


import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IInvitationDAO;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by Andreas on 30.12.2015.
 */
public class DrinkingInvitationDAO extends AbstractDAO<DrinkingInvitation> implements IInvitationDAO<DrinkingInvitation> {

    public DrinkingInvitationDAO(SessionFactory factory) {
        super(factory);
    }

    public void insertOrUpdate(DrinkingInvitation i) {
        persist(i);
    }

    public List<DrinkingInvitation> getAllFor(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM DrinkingInvitation i WHERE i.eingeladenerId=" + personid).list();
    }

    public List<DrinkingInvitation> getAllFrom(long personid) throws BeerBuddyException {
        return super.currentSession().createQuery("FROM DrinkingInvitation i WHERE i.einladerId=" + personid).list();
    }

    public void accept(DrinkingInvitation invitation) throws BeerBuddyException {
        persist(invitation);
    }
}
