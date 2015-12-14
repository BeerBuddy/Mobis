package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.MockUtil;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 30.11.2015.
 */
class FriendListDAOMock extends FriendListDAO {
    public FriendListDAOMock(Context context) {
        super(context);
    }

    Map<Long, FriendList> freindlist = new HashMap<Long, FriendList>();


    @Override
    public boolean isFriendFromId(long personid, long firendid) throws BeerBuddyException {
        FriendList persons = freindlist.get(personid);
        if(persons == null)
        {
            persons = new FriendList();
            persons.setId(personid);
            persons.setPersonid(personid);
            persons.setFriends( MockUtil.createRandomPersons(((int)(Math.random() * 10))));
            freindlist.put(personid,persons);
        }

        for(Person p :persons.getFriends())
        {
            if(p.getId()==personid)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public FriendList getFriendListId(long personid) throws BeerBuddyException {
        FriendList persons = freindlist.get(personid);
        if(persons == null)
        {
            persons = new FriendList();
            persons.setId(personid);
            persons.setPersonid(personid);
            persons.setFriends( MockUtil.createRandomPersons(((int)(Math.random() * 10))));
            freindlist.put(personid,persons);
        }
        return persons;
    }

}
