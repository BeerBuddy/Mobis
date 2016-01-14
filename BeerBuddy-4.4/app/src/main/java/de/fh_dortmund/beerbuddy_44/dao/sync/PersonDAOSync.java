package de.fh_dortmund.beerbuddy_44.dao.sync;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.PersonInsertSync;
import de.fh_dortmund.beerbuddy_44.dao.sync.model.PersonUpdateSync;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 09.11.2015.
 */
public class PersonDAOSync extends PersonDAO {

    private final PersonDAOLocal local;
    private final PersonDAORemote remote;

    public PersonDAOSync(BeerBuddyActivity context) {
        super(context);
        this.remote = new PersonDAORemote(context);
        this.local = new PersonDAOLocal(context);
    }

    @Override
    public void getAll(final RequestListener<Person[]> listener) {
        remote.getAll(new RequestListener<Person[]>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getAll(listener);
            }

            @Override
            public void onRequestSuccess(Person[] persons) {
                listener.onRequestSuccess(persons);
                for (Person p : persons) {
                    try {
                        local.delete(p);
                        local.insert(p);
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void getById(final long id, final RequestListener<Person> listener) {
        remote.getById(id, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getById(id, listener);
            }

            @Override
            public void onRequestSuccess(Person person) {
                listener.onRequestSuccess(person);
                try {
                    local.delete(person);
                    local.insert(person);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void getByEmail(final String mail, final RequestListener<Person> listener) {
        remote.getByEmail(mail, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                local.getByEmail(mail, listener);
            }

            @Override
            public void onRequestSuccess(Person person) {
                listener.onRequestSuccess(person);
                try {
                    local.delete(person);
                    local.insert(person);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void insertOrUpdate(final Person p, final RequestListener<Person> listener) {
        remote.insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (p.getId() == 0) {
                    try {
                        Person personLocal = local.insert(p);
                        context.getSyncService().addSyncModel(new PersonInsertSync(personLocal, remote));
                        listener.onRequestSuccess(personLocal);
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        listener.onRequestSuccess(local.update(p));
                        context.getSyncService().addSyncModel(new PersonUpdateSync(p, remote));
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onRequestSuccess(Person person) {
                listener.onRequestSuccess(person);
                try {
                    local.insert(person);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}