package de.fh_dortmund.beerbuddy_44.dao.interfaces;

import android.content.Context;

import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy.interfaces.IPersonDAO;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by David on 09.11.2015.
 */
public abstract class PersonDAO  {

    protected BeerBuddyActivity context;

    public PersonDAO(BeerBuddyActivity context) {
        this.context = context;
    }

    public abstract void getAll(RequestListener<Person[]> listener);

    public abstract void getById(long id, RequestListener<Person> listener);

    public abstract void getByEmail(String mail, RequestListener<Person> listener);

    public abstract void insertOrUpdate(Person p, RequestListener<Person> listener);
}
