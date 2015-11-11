package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by David on 09.11.2015.
 */
class PersonDAOMock extends PersonDAO {

    HashMap<Long,Person> users= new HashMap<Long,Person>();

    public PersonDAOMock(Context context) {
        super(context);
    }

    @Override
    public List<Person> getAll() {
        List<Person> list = new ArrayList<Person>(this.users.values());
        return list;
    }

    @Override
    public Person getById(long id) {
        return users.get(id);
    }

    @Override
    public Person getByEmail(String mail) {
        for(Map.Entry<Long, Person> e : users.entrySet()) {
            if(e.getValue().getEmail().equals(mail))
            {
                return e.getValue();
            }
        }
        return null;
    }

    @Override
    public void insertOrUpdate(Person p) {
        if(p.getId() == 0)
        {
            p.setId(generateID());
        }
        users.put(p.getId(), p);
    }

    private long generateID() {
        long id = (long)Math.random()*Long.MAX_VALUE;
        return (users.get(id) != null) ? id : generateID();
    }
}
