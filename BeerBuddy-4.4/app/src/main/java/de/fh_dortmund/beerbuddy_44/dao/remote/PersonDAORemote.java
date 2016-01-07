package de.fh_dortmund.beerbuddy_44.dao.remote;

import android.content.Context;

import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;
import de.fh_dortmund.beerbuddy_44.requests.GetAllPersonsRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetByEmailPersonRequest;
import de.fh_dortmund.beerbuddy_44.requests.GetByIDPersonRequest;
import de.fh_dortmund.beerbuddy_44.requests.SavePersonRequest;

/**
 * Created by David on 09.11.2015.
 */
public class PersonDAORemote extends PersonDAO {


    public PersonDAORemote(BeerBuddyActivity context){super(context);
    }
    @Override
    public void getAll(RequestListener<Person[]> listener) {
            GetAllPersonsRequest getAllPersonsRequest = new GetAllPersonsRequest();
            context.getSpiceManager().execute(getAllPersonsRequest, listener);
    }
    @Override
    public void getById(long id,RequestListener<Person> listener) {
            GetByIDPersonRequest req = new GetByIDPersonRequest(id);
            context.getSpiceManager().execute(req, listener);
    }
    @Override
    public void getByEmail(String mail,RequestListener<Person> listener) {
            GetByEmailPersonRequest req = new GetByEmailPersonRequest(mail);
            context.getSpiceManager().execute(req, listener);
    }
    @Override
    public void insertOrUpdate(Person p,RequestListener<Person> listener) {
                SavePersonRequest req = new SavePersonRequest(p);
            context.getSpiceManager().execute(req, listener);
    }
}
