package de.fh_dortmund.beerbuddy_44.dao.sync.model;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DAO;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by grimm on 11.01.2016.
 */
public class PersonInsertSync extends SyncModel<Person, PersonDAORemote>{
    public PersonInsertSync(Person id, PersonDAORemote personDAORemote) {
        super(id, personDAORemote);
    }

    @Override
    public void performSync(final BeerBuddyActivity activity) {
        remoteDAO = new PersonDAORemote(activity);
        localEntity.setId(0);
        remoteDAO.insertOrUpdate(localEntity, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                activity.getSyncService().addSyncModel(new PersonInsertSync(localEntity, remoteDAO));
            }

            @Override
            public void onRequestSuccess(Person person) {
                PersonDAOLocal personDAOLocal = new PersonDAOLocal(activity);
                try {
                    personDAOLocal.delete(localEntity);
                    personDAOLocal.insert(person);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
