package de.fh_dortmund.beerbuddy_44.dao.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
public class DrinkingSpotDAOLocal extends DrinkingSpotDAO {

    BeerBuddyDbHelper dbHelper;
    DrinkingSpotPersonDAO drinkingSpotPersonDAO;
    PersonDAOLocal personDAOLocal;

    public DrinkingSpotDAOLocal(BeerBuddyActivity context) {
        super(context);
        dbHelper = BeerBuddyDbHelper.getInstance(context);
        drinkingSpotPersonDAO = new DrinkingSpotPersonDAO(context);
        personDAOLocal = new PersonDAOLocal(context);
    }

    @Override
    public void getAll(RequestListener<DrinkingSpot[]> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("drinkingspot", new String[]{"id", "creatorId", "beschreibung", "startTime", "ageFrom", "ageTo", "gps", "amountMaleWithoutBeerBuddy", "amountFemaleWithoutBeerBuddy", "active"}, null, null, null, null, null);
            List<DrinkingSpot> list = new LinkedList<DrinkingSpot>();
            while (dbCursor.moveToNext()) {
                DrinkingSpot di = getDrinkingSpot(dbCursor, database);
                list.add(di);
            }
            listener.onRequestSuccess(list.toArray(new DrinkingSpot[]{}));
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    private DrinkingSpot getDrinkingSpot(Cursor dbCursor, SQLiteDatabase database) throws DataAccessException, ParseException {
        DrinkingSpot di = new DrinkingSpot();
        di.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        //TODO join
        di.setPersons(drinkingSpotPersonDAO.getByDsId(di.getId(), database));
        //TODO join
        di.setCreator(personDAOLocal.getById(di.getId(), database));
        di.setBeschreibung(dbCursor.getString(dbCursor.getColumnIndex("beschreibung")));
        di.setStartTime(BeerBuddyDbHelper.DATE_FORMAT.parse(dbCursor.getString(dbCursor.getColumnIndex("startTime"))));
        di.setAgeFrom(dbCursor.getInt(dbCursor.getColumnIndex("ageFrom")));
        di.setAgeTo(dbCursor.getInt(dbCursor.getColumnIndex("ageTo")));
        di.setGps(dbCursor.getString(dbCursor.getColumnIndex("gps")));
        di.setAmountMaleWithoutBeerBuddy(dbCursor.getInt(dbCursor.getColumnIndex("amountMaleWithoutBeerBuddy")));
        di.setAmountFemaleWithoutBeerBuddy(dbCursor.getInt(dbCursor.getColumnIndex("amountFemaleWithoutBeerBuddy")));
        di.setActive(dbCursor.getInt(dbCursor.getColumnIndex("active")) == BeerBuddyDbHelper.BOOLEAN_TRUE);
        return di;
    }


    @Override
    public void getActiveByPersonId(long personId, RequestListener<DrinkingSpot> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("drinkingspot", new String[]{"id", "creatorId", "beschreibung", "startTime", "ageFrom", "ageTo", "gps", "amountMaleWithoutBeerBuddy", "amountFemaleWithoutBeerBuddy", "active"}, "active=? and personid=?", new String[]{BeerBuddyDbHelper.BOOLEAN_TRUE + "", personId + ""}, null, null, null);
            while (dbCursor.moveToNext()) {
                listener.onRequestSuccess(getDrinkingSpot(dbCursor, database));
            }
        } catch (Exception e) {
            listener.onRequestFailure(new SpiceException(e));
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }


    @Override
    public void insertOrUpdate(final DrinkingSpot drinkingSpot, final RequestListener<DrinkingSpot> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {

            if (drinkingSpot.getId() != 0) {
                listener.onRequestSuccess(update(drinkingSpot,database));
            } else {
                listener.onRequestSuccess(insert(drinkingSpot,database));
            }
        } catch (DataAccessException e) {
            listener.onRequestFailure(new SpiceException(e));
            e.printStackTrace();
        }finally {
            database.close();
        }


    }

    private DrinkingSpot insert(DrinkingSpot drinkingSpot, SQLiteDatabase database) throws DataAccessException {
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO drinkingspot (creatorId,beschreibung,startTime,ageFrom,ageTo,gps,amountMaleWithoutBeerBuddy,amountFemaleWithoutBeerBuddy,active) VALUES (?,?,?,?,?,?,?,?,?)");

            stmt.bindLong(1, drinkingSpot.getCreator().getId());
            if (drinkingSpot.getBeschreibung() != null)
                stmt.bindString(2, drinkingSpot.getBeschreibung());
            stmt.bindString(3, BeerBuddyDbHelper.DATE_FORMAT.format(drinkingSpot.getStartTime()));
            stmt.bindLong(4, drinkingSpot.getAgeFrom());
            stmt.bindLong(5, drinkingSpot.getAgeTo());
            stmt.bindString(6, drinkingSpot.getGps());
            stmt.bindLong(7, drinkingSpot.getAmountMaleWithoutBeerBuddy());
            stmt.bindLong(8, drinkingSpot.getAmountFemaleWithoutBeerBuddy());
            stmt.bindLong(9, drinkingSpot.isActive() ? BeerBuddyDbHelper.BOOLEAN_TRUE : BeerBuddyDbHelper.BOOLEAN_FALSE);
            long dsid = stmt.executeInsert();
            drinkingSpotPersonDAO.saveAll(dsid, drinkingSpot.getPersons(), database);
            drinkingSpot.setId(dsid);
            return drinkingSpot;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);

        } finally {
            database.endTransaction();
        }
    }

    private DrinkingSpot update(DrinkingSpot drinkingSpot, SQLiteDatabase database) throws DataAccessException {
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE drinkingspot SET creatorId=?,beschreibung=?,ageFrom=?,ageTo=?,gps=?,amountMaleWithoutBeerBuddy=?,amountFemaleWithoutBeerBuddy=?,active WHERE id = ?");
            stmt.bindLong(8, drinkingSpot.getCreator().getId());
            stmt.bindString(1, drinkingSpot.getBeschreibung());
            stmt.bindLong(2, drinkingSpot.getAgeFrom());
            stmt.bindLong(3, drinkingSpot.getAgeTo());
            stmt.bindString(4, drinkingSpot.getGps());
            stmt.bindLong(5, drinkingSpot.getAmountMaleWithoutBeerBuddy());
            stmt.bindLong(6, drinkingSpot.getAmountFemaleWithoutBeerBuddy());
            stmt.bindLong(7, drinkingSpot.isActive() ? BeerBuddyDbHelper.BOOLEAN_TRUE : BeerBuddyDbHelper.BOOLEAN_FALSE);
            stmt.executeInsert();
            drinkingSpotPersonDAO.deleteAll(drinkingSpot.getId(), database);
            drinkingSpotPersonDAO.saveAll(drinkingSpot.getId(), drinkingSpot.getPersons(), database);
            return drinkingSpot;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.endTransaction();

        }
    }

    @Override
    public void getById(long dsid, RequestListener<DrinkingSpot> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("drinkingspot", new String[]{"id", "creatorId", "beschreibung", "startTime", "ageFrom", "ageTo", "gps", "amountMaleWithoutBeerBuddy", "amountFemaleWithoutBeerBuddy", "active"}, "id=?", new String[]{dsid + ""}, null, null, null);
            List<DrinkingSpot> list = new LinkedList<DrinkingSpot>();
            while (dbCursor.moveToNext()) {
                listener.onRequestSuccess(getDrinkingSpot(dbCursor, database));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    @Override
    public void join(long dsid, final long personId, final RequestListener<Void> listener) {
        getById(dsid, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(final DrinkingSpot drinkingSpot) {
                new PersonDAOLocal(context).getById(personId, new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        listener.onRequestFailure(spiceException);
                    }

                    @Override
                    public void onRequestSuccess(Person person) {
                        drinkingSpot.getPersons().add(person);
                        insertOrUpdate(drinkingSpot, new RequestListener<DrinkingSpot>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                listener.onRequestFailure(spiceException);
                            }

                            @Override
                            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                                listener.onRequestSuccess(null);
                            }
                        });
                    }
                });
            }
        });


    }

    public void join(long dsid, long personId, SQLiteDatabase database) throws DataAccessException {
        DrinkingSpot drinkingSpot = null;
            drinkingSpot = getById(dsid);
            drinkingSpot.getPersons().add(personDAOLocal.getById(personId, database));
        insertOrUpdate(drinkingSpot,  database);
    }

    public DrinkingSpot insertOrUpdate(DrinkingSpot drinkingSpot, SQLiteDatabase database) throws DataAccessException {
        if (drinkingSpot.getId() != 0) {
            return update(drinkingSpot,database);
        } else {
            return insert(drinkingSpot,database);
        }

    }

    private DrinkingSpot getById(long dsid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("drinkingspot", new String[]{"id", "creatorId", "beschreibung", "startTime", "ageFrom", "ageTo", "gps", "amountMaleWithoutBeerBuddy", "amountFemaleWithoutBeerBuddy", "active"}, "id=?", new String[]{dsid + ""}, null, null, null);
            List<DrinkingSpot> list = new LinkedList<DrinkingSpot>();
            while (dbCursor.moveToNext()) {
                return getDrinkingSpot(dbCursor, database);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    @Override
    public void deactivate(long dsid, final RequestListener<Void> listener) {
        getById(dsid, new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                listener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(final DrinkingSpot drinkingSpot) {
                drinkingSpot.setActive(false);
                insertOrUpdate(drinkingSpot, new RequestListener<DrinkingSpot>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        listener.onRequestFailure(spiceException);
                    }

                    @Override
                    public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                        listener.onRequestSuccess(null);
                    }
                });
            }
        });

    }

}
