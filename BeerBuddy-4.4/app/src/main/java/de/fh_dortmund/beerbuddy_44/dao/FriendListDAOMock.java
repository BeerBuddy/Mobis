package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.MockUtil;

/**
 * Created by David on 30.11.2015.
 */
class FriendListDAOMock extends FriendListDAO {
    Map<Long, FriendList> friendlist = new HashMap<Long, FriendList>();

    public FriendListDAOMock(Context context) {
        super(context);
    }

    @Override
    public boolean isFriendFromId(long personid, long friendid) throws BeerBuddyException {
        FriendList persons = friendlist.get(personid);
        if(persons == null)
        {
            persons = new FriendList();
            persons.setId(personid);
            persons.setPersonid(personid);
            persons.setFriends( MockUtil.createRandomPersons(((int)(Math.random() * 10))));
            friendlist.put(personid,persons);
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
    public FriendList getFriendList(long personid) throws BeerBuddyException {
        FriendList persons = friendlist.get(personid);
        if(persons == null)
        {
            persons = new FriendList();
            persons.setId(personid);
            persons.setPersonid(personid);
            persons.setFriends( MockUtil.createRandomPersons(((int)(Math.random() * 10))));
            friendlist.put(personid,persons);
        }
        return persons;
    }

    @Override
    public void insertOrUpdate(FriendList friendList) throws BeerBuddyException {
        //TODO: Implementation for David!
    }
}