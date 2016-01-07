package de.fh_dortmund.beerbuddy.interfaces;

import java.util.List;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by Andreas on 30.12.2015.
 */
public interface IInvitationDAO<E> {

    void insertOrUpdate(E i) throws BeerBuddyException;

    List<E> getAllFor(long personid) throws BeerBuddyException;

    List<E> getAllFrom(long personid) throws BeerBuddyException;

    /**
     * TODO: Zu klären ob es einen Unterschied zwischen insertOrUpdate und accept für die Persistierung gibt!
     */
    void accept(E invitation) throws BeerBuddyException;
}
