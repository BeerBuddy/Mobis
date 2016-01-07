package de.fh_dortmund.beerbuddy.persistence;

import org.hibernate.SessionFactory;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IFriendListDAO;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by Andreas on 04.01.2016.
 */
public class FriendListDAO extends AbstractDAO<FriendList> implements IFriendListDAO {

    public FriendListDAO(SessionFactory factory) {
        super(factory);
    }

    public boolean isFriendFromId(long personid, long friendid) throws BeerBuddyException {
        FriendList friendList = getFriendLists(personid).get(0);
        for (Person person : friendList.getFriends()) {
            if (person.getId() == friendid) {
                return true;
            }
        }
        return false;
    }

    public FriendList getFriendList(long personid) throws BeerBuddyException {
        return getFriendLists(personid).get(0);
    }

    public void insertOrUpdate(FriendList friendList) throws BeerBuddyException {
        persist(friendList);
    }

    /**
     * Holt mögliche FriendLists aus der DB
     *
     * @param personid Id der Person
     * @return Liste an Friendlists
     * @throws BeerBuddyException Sollte keine FriendList gefunden werden
     */
    private List<FriendList> getFriendLists(long personid) throws BeerBuddyException {
        List<FriendList> friendLists = super.currentSession().createQuery("FROM FriendList fl WHERE fl.personid=" + personid).list();
        if (friendLists != null) {
            return friendLists;
        }
        throw new BeerBuddyException("No FriendList found for personId: " + personid);
    }

}
