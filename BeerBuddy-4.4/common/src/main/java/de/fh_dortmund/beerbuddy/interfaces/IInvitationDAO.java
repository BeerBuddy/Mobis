package de.fh_dortmund.beerbuddy.interfaces;

import java.util.List;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by Andreas on 30.12.2015.
 */
public interface IInvitationDAO<E> {

    E insertOrUpdate(E i) throws BeerBuddyException;

    List<E> getAllFor(long personid) throws BeerBuddyException;

    List<E> getAllFrom(long personid) throws BeerBuddyException;

    /**
     * TODO: Zu klären ob es einen Unterschied zwischen insertOrUpdate und accept für die Persistierung gibt!
     * DO: Ja gibt es. Nach dem akzeptieren werden beide in die Freundesliste des jeweils anderen geschrieben und die Invitation gelöscht
     */
    void accept(E invitation) throws BeerBuddyException;

    void decline (E invitation) throws BeerBuddyException;
}
