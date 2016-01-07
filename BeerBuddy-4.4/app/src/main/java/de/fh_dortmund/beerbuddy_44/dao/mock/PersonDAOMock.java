package de.fh_dortmund.beerbuddy_44.dao.mock;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.MockUtil;

/**
 * Created by David on 09.11.2015.
 */
public class PersonDAOMock extends PersonDAO {
    private static final String TAG ="PersonDAOMock";

    HashMap<Long,Person> users= new HashMap<Long,Person>();

    public PersonDAOMock(Context context){
        super(context);
        List<Person> list =MockUtil.createRandomPersons(10);
        for(Person p:list)
        {
            users.put(p.getId(),p);
        }
    }

    @Override
    public List<Person> getAll() {
        List<Person> list = new ArrayList<Person>(this.users.values());
        return list;
    }

    @Override
    public Person getById(long id) {
        Person p = users.get(id);
        if(p == null)
        {
            p = MockUtil.createRandomPerson(id);
            users.put(id,p);
        }
        return p;
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
            p.setId((long)(Math.random()*(double)Long.MAX_VALUE)+1);
        }
        users.put(p.getId(), p);
        Log.d(TAG, "saved Person " + p.getId());
    }

}
